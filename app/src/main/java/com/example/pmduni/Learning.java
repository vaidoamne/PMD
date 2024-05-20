package com.example.pmduni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Learning extends AppCompatActivity {

    private TextView titleTextView;
    private TextView contentTextView;
    private ListView menuListView;

    private List<String> chapters;
    private List<String> chapterContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        menuListView = findViewById(R.id.menuListView);

        // Read chapters and contents from XML file
        loadChaptersAndContentsFromXML(R.raw.chapter_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chapters);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showChapter(position);
            }
        });
    }

    public void GoHome3(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void showChapter(int position) {
        titleTextView.setText(chapters.get(position));
        contentTextView.setText(chapterContents.get(position));
    }

    private void loadChaptersAndContentsFromXML(int resourceId) {
        chapters = new ArrayList<>();
        chapterContents = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("chapter");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    String content = element.getElementsByTagName("content").item(0).getTextContent();
                    chapters.add(title);
                    chapterContents.add(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
