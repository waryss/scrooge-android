package com.warys.scrooge.android.infrastructure.activity;

import com.warys.scrooge.android.ui.authentication.consumer.model.response.LoginResponse;

public class SharedData {

    private static final SharedData INSTANCE = new SharedData();
    private LoginResponse currentUser;


    public static SharedData getInstance() {
        return INSTANCE;
    }

    public boolean isConnected() {
        return currentUser != null && currentUser.getToken() != null;
    }

    public LoginResponse getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginResponse currentUser) {
        this.currentUser = currentUser;
    }
}
