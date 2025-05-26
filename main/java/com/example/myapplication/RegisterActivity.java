package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerLoginInput, registerPasswordInput;
    private Button registerButton;
    private TextView registerErrorTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLoginInput = findViewById(R.id.registerLoginInput);
        registerPasswordInput = findViewById(R.id.registerPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        registerErrorTextView = findViewById(R.id.registerErrorTextView);

        databaseHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String login = registerLoginInput.getText().toString();
        String password = registerPasswordInput.getText().toString();

        if (login.isEmpty() || password.isEmpty()) {
            registerErrorTextView.setText("Все поля должны быть заполнены!");
            return;
        }

        boolean isRegistered = databaseHelper.registerUser(login, password);
        if (isRegistered) {
            registerErrorTextView.setText("Регистрация успешна! Теперь вы можете войти.");
        } else {
            registerErrorTextView.setText("Пользователь с таким логином уже существует!");
        }
    }
}