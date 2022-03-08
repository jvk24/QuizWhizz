package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

public class studentLogin extends AppCompatActivity implements transitionToOtherScreens {

    //Constants - UI element initialisation
    private EditText usernameET;
    private EditText passwordET;
    private Button studentLoginButton;
    private String type;
    private Utils utils;

    @Override
    public void transitionToAnotherScreen(Class cont, boolean forward) {
        Intent intent = new Intent(studentLogin.this, cont);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void checkToChangeScreen(String result) {
        if (result.equals("Successful login!")) {
            transitionToAnotherScreen(studentDashboard.class, true);
        }
    }

    public void onClickLogin(View v) {
        utils = new Utils();

        String usernameString = usernameET.getText().toString();
        String passwordString = passwordET.getText().toString();

        type = "login";

        ServerConnection sc = new ServerConnection(this);
        try {
            sc.execute(type, usernameString, passwordString);
            //sc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type, usernameString, passwordString);

            //Check whether the student has logged in successfully:
            TimeUnit.MILLISECONDS.sleep(170);

            Log.i("Here-1>>>>>>>--------->",sc.result);
            String result = sc.result;

            checkToChangeScreen(result);

        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO toast interrupted exception from Utils class instance
            utils.showShortToast(getApplicationContext(), "Time Interrupt error caused...");
        } catch (RuntimeException npe) {
            npe.printStackTrace();
            //TODO toast to runtime exception from Utils class instance
            utils.showShortToast(getApplicationContext(), "Unable to connect to server...");
        }
    }

    public void onClickHomeImage(View v) {
        transitionToAnotherScreen(MainActivity.class, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.password1ET);
        studentLoginButton = (Button) findViewById(R.id.studentLoginButton);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
