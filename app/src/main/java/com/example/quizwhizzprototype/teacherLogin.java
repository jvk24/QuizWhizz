package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class teacherLogin extends AppCompatActivity implements transitionToOtherScreens {

    //Constants - UI element initialisation
    EditText usernameET;
    EditText passwordET;
    Button teacherLoginButton;

    private String usernameActual;
    private String passwordActual;

    Toast toast;

    public void transitionToAnotherScreen(Class cont, boolean forward) {
        Intent intent = new Intent(teacherLogin.this, cont);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public boolean validateDetails(String u, String p) {
        if ((u.equals(usernameActual)) & (p.equals(passwordActual))) {
            return true;
        } return false;
    }

    public void onClickLogin(View v){
        String usernameString = usernameET.getText().toString();
        String passwordString = passwordET.getText().toString();

        Intent intent = new Intent(teacherLogin.this, teacherDashboard.class);
        boolean validated = validateDetails(usernameString, passwordString);
        if (validated) {
            toast = Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT);
            startActivity(intent);
        } else {
            toast = Toast.makeText(this, "Invalid details", Toast.LENGTH_SHORT);
        } toast.show();
    }

    public void onClickHomeImage(View v) {
        transitionToAnotherScreen(MainActivity.class, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        usernameActual = "MrPearson";
        passwordActual = "teacher1234";

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        teacherLoginButton = (Button) findViewById((R.id.teacherLoginButton));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
