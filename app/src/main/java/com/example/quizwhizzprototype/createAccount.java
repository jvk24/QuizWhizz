package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static android.widget.Toast.LENGTH_LONG;

public class createAccount extends AppCompatActivity implements transitionToOtherScreens {

    //UI element global declaration
    private Button registerButton;
    private EditText nameET;
    private EditText usernameET;
    private EditText password1ET;
    private EditText password2ET;
    private ImageView homeImageButton;
    private Utils utils;

    @Override
    public void transitionToAnotherScreen(Class cont, boolean forward) {
        Intent intent = new Intent(createAccount.this, cont);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public Pattern getRegexUsername() {
        Pattern pattern = Pattern.compile("^([a-zA-Z_0-9]{6,20})$");
        return pattern;
    }

    public Pattern getRegexName() {
        Pattern pattern = Pattern.compile("^([a-zA-Z]{1,20})$");
        return pattern;
    }

    public Pattern getRegexPassword() {
        Pattern pattern = Pattern.compile("^((?=.*\\d)[a-zA-Z\\d]{6,20})$");
        return pattern;
    }

    public boolean validatePassword(String p1, String p2) {
        if (p1.equals(p2)) {

            Pattern patternPassword = getRegexPassword();
            Matcher passwordMatch = patternPassword.matcher(p1);

            if (passwordMatch.matches()) return true;

        } return false;
    }

    public boolean validateUsernameName(String un, String n) {
        Pattern patternUsername = getRegexUsername();
        Pattern patternName = getRegexName();

        Matcher usernameMatch = patternUsername.matcher(un);
        Matcher nameMatch = patternName.matcher(n);

        if (usernameMatch.matches() && nameMatch.matches()) {
            return true;
        } return false;
    }

    public void onClickHomeImage(View v) {
        transitionToAnotherScreen(MainActivity.class, false);
    }

    public void onClickRegister(View v) {

        String nameString = nameET.getText().toString();
        String usernameString = usernameET.getText().toString();
        String password1String = password1ET.getText().toString();
        String password2String = password2ET.getText().toString();

        boolean validPass = validatePassword(password1String, password2String);
        boolean validUsernameName = validateUsernameName(usernameString, nameString);

        if (validPass && validUsernameName) {
            String type = "create_account";

            ServerConnection sc = new ServerConnection(this);

            sc.execute(type, usernameString, password1String, nameString);
        } else {
            utils.showShortToast(getApplicationContext(),"Invalid details, see instructions for the format");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameET = findViewById(R.id.nameET);
        usernameET = findViewById(R.id.usernameET);
        password1ET = findViewById(R.id.password1ET);
        password2ET = findViewById(R.id.password2ET);
        registerButton = findViewById(R.id.registerButton);
        homeImageButton = findViewById(R.id.imageViewHome);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}
