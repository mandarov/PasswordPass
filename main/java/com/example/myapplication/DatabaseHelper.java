package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PasswordManager.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_LOGIN = "login";
    private static final String COLUMN_USER_PASSWORD = "password";

    private static final String TABLE_PASSWORDS = "Passwords";
    private static final String COLUMN_PASSWORD_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_SERVICE_NAME = "service_name";
    private static final String COLUMN_PASSWORD_LOGIN = "login";
    private static final String COLUMN_PASSWORD_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_LOGIN + " TEXT UNIQUE NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

        String CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + " (" +
                COLUMN_PASSWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID_FK + " INTEGER NOT NULL, " +
                COLUMN_SERVICE_NAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD_LOGIN + " TEXT NOT NULL, " +
                COLUMN_PASSWORD_PASSWORD + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " +
                TABLE_USERS + "(" + COLUMN_USER_ID + "));";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PASSWORDS_TABLE);

        System.out.println("Таблицы успешно созданы");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_LOGIN, login);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public int loginUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USER_LOGIN + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{login, password});

        int userId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
        }
        return userId;
    }

    public boolean savePassword(int userId, String serviceName, String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_SERVICE_NAME, serviceName);
        values.put(COLUMN_PASSWORD_LOGIN, login);
        values.put(COLUMN_PASSWORD_PASSWORD, password);

        long result = db.insert(TABLE_PASSWORDS, null, values);
        return result != -1;
    }

    public Cursor getPasswordsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PASSWORDS +
                " WHERE " + COLUMN_USER_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public boolean deletePassword(int passwordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PASSWORDS, COLUMN_PASSWORD_ID + " = ?", new String[]{String.valueOf(passwordId)});
        return rowsDeleted > 0;
    }
}