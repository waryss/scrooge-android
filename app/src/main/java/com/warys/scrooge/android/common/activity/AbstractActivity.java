package com.warys.scrooge.android.common.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.authentication.consumer.model.response.LoginResponse;
import io.reactivex.functions.Consumer;

public abstract class AbstractActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected TextView mErrorMessageView;
    private LoginResponse authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        mErrorMessageView = findViewById(R.id.error_panel);
        init();
    }

    protected abstract void init();

    protected abstract int getActivityLayoutId();

    public LoginResponse getAuthUser() {
        return authUser;
    }

    public void setAuthUser(LoginResponse authUser) {
        this.authUser = authUser;
    }

    public <T> View.OnClickListener getActivityStarterListener(final Class<T> activityClass) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activityClass);
                startActivityForResult(intent, 0);
            }
        };
    }

    protected Consumer<LoginResponse> getLoginCallback() {
        return new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) {
                setAuthUser(response);
            }
        };
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // if (requestCode == REQUEST_READ_CONTACTS) {
        // manage autocompletion
        // }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
