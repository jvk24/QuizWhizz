package com.example.quizwhizzprototype;

import android.content.Context;
import android.widget.Toast;

public class AlertMessages {

    String message;
    Context c;

    //Constructor method setting the string message value
    public AlertMessages(Context context) {
        this.c = context;
    }

    public void createDisplayShortToast() {
        Toast toast = Toast.makeText(this.c, this.message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void createDisplayLongToast() {
        Toast toast = Toast.makeText(this.c, this.message, Toast.LENGTH_LONG);
        toast.show();
    }
}
