package com.example.aleczhong.myapplication.applogic;

public class Question {
    private final String content;
    private int score;

    public Question(String content) {
        this.content = content;
        this.score = 5;
    }

    public String getContent() { return content; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
