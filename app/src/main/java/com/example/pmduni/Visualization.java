package com.example.pmduni;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Visualization extends AppCompatActivity {

    private EditText etNodes, etEdges;
    private Button btnDraw;
    private View graphView;
    private int numNodes, numEdges;
    private List<Integer>[] graph;
    private List<Node> nodes;
    private int[] nodeColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

        etNodes = findViewById(R.id.et_nodes);
        etEdges = findViewById(R.id.et_edges);
        btnDraw = findViewById(R.id.btn_draw);
        graphView = findViewById(R.id.graphView);

        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGraph();
            }
        });
    }

    private void createGraph() {
        try {
            numNodes = Integer.parseInt(etNodes.getText().toString());
            numEdges = Integer.parseInt(etEdges.getText().toString());
            if (numEdges > numNodes * (numNodes - 1) / 2) {
                Toast.makeText(this, "Invalid number of edges", Toast.LENGTH_SHORT).show();
                return;
            }

            graph = generateRandomGraph(numNodes, numEdges);
            nodeColors = graphColoring(graph);

            Bitmap bitmap = Bitmap.createBitmap(graphView.getWidth(), graphView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            drawGraph(canvas);

            graphView.setBackground(new BitmapDrawable(getResources(), bitmap));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Integer>[] generateRandomGraph(int numNodes, int numEdges) {
        Random random = new Random();
        List<Integer>[] graph = new ArrayList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            graph[i] = new ArrayList<>();
        }

        nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            // Generate random position for the node within canvas bounds
            int nodeX = random.nextInt(graphView.getWidth());
            int nodeY = random.nextInt(graphView.getHeight());
            nodes.add(new Node(nodeX, nodeY));
        }

        Set<String> edgeSet = new HashSet<>();
        while (edgeSet.size() < numEdges) {
            int u = random.nextInt(numNodes);
            int v = random.nextInt(numNodes);
            if (u != v && !graph[u].contains(v)) {
                graph[u].add(v);
                graph[v].add(u);
                edgeSet.add(u + "," + v);
            }
        }

        return graph;
    }

    private int[] graphColoring(List<Integer>[] graph) {
        int[] colors = new int[graph.length];
        boolean[] availableColors = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) {
            colors[i] = -1;
            availableColors[i] = true;
        }

        colors[0] = 0;
        for (int u = 1; u < graph.length; u++) {
            for (int v : graph[u]) {
                if (colors[v] != -1) {
                    availableColors[colors[v]] = false;
                }
            }

            int cr;
            for (cr = 0; cr < graph.length; cr++) {
                if (availableColors[cr]) {
                    break;
                }
            }

            colors[u] = cr;

            for (int v : graph[u]) {
                if (colors[v] != -1) {
                    availableColors[colors[v]] = true;
                }
            }
        }

        return colors;
    }

    private void drawGraph(Canvas canvas) {
        drawEdges(canvas);
        drawNodes(canvas);
    }

    private void drawNodes(Canvas canvas) {
        Paint nodePaint = new Paint();
        nodePaint.setStyle(Paint.Style.FILL);
        Random random = new Random();
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        for (int i = 0; i < numNodes; i++) {
            int nodeX = nodes.get(i).x;
            int nodeY = nodes.get(i).y;
            nodePaint.setColor(getNodeColor(nodeColors[i]));
            canvas.drawCircle(nodeX, nodeY, 30, nodePaint);
        }
    }

    private int getNodeColor(int colorIndex) {
        int[] availableColors = {
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.DKGRAY, Color.BLACK,
                Color.LTGRAY, Color.WHITE
        };
        return availableColors[colorIndex % availableColors.length];
    }

    private void drawEdges(Canvas canvas) {
        Paint edgePaint = new Paint();
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5);
        for (int u = 0; u < numNodes; u++) {
            int startX = nodes.get(u).x;
            int startY = nodes.get(u).y;
            for (int v : graph[u]) {
                int endX = nodes.get(v).x;
                int endY = nodes.get(v).y;
                canvas.drawLine(startX, startY, endX, endY, edgePaint);
            }
        }
    }

    private static class Node {
        int x, y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
