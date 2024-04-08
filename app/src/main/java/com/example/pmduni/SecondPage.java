package com.example.pmduni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void GoHome(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
    public void GoLearning(View v) {
        startActivity(new Intent(this, Learning.class));
    }
    public void GoQuizz(View v) {
        startActivity(new Intent(this, Quizz.class));
    }
    public void GoVisualization(View v) {
        startActivity(new Intent(this, Visualization.class));
    }
}