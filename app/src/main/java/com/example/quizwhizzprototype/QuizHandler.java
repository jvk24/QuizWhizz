package com.example.quizwhizzprototype;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class QuizHandler {

    private Context context;
    private JSONObject jsonObject;
    private JSONObject questionSet;
    private JSONArray row_data;
    private JSONObject finObject;

    public String[][] questionQueue;
    public int frontPointer;
    public int rearPointer;
    public int arrayLengthRows;
    public int arrayLengthColumns;

    QuizHandler(String JSONMessage) {
        try {
            this.jsonObject = new JSONObject(JSONMessage);
            this.row_data = jsonObject.getJSONArray("row_data");
            this.arrayLengthRows = this.row_data.length();
            this.arrayLengthColumns = 7;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.questionQueue = new String[this.arrayLengthRows][this.arrayLengthColumns];
        this.frontPointer = 0;
        this.rearPointer = 0;
    }
    public int length() {
        return this.questionQueue.length;
    }

    public boolean isFull(String[][] queue) {
        if (queue.length == this.arrayLengthRows) {
            return true;
        } return false;
    }

    public void enqueue(String[] array) {
        this.questionQueue[this.rearPointer] = array;
        this.rearPointer++;
    }

    public void dequeue() {
        for(int i = this.frontPointer; i < this.questionQueue.length - 1; i++){
            questionQueue[i] = questionQueue[i+1];
        }
    }

    //Debugging display queue in the logs
    public void displayQueue() {
        Log.i("RESULT QUESTIONSET >>>",Arrays.deepToString(this.questionQueue));
    }

    public String[][] getQueueAsArray() {
        return this.questionQueue;
    }

    public String[] peek() {
        return this.questionQueue[this.frontPointer];
    }

    public QuizHandler parseEnqueueJSONMessage(String JSONMessage) {
        //This method will parse the JSONMessage String parameter passed
        //and enqueue each sub-array question set into a queue
        //it will finally return the queue

        QuizHandler qh = new QuizHandler(JSONMessage);
        try {
            JSONObject joParent = new JSONObject(JSONMessage);
            JSONArray jaParent = joParent.getJSONArray("row_data");

            for (int i = 0; i < jaParent.length(); i++) {
                JSONObject finalObject = jaParent.getJSONObject(i);

                String questionNo = String.valueOf(finalObject.getInt("questionid"));
                //Log.i("number::",questionNo);
                String question = finalObject.getString("question");
                //Log.i("question::",question);
                String option1 = finalObject.getString("option1");
                String option2 = finalObject.getString("option2");
                String option3 = finalObject.getString("option3");
                String option4 = finalObject.getString("option4");
                String correctOption = String.valueOf(finalObject.getInt("correctoption"));
                String[] questionSet = {questionNo, question, option1, option2, option3, option4, correctOption};
                Log.i("QUESTIONSET >>>>>>>>>>>", Arrays.toString(questionSet));
                qh.enqueue(questionSet);
            }
            Log.i("ARRAY LENGTH::::<<>>>>>",String.valueOf(this.arrayLengthRows));
            return qh;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
