package com.example.aleczhong.myapplication.applogic;

public interface MatchDisplayInterface {
    void matchAnnouncement(final String msg);
    void displayQuestion(final String question);
    void messageUpdate();
    void enableInput(boolean value);
}
