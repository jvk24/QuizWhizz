package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class studentDashboard extends AppCompatActivity implements transitionToOtherScreens{

    private Intent intent;

    public void transitionToAnotherScreen(Class cont, boolean forward) {
        Intent intent = new Intent(studentDashboard.this, cont);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickQB(View v) {
        //intent = new Intent(studentDashboard.this, quiz.class);
        //startActivity(intent);
        transitionToAnotherScreen(QuizList.class, true);
    }

    public void onClickHSB(View v) {
        //NOT PART OF PROTOTYPE
        transitionToAnotherScreen(highscores.class, true);
    }

    public void onClickLogout(View v) {
        transitionToAnotherScreen(MainActivity.class, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
