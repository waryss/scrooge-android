package com.warys.scrooge.android.ui.authentication.consumer.model.response;

public class LoginResponse {

    private String id = null;
    private String token = null;
    private String username = null;
    private String email = null;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
