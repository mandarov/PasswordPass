package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText loginInput, passwordInput;
    private Button loginButton, registerButton;
    private TextView errorTextView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        errorTextView = findViewById(R.id.errorTextView);

        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Все поля должны быть заполнены!");
            return;
        }

        int userId = databaseHelper.loginUser(login, password);
        if (userId == -1) {
            errorTextView.setText("Неверный логин или пароль!");
            return;
        }

        Intent intent = new Intent(MainActivity.this, PasswordManagerActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }
}