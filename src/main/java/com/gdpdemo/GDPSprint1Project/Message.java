package com.gdpdemo.GDPSprint1Project;

public class Message {

    private String text; // Holds the actual content of the message
    private String type; // Represents the type of the message, like "success," "error," etc.

    // Constructor that initializes the Message object with text and type
    public Message(String text, String type) {
        this.text = text;
        this.type = type;
    }

    // Getters and setters for the 'text' and 'type' fields
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
