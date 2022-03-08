package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class teacherDashboard extends AppCompatActivity implements transitionToOtherScreens {

    private Intent intent;

    @Override
    public void transitionToAnotherScreen(Class cont, boolean forward) {
        Intent intent = new Intent(teacherDashboard.this, cont);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickCQB(View v) {
        transitionToAnotherScreen(createQuiz.class, true);
    }

    public void onClickHSB(View v){
        transitionToAnotherScreen(highscores.class, true);
    }

    public void onClickRB(View v){
        transitionToAnotherScreen(review.class, true);
    }

    public void onClickLogout(View v) {
        transitionToAnotherScreen(MainActivity.class, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
