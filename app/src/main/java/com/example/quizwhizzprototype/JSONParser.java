package com.example.quizwhizzprototype;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    private String JSONMessage;

    public JSONParser(String JSONMessage){
        this.JSONMessage = JSONMessage;
    }

    private JSONObject getJSONObject() {
        try {
            return new JSONObject(this.JSONMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        } return null;
    }

    public JSONArray getFinalJSONArray() {
        JSONObject jo = getJSONObject();
        try {
            return new JSONArray("row_data");
        } catch (JSONException e) {
            e.printStackTrace();
        } return null;
    }
}
