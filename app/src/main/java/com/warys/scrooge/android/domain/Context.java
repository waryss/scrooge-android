package com.warys.scrooge.android.domain;

import android.view.View;
import com.google.android.material.snackbar.Snackbar;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.LoginResponse;

public class Context {

    public enum PageFragment {
        HOME, ANALYSE, SEND, SHARE, SLIDESHOW, TOOLS
    }

    private static final Context INSTANCE = new Context();
    private LoginResponse currentUser;
    private PageFragment currentFragment;


    public static Context getInstance() {
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

    public PageFragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(PageFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void addItem(View view) {
        Snackbar.make(view, "Replace with your own action" + getCurrentFragment(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
