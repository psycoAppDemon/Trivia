package com.example.trivia.model;

public class Question {
    private String answer;
    private boolean answerCorrect;


    public Question() {}

    public Question(String answer, boolean answerCorrect) {
        this.answer = answer;
        this.answerCorrect = answerCorrect;
    }
    public String getAnswer() {
        return answer;
    }

    public boolean isAnswerCorrect() {
        return answerCorrect;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerCorrect=" + answerCorrect +
                '}';
    }
}
