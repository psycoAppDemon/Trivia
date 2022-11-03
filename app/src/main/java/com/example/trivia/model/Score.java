package com.example.trivia.model;

public class Score {
    private int highestScore=0,currentScore=0;

    public int getHighestScore() {
        return highestScore;
    }

    public Score(){}

    public int getCurrentScore() {
        return currentScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore=highestScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
        if(this.currentScore<0)this.currentScore=0;
        highestScore=Math.max(this.currentScore,highestScore);
    }
}
