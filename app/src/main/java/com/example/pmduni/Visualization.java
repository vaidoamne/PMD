package com.example.pmduni;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Visualization extends AppCompatActivity {

    private EditText etNodes, etEdges;
    private Spinner spinnerAlgorithms;
    private Button btnDraw;
    private View graphView;
    private TextView algorithmStepsTextView;
    private int numNodes, numEdges;
    private List<Integer>[] graph;
    private List<Node> nodes;
    private int[] nodeColors;
    private String selectedAlgorithm;
    private int executionSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

        etNodes = findViewById(R.id.et_nodes);
        etEdges = findViewById(R.id.et_edges);
        spinnerAlgorithms = findViewById(R.id.spinner_algorithms);
        btnDraw = findViewById(R.id.btn_draw);
        graphView = findViewById(R.id.graphView);
        algorithmStepsTextView = findViewById(R.id.algorithm_steps);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algorithm_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlgorithms.setAdapter(adapter);

        spinnerAlgorithms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAlgorithm = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAlgorithm = null;
            }
        });

        btnDraw.setOnClickListener(v -> {
            if (validateInputs()) {
                generateGraph();
                applySelectedAlgorithm();
                drawGraph();
            }
        });
    }

    private boolean validateInputs() {
        String nodesInput = etNodes.getText().toString().trim();
        String edgesInput = etEdges.getText().toString().trim();

        if (nodesInput.isEmpty() || edgesInput.isEmpty()) {
            Toast.makeText(this, "Please enter both nodes and edges", Toast.LENGTH_SHORT).show();
            return false;
        }

        numNodes = Integer.parseInt(nodesInput);
        numEdges = Integer.parseInt(edgesInput);

        if (numNodes <= 0 || numEdges <= 0) {
            Toast.makeText(this, "Nodes and edges must be positive integers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void generateGraph() {
        graph = new ArrayList[numNodes];
        nodes = new ArrayList<>();
        nodeColors = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph[i] = new ArrayList<>();
            nodes.add(new Node(i, new Random().nextInt(graphView.getWidth()), new Random().nextInt(graphView.getHeight())));
        }

        Random random = new Random();
        Set<String> edgesSet = new HashSet<>();

        while (edgesSet.size() < numEdges) {
            int u = random.nextInt(numNodes);
            int v = random.nextInt(numNodes);
            if (u != v && !edgesSet.contains(u + "-" + v) && !edgesSet.contains(v + "-" + u)) {
                graph[u].add(v);
                graph[v].add(u);
                edgesSet.add(u + "-" + v);
            }
        }
    }

    private void applySelectedAlgorithm() {
        if (selectedAlgorithm == null) {
            Toast.makeText(this, "Please select an algorithm", Toast.LENGTH_SHORT).show();
            return;
        }

        executionSteps = 0;

        switch (selectedAlgorithm) {
            case "Greedy Coloring":
                applyGreedyColoring();
                break;
            case "Backtracking Coloring":
                applyBacktrackingColoring();
                break;
            case "Sequential Coloring":
                applySequentialColoring();
                break;
        }

        algorithmStepsTextView.setText(executionSteps + " steps");
    }

    private void applyGreedyColoring() {
        boolean[] available = new boolean[numNodes];
        nodeColors[0] = 0;
        executionSteps++;

        for (int u = 1; u < numNodes; u++) {
            for (int neighbor : graph[u]) {
                if (nodeColors[neighbor] != -1) {
                    available[nodeColors[neighbor]] = true;
                    executionSteps++;
                }
            }

            int color;
            for (color = 0; color < numNodes; color++) {
                if (!available[color]) {
                    break;
                }
                executionSteps++;
            }

            nodeColors[u] = color;
            executionSteps++;

            // Reset available colors
            for (int neighbor : graph[u]) {
                if (nodeColors[neighbor] != -1) {
                    available[nodeColors[neighbor]] = false;
                    executionSteps++;
                }
            }
        }
    }

    private void applyBacktrackingColoring() {
        boolean[] available = new boolean[numNodes];
        nodeColors = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodeColors[i] = -1;
        }
        executionSteps++;

        if (backtrackColoring(0, available)) {
            executionSteps++;
        } else {
            Toast.makeText(this, "Graph cannot be colored", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean backtrackColoring(int nodeIndex, boolean[] availableColors) {
        if (nodeIndex == numNodes) {
            return true;
        }

        for (int color = 0; color < numNodes; color++) {
            if (isSafeColor(nodeIndex, color, availableColors)) {
                nodeColors[nodeIndex] = color;
                availableColors[color] = true;
                executionSteps++;

                if (backtrackColoring(nodeIndex + 1, availableColors)) {
                    return true;
                }

                nodeColors[nodeIndex] = -1;
                availableColors[color] = false;
                executionSteps++;
            }
        }

        return false;
    }

    private boolean isSafeColor(int nodeIndex, int color, boolean[] availableColors) {
        for (int neighbor : graph[nodeIndex]) {
            if (nodeColors[neighbor] == color) {
                return false;
            }
            executionSteps++;
        }
        return true;
    }

    private void applySequentialColoring() {
        boolean[] availableColors = new boolean[numNodes];
        nodeColors = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
            nodeColors[i] = -1;
        }
        executionSteps++;

        for (int node = 0; node < numNodes; node++) {
            for (int neighbor : graph[node]) {
                if (nodeColors[neighbor] != -1) {
                    availableColors[nodeColors[neighbor]] = true;
                    executionSteps++;
                }
            }

            int color;
            for (color = 0; color < numNodes; color++) {
                if (!availableColors[color]) {
                    break;
                }
                executionSteps++;
            }

            nodeColors[node] = color;
            executionSteps++;

            // Reset available colors
            for (int neighbor : graph[node]) {
                if (nodeColors[neighbor] != -1) {
                    availableColors[nodeColors[neighbor]] = false;
                    executionSteps++;
                }
            }
        }
    }

    private void drawGraph() {
        Bitmap bitmap = Bitmap.createBitmap(graphView.getWidth(), graphView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        drawEdges(canvas);
        drawNodes(canvas);

        graphView.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void drawNodes(Canvas canvas) {
        Paint nodePaint = new Paint();
        nodePaint.setStyle(Paint.Style.FILL);
        Random random = new Random();
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        for (int i = 0; i < numNodes; i++) {
            int nodeX = nodes.get(i).getX();
            int nodeY = nodes.get(i).getY();
            int color = nodeColors[i];

            if (color == -1) {
                nodePaint.setColor(Color.WHITE);
            } else {
                nodePaint.setColor(generateRandomColor(random));
            }
            canvas.drawCircle(nodeX, nodeY, 20, nodePaint);
            executionSteps++;
        }
    }

    private int generateRandomColor(Random random) {
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void drawEdges(Canvas canvas) {
        Paint edgePaint = new Paint();
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5);

        for (int i = 0; i < numNodes; i++) {
            int startX = nodes.get(i).getX();
            int startY = nodes.get(i).getY();
            for (int j : graph[i]) {
                int endX = nodes.get(j).getX();
                int endY = nodes.get(j).getY();
                canvas.drawLine(startX, startY, endX, endY, edgePaint);
                executionSteps++;
            }
        }
    }

    // Node class representing a node in the graph
    class Node {
        int id;
        int x;
        int y;

        Node(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
