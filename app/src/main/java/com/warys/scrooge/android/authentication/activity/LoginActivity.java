package com.warys.scrooge.android.authentication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.warys.scrooge.android.FirstPageDetailActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.authentication.consumer.model.request.LoginRequest;
import com.warys.scrooge.android.authentication.consumer.model.response.LoginResponse;
import com.warys.scrooge.android.authentication.service.PublicUsersApiService;
import com.warys.scrooge.android.common.Logger;
import com.warys.scrooge.android.common.activity.AbstractActivity;
import com.warys.scrooge.android.common.consumer.RestClient;
import com.warys.scrooge.android.common.consumer.RestRequester;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static com.warys.scrooge.android.authentication.activity.util.LoginUtil.isEmailValid;
import static com.warys.scrooge.android.authentication.activity.util.LoginUtil.isPasswordValid;

public class LoginActivity extends AbstractActivity {

    private static final Logger LOGGER = new Logger(LoginActivity.class);

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private PublicUsersApiService usersApiService = RestClient.getInstance().load(PublicUsersApiService.class);
    private Button mEmailSignInButton;

    protected void init() {
        TextView signUpLink = findViewById(R.id.link_signup);

        signUpLink.setOnClickListener(getActivityStarterListener(SignupActivity.class));
        // Get views and set set listener
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.email_log_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView.setOnEditorActionListener(getPasswordViewListener());
        mEmailSignInButton.setOnClickListener(getEmailButtonListener());
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    private OnClickListener getEmailButtonListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        };
    }

    private TextView.OnEditorActionListener getPasswordViewListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final Single<LoginResponse> request = usersApiService.login(new LoginRequest(mEmail, mPassword));

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