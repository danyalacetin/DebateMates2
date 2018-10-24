package com.example.aleczhong.myapplication.applogic;

import android.support.annotation.NonNull;

public class ChatMessage implements Comparable<Integer> {
    private final String content;
    private final Integer id;
    private final MessageType type;

    public ChatMessage(String content, int id, MessageType type) {
        this.content = content;
        this.id = id;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public int compareTo(@NonNull Integer integer) {
        return id.compareTo(integer);
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s", id, type.toString(), content);
    }
}
