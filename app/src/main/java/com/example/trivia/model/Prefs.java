package com.example.trivia.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

public class Prefs {

    public static final String CURRENT_QUESTION_INDEX = "currentQuestionIndex";
    public static final String CURRENT_SCORE = "currentScore";
    public static final String HIGHEST_SCORE = "highestScore";
    private final SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void setState(int currentQuestionIndex,Score score) {
        SharedPreferences.Editor editor =  preferences.edit();
        //preferences.edit().putString("highestScore",highestScore).apply();
        editor.putInt(CURRENT_QUESTION_INDEX,currentQuestionIndex);
        editor.putInt(CURRENT_SCORE,score.getCurrentScore());
        editor.putInt(HIGHEST_SCORE,score.getHighestScore());
        editor.apply();
        //Log.d("Set", "setState: "+preferences.getInt("currentQuestionIndex",0));
    }

    public void setCurrentScoreState(int currentScore){
        preferences.edit().putInt(CURRENT_SCORE,currentScore).apply();
        //Log.d("Score", "setCurrentScoreState: "+ currentScore);
    }

    public Pair<Integer, Score> getState() {
        Score score = new Score();
        score.setCurrentScore(preferences.getInt(CURRENT_SCORE,0));
        score.setHighestScore(preferences.getInt(HIGHEST_SCORE,0));
        Integer currentQuestionIndex= preferences.getInt(CURRENT_QUESTION_INDEX,0);
        //Log.d("Get", "getState: "+currentQuestionIndex);
        return Pair.create(currentQuestionIndex,score);

    }
}
