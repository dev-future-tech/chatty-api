package com.example.restservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SocketResponse {
    private String message;

    @JsonProperty("prev_message")
    private String prevMessage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrevMessage() {
        return prevMessage;
    }

    public void setPrevMessage(String prevMessage) {
        this.prevMessage = prevMessage;
    }
}
