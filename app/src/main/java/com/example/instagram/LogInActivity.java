package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity implements View.OnKeyListener {
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        password = (TextView) findViewById(R.id.passwordTextView);
        password.setOnKeyListener(this);
    }
    public void logInButton(View view){
        TextView username = (TextView) findViewById(R.id.usernameTextView);
        if (TextUtils.isEmpty(username.getText())){
            username.setError("Enter your username");
        } else if (TextUtils.isEmpty(password.getText())) {
            password.setError("Enter your password");
        } else if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password cannot contain whitespaces and must be between 4 and 20 characters");
        } else {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void signUpButton(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
    public void forgotPasswordButton(View view){
        Log.i("Button", "Forgot password");
    }
    public final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9\\S]{5,20}$");

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            keyboardDown(view);
            logInButton(view);
        }
        return false;
    }
    public void keyboardDown(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}