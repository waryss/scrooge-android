package com.warys.scrooge.android;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.warys.scrooge.android.domain.Context;
import com.warys.scrooge.android.ui.authentication.activity.LoginActivity;

public class AuthenticatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAuthentication();
    }

    private void checkAuthentication() {
        if (!Context.getInstance().isConnected()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }
}
