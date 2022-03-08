package com.example.quizwhizzprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Utils {

    public static void showShortToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog.Builder getAlertDialog(Context c, String body) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(body);
        builder.setCancelable(false);
        return builder;
    }
}