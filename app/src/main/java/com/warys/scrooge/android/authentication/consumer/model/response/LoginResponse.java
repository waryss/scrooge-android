package com.warys.scrooge.android.authentication.consumer.model.response;

public class LoginResponse {

    private String id;
    private String token;
    private String username;
    private String email;

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
                ", authUser='" + token + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
