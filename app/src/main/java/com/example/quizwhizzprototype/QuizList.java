package com.example.quizwhizzprototype;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class QuizList extends AppCompatActivity implements transitionToOtherScreens {

    private String JSONMessage;
    private ListView listView;
    private TextView quizStatusTV;

    @Override
    public void transitionToAnotherScreen(Class c, boolean forward) {
        Intent intent = new Intent(QuizList.this, c);
        startActivity(intent);
        if (!forward) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void onClickReturnButton(View v) {
        transitionToAnotherScreen(studentDashboard.class, false);
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

    public int getTestId(String[] items, int i) {
        String test = items[i];
        String parts[] = test.split(" ");
        int testid = Integer.parseInt(parts[0]);
        return testid;
    }

    public void generateListView(JSONArray jsonArr) {
        final String[] items = new String[jsonArr.length()];
        if (items.length == 0) {
            quizStatusTV.setText("No more pending quizzes");
        } else {
            quizStatusTV.setVisibility(View.INVISIBLE);
        }
        for(int i = 0; i < items.length; i++) {
            try {
                items[i] = jsonArr.getJSONObject(i).getString("testId")+" - "
                        +jsonArr.getJSONObject(i).getString("quizname")+"  ["
                        +jsonArr.getJSONObject(i).getString("enddatetime")+"]";

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("ITEMS:::: ", Arrays.toString(items));
        listView = (ListView) findViewById(R.id.quizListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("CLICKED ITEM::: ",items[i]);
                Log.i("TestID for above",String.valueOf(getTestId(items, i)));

                Intent intent = new Intent(QuizList.this, quiz.class);
                intent.putExtra("testid",String.valueOf(getTestId(items, i)));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        quizStatusTV = (TextView) findViewById(R.id.quizStatusTV);

        ServerConnection sc = new ServerConnection(this);

        SharedPreferences shared = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String username = (shared.getString("username",""));

        sc.execute("test_details_return", username);

        //WAITING FOR THREAD TO FINISH, TIMEOUT FOR 200mills
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Log.i("Result=",sc.result);

        JSONMessage = sc.result;
        JSONArray jsonArray = convertToJSONArray(JSONMessage);
        //Log.i("JSONARRAY::::: ",jsonArray.toString());
        generateListView(jsonArray);

        //Log.i("JSONMESSAGE TO QUIZLIST",JSONMessage);
    }
}
