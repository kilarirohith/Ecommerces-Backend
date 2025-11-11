package com.Kilari.response;


import lombok.Data;


@Data
public class ApiResponse {
    private String message;

    // Getter and Setter methods
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
