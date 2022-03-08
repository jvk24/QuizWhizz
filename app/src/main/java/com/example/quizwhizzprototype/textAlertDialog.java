package com.example.quizwhizzprototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatDialogFragment;

public class textAlertDialog extends AppCompatDialogFragment {

    private EditText editTextQuizName;
    private EditText editTextDuration;
    private EditText editTextDueDate;
    private Toast toast;
    private Matcher matcher;
    private Pattern pattern;

    private quizDialogListener qdListener;
    private AlertMessages alert;

    public Pattern regexQuizname() {
        Pattern pattern = Pattern.compile("^([?! ]*[a-zA-Z0-9]{1,25})+$");
        return pattern;
    }

    public Pattern regexTime() {
        //Quix time regex pattern compilation; will only allow for time durations to be longer than 00:20
        Pattern pattern = Pattern.compile("^((00):([2-5][0-9]))|(([0-9][1-9]):([0-5][0-9]))|(([1-9][0-9]):([0-5][0-9]))$");
        return pattern;
    }

    public Pattern regexDate() {
        Pattern pattern = Pattern.compile("^(([0-9]){4})-(0[1-9]|1[0-2])-(([0-2][0-9])|3(0|1))$");
        return pattern;
    }

    public boolean dateIsAfterCurrentDate(String inputtedDateString) {
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
            Date inputtedDate = sdf.parse(inputtedDateString);

            if (inputtedDate.compareTo(currentDate) <= 0) {
                return false;
            } else if (inputtedDate.compareTo(currentDate) > 0) {
                return true;
            }
            Log.i("CURRENT DATE: ", currentDate.toString());
            Log.i("INPUT DATE: ", inputtedDateString);

        } catch (ParseException pe) {
            Log.i("ERROR:: ", "ERROR WHILE PARSING DATES");
        }
        return false;
    }

    public boolean validateAlertDialog(EditText quizName, EditText duration, EditText duedate) {

        Pattern timePattern = regexTime();
        Pattern datePattern = regexDate();
        Pattern quizNamePattern = regexQuizname();

        //DEBUGGING TO LOGS
        Log.i("QUIZ NAME :::::::", quizName.getText().toString());
        Log.i("DURATION :::::::", duration.getText().toString());
        Log.i("DATE DUE::::::::", duedate.getText().toString());

        Matcher timeMatch = timePattern.matcher(duration.getText().toString());
        Matcher dateMatch = datePattern.matcher(duedate.getText().toString());
        Matcher quizNameMatch = quizNamePattern.matcher(quizName.getText().toString());

        //EDITED
        if (timeMatch.matches() && dateMatch.matches()
                && dateIsAfterCurrentDate(duedate.getText().toString())
                && quizNameMatch.matches()
                && duration.getText().toString().length() == 5) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Utils utils = new Utils();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.quizname_dialog, null);

        builder.setView(view)
                .setTitle("Quiz details")
                .setCancelable(false)
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String quizName = editTextQuizName.getText().toString();
                        String quizDuration = editTextDuration.getText().toString();
                        String quizDueDate = editTextDueDate.getText().toString();
                        boolean valid = validateAlertDialog(editTextQuizName, editTextDuration, editTextDueDate);
                        Log.i("IS INPUT VALID?: ",String.valueOf(valid));
                        if (valid) {
                            String type = "test_details";
                            ServerConnection sc = new ServerConnection(null);
                            sc.execute(type, quizName, quizDueDate, quizDuration);
                        } else {
                            dialogInterface.cancel();
                            utils.showShortToast(getContext(),"Please enter valid details in the text fields");
                            textAlertDialog tad = new textAlertDialog();
                            tad.show(getFragmentManager(),"quiz dialog");
                        }
                        //qdListener.retrieveAlertDialogText(quizName, quizDuration, quizDueDate);
                    }
                });

        editTextQuizName = view.findViewById(R.id.editTextQuizName);
        editTextDuration = view.findViewById(R.id.editTextQuizDuration);
        editTextDueDate = view.findViewById(R.id.editTextQuizDueDate);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            qdListener = (quizDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface quizDialogListener {
        void retrieveAlertDialogText(String quizName, String quizDuration, String quizDueDate);
    }
}
