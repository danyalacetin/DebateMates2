package com.example.aleczhong.myapplication.applogic;

public class Question {
    private final String content;
    private int score;

    public Question(String content) {
        this.content = content;
        this.score = 5;
        QuestionTest test = new QuestionTest("hello");
        ChatMessageTest test2 = new ChatMessageTest("test", 1, MessageType.OPPONENT);
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
