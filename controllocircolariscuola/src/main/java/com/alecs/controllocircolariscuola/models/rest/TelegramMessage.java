package com.alecs.controllocircolariscuola.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

// https://core.telegram.org/bots/api#available-methods

public class TelegramMessage implements Serializable{
    private static final long serialVersionUID = 1L;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("text")
    private String text;
    @JsonProperty("parse_mode")
    private String parseMode = "markdown";

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParseMode() {
        return parseMode;
    }

    public void setParseMode(String parseMode) {
        this.parseMode = parseMode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
