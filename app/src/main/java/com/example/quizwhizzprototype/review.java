package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class review extends AppCompatActivity implements transitionToOtherScreens {

    private String JSONMessage;

    private String[] meansArray;
    private String[] stdevsArray;

    @Override
    public void transitionToAnotherScreen(Class c, boolean forward) {
        Intent intent = new Intent(review.this, c);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickReturnButton(View v) {
        transitionToAnotherScreen(teacherDashboard.class, false);
    }

    public JSONArray convertToJSONArray(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("row_data");
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
        } return null;
    }

    public void generareMeansList(JSONArray productsList) {
        for (int i = 0; i < productsList.length(); i++) {
            try {
                meansArray[i] = productsList.getJSONObject(i).getString("avg_score");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } Log.i("MEANS ARRAY",Arrays.toString(meansArray));
    }

    public void generateStdevsList(JSONArray productsList) {
        for (int i = 0; i < productsList.length(); i++) {
            try {
                stdevsArray[i] = productsList.getJSONObject(i).getString("stdev_score");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } Log.i("STDEVS ARRAY", Arrays.toString(stdevsArray));
    }

    public String[] generateIQRList(int length) {
        String[] IQRList = new String[length];
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        for (int i = 0; i < length; i++) {
            double mean = Double.parseDouble(meansArray[i]);
            double stdev =  Double.parseDouble(stdevsArray[i]);
            try {
                CNDistribution CND = new CNDistribution(mean, stdev);
                IQRList[i] = String.valueOf(df.format(CND.calculateIQR()));
            } catch (NotStrictlyPositiveException e) {
                IQRList[i] = "N/A";
            }
        }
        return IQRList;
    }

    public void applyTableLayout(JSONArray productsList, String[] IQRList) {

        //make sure that the lists contain data or else display will be blank screen
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);
        for(int ctr = 0; ctr < productsList.length(); ctr ++) {

            //Creating new tablerows and textviews objects via instantiation
            TableRow row = new TableRow(this);
            TextView txt1 = new TextView(this);
            TextView txt2 = new TextView(this);
            TextView txt3 = new TextView(this);
            TextView txt4 = new TextView(this);
            TextView txt5 = new TextView(this);
            TextView txt6 = new TextView(this);
            TextView txt7 = new TextView(this);
            TextView txt8 = new TextView(this);
            TextView txt9 = new TextView(this);
            TextView txt10 = new TextView(this);

            //setting the text
            try {
                txt1.setText(productsList.getJSONObject(ctr).getString("quizname"));
                txt2.setText(productsList.getJSONObject(ctr).getString("nof_students"));
                txt3.setText(productsList.getJSONObject(ctr).getString("min_score"));
                txt4.setText(productsList.getJSONObject(ctr).getString("max_score"));
                txt5.setText(productsList.getJSONObject(ctr).getString("avg_score"));
                txt6.setText(productsList.getJSONObject(ctr).getString("stdev_score"));
                txt7.setText(productsList.getJSONObject(ctr).getString("range_score"));
                txt8.setText(productsList.getJSONObject(ctr).getString("var_score"));
                txt9.setText(IQRList[ctr]);
                txt10.setText(productsList.getJSONObject(ctr).getString("max_possible_score"));

            } catch (JSONException j) {
                j.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

            txt1.setLayoutParams(params1);
            txt2.setLayoutParams(params1);
            txt3.setLayoutParams(params1);
            txt4.setLayoutParams(params1);
            txt5.setLayoutParams(params1);
            txt6.setLayoutParams(params1);
            txt7.setLayoutParams(params1);
            txt8.setLayoutParams(params1);
            txt9.setLayoutParams(params1);
            txt10.setLayoutParams(params1);

            //the textviews have to be added to the row created

            row.addView(txt1);
            row.addView(txt2);
            row.addView(txt3);
            row.addView(txt4);
            row.addView(txt5);
            row.addView(txt6);
            row.addView(txt7);
            row.addView(txt8);
            row.addView(txt9);
            row.addView(txt10);
            row.setLayoutParams(params2);
            tbl.addView(row);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ServerConnection sc = new ServerConnection(this);
        String type = "retrieve_stats";
        sc.execute(type);

        try {
            TimeUnit.MILLISECONDS.sleep(450);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JSONMessage = sc.result;
        Log.i("JSON MESSAGE", JSONMessage);
        Toast t = Toast.makeText(this, JSONMessage, Toast.LENGTH_LONG);
        //t.show();

        JSONArray jsonArray = convertToJSONArray(JSONMessage);

        meansArray = new String[jsonArray.length()];
        stdevsArray = new String[jsonArray.length()];

        generareMeansList(jsonArray);
        generateStdevsList(jsonArray);
        String[] IQRList = generateIQRList(jsonArray.length());

        applyTableLayout(jsonArray, IQRList);

    }
}
