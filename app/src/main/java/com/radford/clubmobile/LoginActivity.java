package com.radford.clubmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.radford.clubmobile.managers.UserManager;
import com.radford.clubmobile.models.LoginRequest;
import com.radford.clubmobile.models.LoginResponse;
import com.radford.clubmobile.networking.ClubService;
import com.radford.clubmobile.networking.ClubServiceProvider;
import com.radford.clubmobile.utils.AlertHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Callback<LoginResponse> {
    private static final int REGISTRATION_REQUEST = 1;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private ScrollView mLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginView = findViewById(R.id.login_form);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivityForResult(intent, REGISTRATION_REQUEST);
            }
        });

//        mUsernameView.setText("clubuser");
//        mPasswordView.setText("pass");
//        attemptLogin();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if(!email.substring(email.indexOf('@') + 1).toLowerCase().equals("radford.edu")) {
            mEmailView.setError(getString(R.string.error_invalid_radford_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            ClubService service = ClubServiceProvider.getService();
            LoginRequest request = new LoginRequest(email, password);
            service.login(request).enqueue(this);

        }
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            UserManager.setSessionId(response.body().getSessionId());
            UserManager.setUser(response.body().getUser());

            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);

            finish();
        } else if(response.code() == 403){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must verify your email to sign in.")
                    .setCancelable(false)
                    .setPositiveButton("Resend code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ClubService service = ClubServiceProvider.getService();
                            LoginRequest request = new LoginRequest(mEmailView.getText().toString(), mPasswordView.getText().toString());
                            service.resendEmail(request).enqueue(new ResendCallback());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
        } else {
            AlertHelper.makeErrorDialog(this, "Failed to login").show();
        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        AlertHelper.makeErrorDialog(this, "Failed to login").show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(mLoginView, "Account created", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class ResendCallback implements Callback<Void> {

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.isSuccessful()) {
                Snackbar.make(mLoginView, "Resent email", Snackbar.LENGTH_LONG).show();
            } else {
                AlertHelper.makeErrorDialog(LoginActivity.this, "Failed to resend code").show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            AlertHelper.makeErrorDialog(LoginActivity.this, "Failed to resend code").show();
        }
    }
}

