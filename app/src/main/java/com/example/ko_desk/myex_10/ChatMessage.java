package com.example.ko_desk.myex_10;

public class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(String message) {
        this.message = message;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLeft() {
        return left;
    }

    public String getMessage() {
        return message;
    }

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }

}

