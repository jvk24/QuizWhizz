package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class highscores extends AppCompatActivity implements transitionToOtherScreens{

    private ListView scoresListView;
    private TextView scoresStatusTV;
    private String JSONMessage;
    private JSONArray jsonArray;
    private TableLayout tl;

    @Override
    public void transitionToAnotherScreen(Class c, boolean forward) {
        Intent intent = new Intent(highscores.this, c);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickReturnButton(View v) {
        this.finish();
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

    public void applyTableLayout(JSONArray productsList) {

        //make sure that the lists contain data or else display will be blank screen
        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableLayout tbl=(TableLayout) findViewById(R.id.tableLayout);

        for (int ctr = 0; ctr < productsList.length(); ctr++) {

            //Creating new tablerows and textviews
            TableRow row = new TableRow(this);
            TextView txt1 = new TextView(this);
            TextView txt2 = new TextView(this);
            TextView txt3 = new TextView(this);
            TextView txt4 = new TextView(this);

            //setting the text
            try {
                txt1.setText(productsList.getJSONObject(ctr).getString("quizname"));
                txt2.setText(productsList.getJSONObject(ctr).getString("student_name"));
                txt3.setText(productsList.getJSONObject(ctr).getString("score"));
                txt4.setText(productsList.getJSONObject(ctr).getString("rank"));

            } catch (JSONException j) {
                j.printStackTrace();
            }

            txt1.setLayoutParams(params1);
            txt2.setLayoutParams(params1);
            txt3.setLayoutParams(params1);
            txt4.setLayoutParams(params1);

            //the textviews have to be added to the row created
            row.addView(txt1);
            row.addView(txt2);
            row.addView(txt3);
            row.addView(txt4);
            row.setLayoutParams(params2);
            tbl.addView(row);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //scoresStatusTV = (TextView) findViewById(R.id.scoresStatusTV);

        String type = "retrieve_score";
        ServerConnection sc = new ServerConnection(this);
        sc.execute(type);

        //WAITING FOR THREAD TO FINISH, TIMEOUT FOR 200mills

        try {
            TimeUnit.MILLISECONDS.sleep(170);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.i("Result=",sc.result);

        JSONMessage = sc.result;

        Toast t = Toast.makeText(this, JSONMessage, Toast.LENGTH_LONG);
        //t.show();
        Log.i("JSON MESSAGE: ",JSONMessage);
        JSONArray jsonArray = convertToJSONArray(JSONMessage);

        //applyColumnHeadings(tr_head, tl);
        applyTableLayout(jsonArray);
    }
}
