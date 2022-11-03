package com.example.trivia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Prefs;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;

import java.util.ArrayList;
import java.util.List;
// using animation
// Singleton class and Volley library
// Using Shared preferences for data persistence- to store data in the device - saves key value pairs in xml file
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    public int currentQuestionIndex = 0;
    List<Question> questions;
    public static ArrayList<Boolean> isAttempted;

    private static final String MESSAGE_ID = "Current_State";

    private Score score;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(this);

        Pair<Integer,Score> previousState = prefs.getState();

        currentQuestionIndex = previousState.first;
        score= previousState.second;
        binding.highestScoreView.setText(String.format(getString(R.string.highestScore),score.getHighestScore()));
        binding.currentScoreView.setText(String.format(getString(R.string.currentScore),score.getCurrentScore()));
        // this is concept of anonymous inner class
        // only one object is being created here
        questions =  new Repository().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                isAttempted = new ArrayList<>();
                for (int i = 0; i < questions.size(); i++) isAttempted.add(false);

                updateCounter(questionArrayList);
                binding.questionTextView.setText(
                        questionArrayList.get(currentQuestionIndex).getAnswer());

            }
        });


        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex=(currentQuestionIndex+1)%questions.size();
            updateQuestion();
            //setState();
        });

        binding.buttonTrue.setOnClickListener(view -> {
                checkAnswer(true);
                updateQuestion();
        });
        
        binding.buttonFalse.setOnClickListener(view -> {
                checkAnswer(false);
                updateQuestion();
        });


        //    binding.resetButton.setOnClickListener(view -> {
//            currentQuestionIndex=0;
//            score.setHighestScore(0);
//            score.setCurrentScore(0);
//            prefs.setState(currentQuestionIndex,score);
//
//        });

    }

    private void getState() {
        SharedPreferences getShareData = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        String highestScore=getShareData.getString("highestScore","Nothing Yet");
        String currentScore=getShareData.getString("currentScore","Nothing Yet");

        binding.currentScoreView.setText(currentScore);
        binding.highestScoreView.setText(highestScore);
    }

    private void setState() {
        SharedPreferences setAppState = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = setAppState.edit();


        String highestScore = binding.highestScoreView.getText().toString();
        editor.putString("highestScore",highestScore);

        String currentScore = binding.currentScoreView.getText().toString();
        editor.putString("currentScore",currentScore);


        editor.apply();
    }

    private void checkAnswer(boolean userChoice) {
        int snackMessageId = 0;

        if(questions.get(currentQuestionIndex).isAnswerCorrect()==userChoice){
            snackMessageId=R.string.correct_answer;

            if(!isAttempted.get(currentQuestionIndex)) {
                updateScore(true);
                isAttempted.set(currentQuestionIndex,true);
            }
            fadeAnimation();
        }
        else{
            snackMessageId=R.string.wrong_answer;

            if(!isAttempted.get(currentQuestionIndex)) {
                updateScore(false);
                isAttempted.set(currentQuestionIndex,true);
            }

            shakeAnimation();
        }

        Toast.makeText(this,snackMessageId, Toast.LENGTH_SHORT).show();
        //Snackbar.make(binding.cardView,snackMessageId,Snackbar.LENGTH_SHORT).show();
    }

    private void updateScore(boolean answeredCorrect) {
        if(answeredCorrect){
            score.setCurrentScore(score.getCurrentScore()+2);
        }
        else {
            score.setCurrentScore(score.getCurrentScore()-1);
        }

        binding.currentScoreView.setText(String.format(getString(R.string.currentScore), score.getCurrentScore()));
        binding.highestScoreView.setText(String.format(getString(R.string.highestScore),score.getHighestScore()));
        prefs.setCurrentScoreState(score.getCurrentScore());
       // Log.d("update Score", "updateScore: " + score.getCurrentScore());
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format(getString(R.string.test_formatted), currentQuestionIndex, questionArrayList.size()));
    }

    private void updateQuestion() {
        updateCounter((ArrayList<Question>) questions);

        binding.questionTextView.setText(
                questions.get(currentQuestionIndex).getAnswer());
    }

    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        currentQuestionIndex=(currentQuestionIndex+1)%questions.size();
        prefs.setState(currentQuestionIndex,score);
    }
}