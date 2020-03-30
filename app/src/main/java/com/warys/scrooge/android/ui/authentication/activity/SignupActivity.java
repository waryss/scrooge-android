package com.warys.scrooge.android.ui.authentication.activity;

import android.content.Intent;
import android.widget.Button;
import com.warys.scrooge.android.BluePurseActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.infrastructure.activity.AbstractAuthActivity;
import com.warys.scrooge.android.infrastructure.consumer.RestClient;
import com.warys.scrooge.android.infrastructure.consumer.RestRequester;
import com.warys.scrooge.android.ui.authentication.consumer.HttpAsyncTask;
import com.warys.scrooge.android.ui.authentication.consumer.model.request.UserRequest;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.UserResponse;
import com.warys.scrooge.android.ui.authentication.service.PublicUsersApiService;
import io.reactivex.Single;

public class SignupActivity extends AbstractAuthActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private SignupTask mAuthTask = null;

    private PublicUsersApiService usersApiService = RestClient.getInstance().load(PublicUsersApiService.class);

    protected void init() {
        // Get views and set set listener
        super.init();
        Button mEmailSignInButton = findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(getEmailButtonListener());
        mSendFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public void initTask() {
        if (mAuthTask == null) {
            mAuthTask = new SignupTask();
        }
    }

    public class SignupTask extends HttpAsyncTask {

        @Override
        protected Boolean doInBackground(Void... params) {
            final UserRequest userRequest = new UserRequest();
            userRequest.setUsername(login);
            userRequest.setEmail(email);
            userRequest.setPassword(password);
            final Single<UserResponse> request = usersApiService.register(userRequest);
            RestRequester.execute(request, mErrorMessageView);
            return true;
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