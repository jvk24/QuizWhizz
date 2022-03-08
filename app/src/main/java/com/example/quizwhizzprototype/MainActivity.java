package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

interface transitionToOtherScreens {
    void transitionToAnotherScreen(Class c, boolean forward);
}

public class MainActivity extends AppCompatActivity {

    private Button studentLoginButton;
    private Button teacherLoginButton;
    private Button createAccountButton;

    public Intent intent;

    public void transitionToAnotherScreen(Class c) {
        Intent intent = new Intent(MainActivity.this, c);
        startActivity(intent);
    }

    public void onClickSLB(View v) {
        transitionToAnotherScreen(studentLogin.class);
    }

    public void onClickTLB(View v) {
        transitionToAnotherScreen(teacherLogin.class);
    }

    public void onClickCAB(View v) {
        transitionToAnotherScreen(createAccount.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentLoginButton = (Button) findViewById(R.id.studentLoginButton);
        teacherLoginButton = (Button) findViewById(R.id.teacherLoginButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}
