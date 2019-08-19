package com.warys.scrooge.android.common.activity;

import android.app.LoaderManager;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import com.warys.scrooge.android.authentication.consumer.model.response.LoginResponse;

public abstract class AbstractActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private LoginResponse authUser;

    public LoginResponse getAuthUser() {
        return authUser;
    }

    public void setAuthUser(LoginResponse authUser) {
        this.authUser = authUser;
    }

}
