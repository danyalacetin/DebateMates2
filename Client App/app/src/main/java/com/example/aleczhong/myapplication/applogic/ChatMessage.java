package com.example.aleczhong.myapplication.applogic;

public class ChatMessage {
    private final String content;
    private final int id;
    private final boolean isUser;

    public ChatMessage(String content, int id, boolean isUser) {
        this.content = content;
        this.id = id;
        this.isUser = isUser;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public boolean isUser() {
        return isUser;
    }
}
