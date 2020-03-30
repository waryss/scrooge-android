package com.warys.scrooge.android.infrastructure.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.domain.Context;
import com.warys.scrooge.android.ui.authentication.consumer.HttpAsyncTask;
import com.warys.scrooge.android.ui.authentication.consumer.model.response.LoginResponse;
import io.reactivex.functions.Consumer;

import static com.warys.scrooge.android.infrastructure.common.StringUtil.isEmailValid;
import static com.warys.scrooge.android.infrastructure.common.StringUtil.isPasswordValid;

public abstract class AbstractAuthActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    protected HttpAsyncTask mAuthTask = null;

    // UI references.
    protected AutoCompleteTextView mEmailView;
    protected EditText mPasswordView;
    protected View mProgressView;
    protected View mSendFormView;
    private EditText mLoginView;

    // UI references.
    protected String login;
    protected String email;
    protected String password;

    protected void init() {
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(getPasswordViewListener());
    }

    protected TextView mErrorMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        mErrorMessageView = findViewById(R.id.error_panel);
        init();
    }

    protected abstract int getActivityLayoutId();

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
                Context.getInstance().setCurrentUser(response);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSendFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    protected void attemptLogin() {
        if (getTask() != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if (mLoginView != null) {
            login = mLoginView.getText().toString();
        }

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

        // Check for a valid login.
        if (mLoginView != null && TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
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
            initTask();
            getTask().execute((Void) null);
        }
    }

    public abstract void initTask();

    public HttpAsyncTask getTask() {
        return mAuthTask;
    }

    protected View.OnClickListener getEmailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        };
    }

    protected TextView.OnEditorActionListener getPasswordViewListener() {
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

}
