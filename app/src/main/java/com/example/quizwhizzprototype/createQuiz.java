package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createQuiz extends AppCompatActivity implements textAlertDialog.quizDialogListener, transitionToOtherScreens {

    //Initialise the UI elements to be used
    private Button finishButton;
    private Button addQuestion;
    private EditText questionET;
    private EditText option1ET;
    private EditText option2ET;
    private EditText option3ET;
    private EditText option4ET;
    private EditText correctOptionET;
    private Intent intent;
    private String processType;

    private String quizName;
    private String quizDuration;
    private String quizDueDate;

    private Utils utils;

    @Override
    public void transitionToAnotherScreen(Class c, boolean forward) {
        Intent intent = new Intent(createQuiz.this, c);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickFinish(View v) {
        utils.showShortToast(getApplicationContext(),"Uploading Quiz...");
        transitionToAnotherScreen(teacherDashboard.class, false);
    }

    public Pattern getRegexQuestion(){
        Pattern pattern  = Pattern.compile("^([?! ]*[a-zA-Z0-9?,.+=*\\-']{1,100})+$"); //ALTERED <--
        return pattern;
    }

    public Pattern getRegexOption(){
        Pattern pattern  = Pattern.compile("^([?! ]*[a-zA-Z0-9?,.=/+'\\-]{1,35})+$"); //ALTERED <--
        return pattern;
    }

    private boolean validateInputs(String question, String o1, String o2, String o3, String o4, String co) {

        //check if the elements in the array are unique, by using a hash set comparison in length to the original array
        //since a hash set can only contain unique elements (as with any set)
        //The expression is evaluated as a boolean expression
        String[] optionsArr = {o1, o2, o3, o4};
        Set<String> optionsSet = new HashSet<String>(Arrays.asList(optionsArr));

        //will contain true if they are of equal length, false otherwise
        boolean uniqueOptions = optionsSet.size() == optionsArr.length;

        //will store true if correct option is between 1 and 4, false otherwise
        boolean validCorrectOptionNumber;
        try {
            validCorrectOptionNumber = (Integer.parseInt(co) >= 1) && (Integer.parseInt(co) <= 4);
        } catch (java.lang.NumberFormatException nfe) {
            validCorrectOptionNumber = false;
        }

        //will now apply the regular expressions to make sure that the input strings match the format
        Pattern patternOption = getRegexOption();
        Pattern patternQuestion = getRegexQuestion();
        boolean questionMatch = patternQuestion.matcher(question).matches();
        boolean allOptionsMatch = true;

        //boolean array created of all the matches based on the regex string
        boolean[] optionsMatches = {
                patternOption.matcher(o1).matches(),
                patternOption.matcher(o2).matches(),
                patternOption.matcher(o3).matches(),
                patternOption.matcher(o4).matches()
        };

        //boolean array is then iterated through to check if there are any false values
        for (boolean bool : optionsMatches) if (!bool) {allOptionsMatch = false; break;}

        //compound boolean expression shortened here in this expression
        boolean regexBothMatch = questionMatch && allOptionsMatch;

        //Function returns the logical AND operation between the above booleans, such that it will only be true
        //if both the validations return true
        return (uniqueOptions && validCorrectOptionNumber && regexBothMatch);
    }

    public void onClickAddQuestion(View v) {
        String questionString = questionET.getText().toString();
        String o1String = option1ET.getText().toString();
        String o2String = option2ET.getText().toString();
        String o3String = option3ET.getText().toString();
        String o4String = option4ET.getText().toString();
        String correctString = correctOptionET.getText().toString();
        processType = "create_quiz";

        boolean valid = validateInputs(questionString, o1String, o2String, o3String, o4String, correctString);
        if (!valid) {
            utils.showLongToast(getApplicationContext(),"Each option must be filled out, unique, and the correct" +
                    " option must be an integer between 1 and 4 inclusive");
        } else {
            finishButton.setVisibility(View.VISIBLE);
            ServerConnection sc = new ServerConnection(this);
            sc.execute(processType, questionString, o1String, o2String, o3String, o4String, correctString);

            questionET.getText().clear();
            option1ET.getText().clear();
            option2ET.getText().clear();
            option3ET.getText().clear();
            option4ET.getText().clear();
            correctOptionET.getText().clear();
        }
    }

    public void initiateDialog() {
        textAlertDialog textAlertDialog = new textAlertDialog();
        textAlertDialog.show(getSupportFragmentManager(), "quiz dialog");
    }

    @Override
    public void retrieveAlertDialogText(String quizNameRetrieved, String quizDurationRetrieved, String quizDueDateRetrieved) {
        quizName = quizNameRetrieved;
        quizDuration = quizDurationRetrieved;
        quizDueDate = quizDueDateRetrieved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        initiateDialog();

        finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setVisibility(View.INVISIBLE);

        addQuestion = (Button) findViewById(R.id.addQuestionButton);
        questionET = (EditText) findViewById(R.id.questionET);
        option1ET = (EditText) findViewById(R.id.option1ET);
        option2ET = (EditText) findViewById(R.id.option2ET);
        option3ET = (EditText) findViewById(R.id.option3ET);
        option4ET = (EditText) findViewById(R.id.option4ET);
        correctOptionET = (EditText) findViewById(R.id.correctOptionET);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
