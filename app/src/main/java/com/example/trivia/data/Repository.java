package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    ArrayList<Question> questionArrayList= new ArrayList<>();
    String url ="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListAsyncResponse callBack){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            questionArrayList.add(new Question(
                                    response.getJSONArray(i).getString(0),
                                    response.getJSONArray(i).getBoolean(1)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("TAG", "onCreate: "+ response.getJSONArray(i).get(0));
                    }
                    if(null !=callBack){
                        callBack.processFinished(questionArrayList);
                    }
                }, error -> {

        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
