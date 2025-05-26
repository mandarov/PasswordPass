package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import com.example.myapplication.DatabaseHelper;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class PasswordManagerActivity extends AppCompatActivity {

    private EditText serviceNameInput, loginInput, passwordInput;
    private Button saveButton, viewButton;
    private TextView savedDataTextView;

    private int userId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            finish();
        }

        databaseHelper = new DatabaseHelper(this);

        serviceNameInput = findViewById(R.id.serviceNameInput);
        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        saveButton = findViewById(R.id.saveButton);
        viewButton = findViewById(R.id.viewButton);
        savedDataTextView = findViewById(R.id.savedDataTextView);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewData();
            }
        });
    }

    private void saveData() {
        String serviceName = serviceNameInput.getText().toString();
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (serviceName.isEmpty() || login.isEmpty() || password.isEmpty()) {
            savedDataTextView.setText("Все поля должны быть заполнены!");
            return;
        }

        boolean isSaved = databaseHelper.savePassword(userId, serviceName, login, password);
        if (isSaved) {
            savedDataTextView.setText("Данные для " + serviceName + " успешно сохранены!");
        } else {
            savedDataTextView.setText("Ошибка при сохранении данных!");
        }
    }

    private void viewData() {
        Cursor cursor = databaseHelper.getPasswordsForUser(userId);
        StringBuilder data = new StringBuilder("Сохраненные данные:\n");

        if (cursor.getCount() == 0) {
            data.append("Нет сохраненных данных.");
        } else {
            while (cursor.moveToNext()) {
                int passwordId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String serviceName = cursor.getString(cursor.getColumnIndexOrThrow("service_name"));
                String login = cursor.getString(cursor.getColumnIndexOrThrow("login"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                data.append("ID: ").append(passwordId).append("\n");
                data.append("Сервис: ").append(serviceName).append("\n");
                data.append("Логин: ").append(login).append("\n");
                data.append("Пароль: ").append(password).append("\n\n");
            }
        }

        cursor.close();
        savedDataTextView.setText(data.toString());
    }

    public void deletePassword(int passwordId) {
        boolean isDeleted = databaseHelper.deletePassword(passwordId);
        if (isDeleted) {
            savedDataTextView.setText("Пароль успешно удален!");
        } else {
            savedDataTextView.setText("Ошибка при удалении пароля!");
        }
        viewData();
    }
}