package com.example.pmduni;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pmduni.R;

public class Quizz extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private Button submitButton;
    private TextView answerTextView;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitButton);
        answerTextView = findViewById(R.id.answerTextView);
        image = findViewById(R.id.imageView3);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void checkAnswer() {
        String userAnswer = answerEditText.getText().toString().trim();
        if (userAnswer.equals("6")) {
            answerTextView.setText("Correct!");
        } else {
            answerTextView.setText("Incorrect! Try again.");
        }

        answerTextView.setVisibility(View.VISIBLE);
        image.setVisibility(View.INVISIBLE);
        questionTextView.setText("How many colours are usually used(3)");
        if (userAnswer.equals("3")) {
            answerTextView.setText("Correct!");
        } else {
            answerTextView.setText("Incorrect! Try again.");
        }

    }
    public void GoHome3(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}