package com.warys.scrooge.android.ui.authentication.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import com.warys.scrooge.android.BluePurseActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.infrastructure.activity.AbstractAuthActivity;
import com.warys.scrooge.android.domain.Context;
import com.warys.scrooge.android.infrastructure.consumer.RestClient;
import com.warys.scrooge.android.infrastructure.consumer.RestRequester;
import com.warys.scrooge.android.ui.authentication.consumer.HttpAsyncTask;
import com.warys.scrooge.android.ui.authentication.consumer.model.request.LoginRequest;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.LoginResponse;
import com.warys.scrooge.android.ui.authentication.service.PublicUsersApiService;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AbstractAuthActivity {

    private final PublicUsersApiService usersApiService = RestClient.getInstance().load(PublicUsersApiService.class);

    protected void init() {
        super.init();
        // Get views and set set listener
        TextView signUpLink = findViewById(R.id.link_signup);
        signUpLink.setOnClickListener(getActivityStarterListener(SignupActivity.class));
        Button mEmailSignInButton = findViewById(R.id.email_log_in_button);
        mEmailSignInButton.setOnClickListener(getEmailButtonListener());
        mSendFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initTask() {
        if (mAuthTask == null) {
            mAuthTask = new UserLoginTask();
        }
    }

    private class UserLoginTask extends HttpAsyncTask {

        @Override
        protected Boolean doInBackground(Void... params) {
            final Single<LoginResponse> request = usersApiService.login(new LoginRequest(email, password));

            final Consumer<LoginResponse> callback = getLoginCallback();
            RestRequester.execute(request, callback, mErrorMessageView);

            return Context.getInstance().isConnected();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(getApplicationContext(), BluePurseActivity.class);
                startActivityForResult(intent, 0);
            } else {
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}