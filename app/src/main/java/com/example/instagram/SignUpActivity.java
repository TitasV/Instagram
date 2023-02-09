package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnKeyListener {
    TextView repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        repeatPassword = (TextView) findViewById(R.id.repeatPasswordTextView);
        repeatPassword.setOnKeyListener(this);
    }
    public void signUpButton(View view){
        TextView username = (TextView) findViewById(R.id.usernameTextView);
        TextView email = (TextView) findViewById(R.id.emailTextView);
        TextView password = (TextView) findViewById(R.id.passwordTextView);
        if (TextUtils.isEmpty(username.getText())){
            username.setError("Enter username");
        } else if (TextUtils.isEmpty(email.getText())){
            email.setError("Enter email");
        } else if (!EMAIL_ADDRESS_PATTERN.matcher(email.getText().toString()).matches()) {
            email.setError("Wrong email form");
        } else if (TextUtils.isEmpty(password.getText())){
            password.setError("Enter password");
        } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password cannot contain whitespaces and must be between 4 and 20 characters");
        } else if (TextUtils.isEmpty(repeatPassword.getText())){
            repeatPassword.setError("Repeat password");
        } else if (!password.getText().toString().equals(repeatPassword.getText().toString())){
            repeatPassword.setError("Password did not match");
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(SignUpActivity.this, "Successfully Signed Up!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void logInButton(View view){
        finish();
    }
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}"
    );
    public final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9\\S]{5,20}$");
    public void keyboardDown(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            keyboardDown(view);
            signUpButton(view);
        }
        return false;
    }
}