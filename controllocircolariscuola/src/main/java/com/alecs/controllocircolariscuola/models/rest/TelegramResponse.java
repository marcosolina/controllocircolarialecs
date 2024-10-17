package com.alecs.controllocircolariscuola.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("error_code")
    private int erroCode;
    @JsonProperty("description")
    private String description;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getErroCode() {
        return erroCode;
    }

    public void setErroCode(int erroCode) {
        this.erroCode = erroCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
