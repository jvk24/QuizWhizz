package com.example.quizwhizzprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_SHORT;

public class ServerConnection extends AsyncTask<String, Void, String> {

    private Context context;
    private Toast toast;
    private String type;

    //private String ip = "192.168.0.52";
    private String ip = "192.168.0.52"; //private, non-routable IP
    //private String ip = ""; //School temp IP
    
    private String createAccountPHP = "createAccount.php";
    private String createQuizPHP = "createQuiz.php";
    private String loginPHP = "login.php";
    private String quizPHP = "retrieveQuestions.php";
    private String testDetailsPHP = "testDetails.php";
    private String questionsPHP = "questions.php";
    private String testDetailsReturnPHP = "quizList.php";
    private String recordScorePHP = "recordStudentTestScore.php";
    private String retrieveScorePHP = "retrieveScore.php";
    private String retrieveStatsPHP = "retrieveStats.php";
    private String retriveDurationPHP = "retrieveDuration.php";

    public String result;

    ServerConnection(Context ctx) {
        context = ctx;
        //this.Result = "";

    }

    private HttpURLConnection HTTP_POST_CONNECTION(String complete_url, int flag) {
        try {
            URL url = new URL(complete_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (flag == 0) {
                httpURLConnection.setRequestMethod("POST");
            } else {
                httpURLConnection.setRequestMethod("GET");
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private OutputStream OUTPUT_STREAM_PROCESS(HttpURLConnection httpURLConnection) {
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            return outputStream;
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }

    private void BUFFERED_WRITER_PROCESS(OutputStream outputStream, String post_data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String BUFFERED_READER_PROCESS(HttpURLConnection httpURLConnection) {
        String result = "";
        String line = "";

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }

    private void NO_INPUT_PROCESS(String complete_url) {
        try {
            StringBuilder resultSB = new StringBuilder();
            URL obj = new URL(complete_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
            httpURLConnection.setRequestMethod("POST");
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                resultSB.append(line);
            }
            rd.close();
            result = resultSB.toString();
            Log.i("result:::: ",result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        type = params[0];

        if (type.equals("create_account")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+createAccountPHP;
            String user_name = params[1];
            String password = params[2];
            String name = params[3];


            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0); //1ST STEP ==================

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection); //2ND STEP ======================

            try {
                String post_data = URLEncoder.encode("user_name", "UTF-8")
                            + "=" + URLEncoder.encode(user_name, "UTF-8")
                            + "&" + URLEncoder.encode("password", "UTF-8")
                            + "=" + URLEncoder.encode(password, "UTF-8")
                            + "&" + URLEncoder.encode("name", "UTF-8")
                            + "=" + URLEncoder.encode(name, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================
             } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            return result;

        } else if (type == "login") {

            String complete_url = "http://"+ip+"/quizwhizz/"+loginPHP;
            String user_name = params[1];
            String password = params[2];

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0); //1ST STEP ==================

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection); //2ND STEP ======================

            try {
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            if (result.contains("Success"))
            {
                SharedPreferences sharedpreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("username", user_name);
                editor.commit();
            }
            return result;

        } else if (type.equals("create_quiz")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+questionsPHP+"?";
            String question = params[1];
            String option1 = params[2];
            String option2 = params[3];
            String option3 = params[4];
            String option4 = params[5];
            String correct_option = params[6];

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0); //1ST STEP ==================

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection); //2ND STEP ======================

            try {
                String post_data = URLEncoder.encode("question", "UTF-8")
                        + "=" + URLEncoder.encode(question, "UTF-8")
                        + "&" + URLEncoder.encode("option1", "UTF-8")
                        + "=" + URLEncoder.encode(option1, "UTF-8")
                        + "&" + URLEncoder.encode("option2", "UTF-8")
                        + "=" + URLEncoder.encode(option2, "UTF-8")
                        + "&" + URLEncoder.encode("option3", "UTF-8")
                        + "=" + URLEncoder.encode(option3, "UTF-8")
                        + "&" + URLEncoder.encode("option4", "UTF-8")
                        + "=" + URLEncoder.encode(option4, "UTF-8")
                        + "&" + URLEncoder.encode("correctOption", "UTF-8")
                        + "=" + URLEncoder.encode(correct_option, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            return result;

        } else if (type.equals("quiz")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+quizPHP+"?";

            String username = params[1];
            String testid = params[2];

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0); //1ST STEP ==================

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection); //2ND STEP ======================

            try {
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("testid","UTF-8")+"="+URLEncoder.encode(testid,"UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            return result;
            //CONTINUE STEPS...

        } else if (type.equals("test_details")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+testDetailsPHP+"?";

            String quiz_name = params[1];
            String quiz_due_date = params[2];
            String quiz_duration = params[3];

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0);

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection);
            result = "Success";
            try {
                String post_data = URLEncoder.encode("quizName", "UTF-8")
                        + "=" + URLEncoder.encode(quiz_name, "UTF-8")
                        + "&" + URLEncoder.encode("quizDueDate", "UTF-8")
                        + "=" + URLEncoder.encode(quiz_due_date, "UTF-8")
                        + "&" + URLEncoder.encode("quizDuration", "UTF-8")
                        + "=" + URLEncoder.encode(quiz_duration, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (Exception e) {
                result = "Failed";
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            return result;

        } else if (type.equals("test_details_return")) {

            String username = params[1];

            String complete_url = "http://"+ip+"/quizwhizz/"+testDetailsReturnPHP+"?";

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0);

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection);
            result = "Success";
            try {
                String post_data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (Exception e) {
                result= "Failed";
                e.printStackTrace();
            }

            if (result.equals("Success")) {
                result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
                return result;
            }
            else {
                return null;
            }
        } else if (type.equals("record_score")) {
            String complete_url = "http://"+ip+"/quizwhizz/"+recordScorePHP+"?";

            String username = params[1];
            String testid = params[2];
            String score = params[3];

            Log.i("USERNAME RECORD SCORE",username+" and "+testid+" and "+score);

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0);

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection);
            result = "Success";
            try {
                String post_data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username, "UTF-8")
                        + "&" + URLEncoder.encode("testid", "UTF-8")
                        + "=" + URLEncoder.encode(testid, "UTF-8")
                        + "&" + URLEncoder.encode("score", "UTF-8")
                        + "=" + URLEncoder.encode(score, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (Exception e) {
                result= "Failed";
                e.printStackTrace();
            }
            if (result.equals("Success")) {
                result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
                return result;
            }
            else {
                return null;
            }

        } else if (type.equals("retrieve_score")) {
            String complete_url = "http://"+ip+"/quizwhizz/"+retrieveScorePHP+"?";

            NO_INPUT_PROCESS(complete_url);

        } else if (type.equals("retrieve_stats")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+retrieveStatsPHP+"?";

            NO_INPUT_PROCESS(complete_url);

        } else if (type.equals("retrieve_duration")) {

            String complete_url = "http://"+ip+"/quizwhizz/"+retriveDurationPHP+"?";

            String testId = params[1];

            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0); //1ST STEP ==================

            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection); //2ND STEP ======================

            try {
                String post_data = URLEncoder.encode("testId","UTF-8")+"="+URLEncoder.encode(testId,"UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
            return result;

            /*
            HttpURLConnection httpURLConnection = HTTP_POST_CONNECTION(complete_url, 0);


            OutputStream outputStream = OUTPUT_STREAM_PROCESS(httpURLConnection);
            result = "Success";
            try {
                String post_data = URLEncoder.encode("testid", "UTF-8")
                        + "=" + URLEncoder.encode(testId, "UTF-8");

                BUFFERED_WRITER_PROCESS(outputStream, post_data); //3RD STEP ======================================

            } catch (Exception e) {
                result= "Failed";
                e.printStackTrace();
            }

            if (result.equals("Success")) {
                result = BUFFERED_READER_PROCESS(httpURLConnection); //4TH STEP ================================
                return result;
            }
            else {
                return null;
            }*/
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        /*
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");*/
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        /*
        alertDialog.setMessage(result);
        alertDialog.show();*/
        if (result != null && context != null && type != "quiz" && type != "test_details_return" && type != "retrieve_duration" && type != "record_score") {
            toast = Toast.makeText(context, result, LENGTH_SHORT);
            toast.show();
        }
    }
}