package com.warys.scrooge.android.authentication.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import com.warys.scrooge.android.FirstPageDetailActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.authentication.consumer.HttpAsyncTask;
import com.warys.scrooge.android.authentication.consumer.model.request.LoginRequest;
import com.warys.scrooge.android.authentication.consumer.model.response.LoginResponse;
import com.warys.scrooge.android.authentication.service.PublicUsersApiService;
import com.warys.scrooge.android.common.consumer.RestClient;
import com.warys.scrooge.android.common.consumer.RestRequester;
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

            return !(getAuthUser() == null || getAuthUser().getToken() == null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(getApplicationContext(), FirstPageDetailActivity.class);
                startActivityForResult(intent, 0);
                startActivity(intent);
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