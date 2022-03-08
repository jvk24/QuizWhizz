package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class quiz extends AppCompatActivity implements transitionToOtherScreens{

    private AlertDialog buttonChoiceConfirmationAD;
    private Button beginButton;
    private TextView questionNumberTV;
    private TextView timerTV;
    private TextView scoreTV;
    private TextView questionTV;
    private TextView validityTextView;
    private GridLayout buttonGridLayout;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;
    private Button nextQuestionButton;
    private MediaPlayer nmp;
    private MediaPlayer pmp;

    private QuizHandler testQuestionSet;
    private String JSONMessage;
    private CountDownTimer countDownTimer;
    private AlertDialog ad;
    private Intent intent;
    private Toast toast;
    private Utils utils;

    private String type;
    private String username;

    private int score;
    private int correctOption;
    private int quizQuestionNo;

    private long timeLeftMillis; //Will be set by the teacher
    private boolean finishedBefore = false;

    protected static String[][] questionSetArr;
    protected static int quizMaxScore;

    @Override
    public void transitionToAnotherScreen(Class c, boolean forward) {
        intent = new Intent(quiz.this, c);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void setQuizElementsVisible() {

        buttonGridLayout.setVisibility(View.VISIBLE);
        questionNumberTV.setVisibility(View.VISIBLE);
        timerTV.setVisibility(View.VISIBLE);
        scoreTV.setVisibility(View.VISIBLE);
        questionTV.setVisibility(View.VISIBLE);
        option1Button.setVisibility(View.VISIBLE);
        option2Button.setVisibility(View.VISIBLE);
        option3Button.setVisibility(View.VISIBLE);
        option4Button.setVisibility(View.VISIBLE);

    }

    public void setQuestionSetOntoUI(String[][] questionSet) {
        enableButtons();
        questionNumberTV.setText(String.valueOf(quizQuestionNo+1));
        questionTV.setText(questionSet[0][1]);
        option1Button.setText(questionSet[0][2]);
        option2Button.setText(questionSet[0][3]);
        option3Button.setText(questionSet[0][4]);
        option4Button.setText(questionSet[0][5]);
        correctOption = Integer.valueOf(questionSet[0][6]);
    }

    public void disableButtons() {
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        option4Button.setEnabled(false);
    }

    public void enableButtons() {
        option1Button.setEnabled(true);
        option2Button.setEnabled(true);
        option3Button.setEnabled(true);
        option4Button.setEnabled(true);
    }

    public void nextQuestionButton(View view) {
        validityTextView.setVisibility(View.INVISIBLE);
        quizProcess(testQuestionSet);
    }

    public boolean initialiseConfirmationAlertDialog() {
        return false;
    }

    public void validateButtonClicked(Object buttonId) {
        //Log value id of the button to identify which button was clicked
        Log.i("BUTTON ID CLICKED >>>",String.valueOf(buttonId));
        disableButtons();
        if (String.valueOf(buttonId).equals(String.valueOf(correctOption-1))) {
            validityTextView.setTextColor(Color.GREEN);
            validityTextView.setText("Correct");
            score += 1;
            pmp.start();
        } else {
            validityTextView.setTextColor(Color.RED);
            validityTextView.setText("Incorrect");
            nmp.start();
        }
        validityTextView.setVisibility(View.VISIBLE);
        quizQuestionNo += 1;
        nextQuestionButton.setVisibility(View.VISIBLE);
        scoreTV.setText(String.valueOf(score));
    }

    public void validateButtons(final View view) {
        AlertDialog.Builder AD = new AlertDialog.Builder(quiz.this);
        AD.setMessage("Are you sure you want to choose this choice?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initialiseConfirmationAlertDialog();
                        Object buttonClickedID = view.getTag();
                        validateButtonClicked(buttonClickedID);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = AD.create();
        alert.setTitle("Choice confirmation");
        alert.show();
    }

    public void showQuizFinishedDialog() {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        double percentage = ((double) score / (double) quizQuestionNo) * 100;

        Log.i("SCORE: ",String.valueOf(score));
        Log.i("NOF QUESTIONS: ",String.valueOf(quizQuestionNo));
        Log.i("CALCULATION: ", String.valueOf((double) score / (double) quizMaxScore));

        android.app.AlertDialog.Builder builder = utils.getAlertDialog(this, "SUMMARY:" +
                "\n\nScore: "+score+"\nPercentage: "+df.format(percentage)+"%");
        builder.setPositiveButton(
                "FINISH",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        transitionToAnotherScreen(studentDashboard.class, false);
                    }
                });
        android.app.AlertDialog ad = builder.create();
        ad.show();
    }

    public void quizProcess(QuizHandler testQuestionSet /*String[][] questionSet*/) {
        boolean quizFinished = false;
        nextQuestionButton.setVisibility(View.INVISIBLE);
        if (quizQuestionNo < testQuestionSet.length()) {
            setQuestionSetOntoUI(testQuestionSet.getQueueAsArray());
            testQuestionSet.dequeue();
        } else {
            quizFinished = true;
            timeLeftMillis = 0;
            finishedBefore = true;
            utils.showShortToast(getApplicationContext(),"End of quiz!");

            countDownTimer.cancel();
            showQuizFinishedDialog();

            ServerConnection sc = new ServerConnection(this);
            String testid = getIntent().getStringExtra("testid");
            type = "record_score";
            sc.execute(type, username, testid, String.valueOf(score));
        }
        questionSetArr = testQuestionSet.getQueueAsArray();
    }

    public Toast setFinishMessage() {
        toast = Toast.makeText(this, "You have run out of time!", LENGTH_LONG);
        return toast;
    }

    public long calculateTime(String time) {
        String[] splitString = time.split(":");
        int minutes = Integer.parseInt(splitString[0]);
        int seconds = Integer.parseInt(splitString[1]);
        return ((minutes*60*1000)+(seconds*1000));
    }

    public void updateTimer() {
        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;

        String timerLeftFrmt = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTV.setText(timerLeftFrmt);
        if ((seconds <= 10) && (minutes == 0)) {
            timerTV.setTextColor(Color.RED);
        }
    }

    public void initiateTimerCountdown() throws JSONException {

        final Toast t = setFinishMessage();
        final ServerConnection sc = new ServerConnection(this);
        final ServerConnection sc2 = new ServerConnection(this);
        final String testid = getIntent().getStringExtra("testid");

        String type1 = "retrieve_duration";
        sc.execute(type1, testid);

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String obtainedTime = sc.result;
        Log.i("OBTAINED TIME: ",obtainedTime);

        timeLeftMillis = calculateTime(obtainedTime);

        timerTV.setTextColor(Color.BLACK);
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {

            @Override
            public void onTick(long l) {
                timeLeftMillis = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                if (!(finishedBefore == true)) {
                    t.show();

                    //START OF CHANGE
                    type = "record_score";
                    //ServerConnection sc2 = new ServerConnection(null);
                    sc2.execute(type, username, testid, String.valueOf(score));
                    //END OF CHANGE
                    //transitionToAnotherScreen(studentDashboard.class, false);
                    showQuizFinishedDialog();
                }
            }
        }.start();
    }

    public void onClickBegin(View v) {
        score = 0;
        beginButton.setVisibility(View.GONE);
        setQuizElementsVisible();

        try {
            initiateTimerCountdown();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String testid = getIntent().getStringExtra("testid");

        Log.i("EXTRACTED 2: ","EXTRACTED FROM INTENT: "+testid);

        type = "quiz";
        ServerConnection sc = new ServerConnection(this);
        sc.execute(type, username, testid); //Add parameters

        //WAITING FOR THREAD TO FINISH, TIMEOUT FOR 170mills
        try {
            TimeUnit.MILLISECONDS.sleep(310);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.i("Result=",sc.result);

        JSONMessage = sc.result;

        Toast t = Toast.makeText(this, JSONMessage, LENGTH_LONG);
        //t.show();

        testQuestionSet = new QuizHandler(JSONMessage).parseEnqueueJSONMessage(JSONMessage);
        //testQuestionSet.displayQueue();
        quizMaxScore = testQuestionSet.getQueueAsArray().length;
        Log.i("GET QUEUE AS ARRAY: ",String.valueOf(testQuestionSet.getQueueAsArray()));
        //quizProcess(testQuestionSet.getQueueAsArray()); <<WORKING
        quizProcess(testQuestionSet);
        //retrieves the quizhandler queue and will call other methods to
        // dequeue the sub-arrays of question sets onto the view framework
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        nmp = MediaPlayer.create(this, R.raw.negative_sound);
        pmp = MediaPlayer.create(this, R.raw.positive_sound);

        SharedPreferences shared = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        username = (shared.getString("username",""));

        Log.i("EXTRACTED 1: ","EXTRACTED FROM PREFERENCES: "+username);

        ad = new AlertDialog.Builder(this).create();

        beginButton = findViewById(R.id.beginButton);

        questionNumberTV = findViewById(R.id.questionNumberTV);
        timerTV = findViewById(R.id.timerTV);
        scoreTV = findViewById(R.id.scoreTV);
        questionTV = findViewById(R.id.questionTV);
        buttonGridLayout = findViewById(R.id.optionButtonsGridLayout);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        validityTextView = findViewById(R.id.validityTextView);

        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        quizQuestionNo = 0;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
