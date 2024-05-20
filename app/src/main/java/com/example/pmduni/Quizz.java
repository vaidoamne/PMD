package com.example.pmduni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Quizz extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private Button submitButton;
    private TextView answerTextView;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private String[] questions = {
            "What is the purpose of graph coloring?",
            "Which algorithm is used to assign colors to vertices in a graph?",
            "What is the maximum number of colors needed to color a planar graph?",
            "What is the concept used in backtracking graph coloring?",
            "Which algorithm provides an approximate solution to graph coloring?"
    };
    private String[] answers = {
            "To assign colors to vertices in a graph such that no two adjacent vertices have the same color.",
            "Graph coloring algorithm",
            "Four (Four Color Theorem)",
            "Backtracking",
            "Greedy Coloring"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitButton);
        answerTextView = findViewById(R.id.answerTextView);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionTextView.setText(questions[currentQuestionIndex]);
            answerEditText.setText("");
            answerTextView.setVisibility(View.INVISIBLE);
        } else {
            showResult();
        }
    }

    private void checkAnswer() {
        String userAnswer = answerEditText.getText().toString().trim();
        if (userAnswer.equalsIgnoreCase(answers[currentQuestionIndex])) {
            correctAnswers++;
        }

        currentQuestionIndex++;
        displayQuestion();
    }

    private void showResult() {
        questionTextView.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        answerTextView.setVisibility(View.VISIBLE);
        answerTextView.setText("You got " + correctAnswers + " out of " + questions.length + " questions correct.");
        Button homeButton = findViewById(R.id.button5);
        homeButton.setVisibility(View.VISIBLE);
    }

    public void GoHome3(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
