package com.example.remindme.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class HistoryDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "RemindME-HISTORY";
    private static final String TABLE_HISTORY = "History";
    private static final String COLUMN_NAME = "medicationName";
    private static final String COLUMN_DATE = "medDate";
    private static final String COLUMN_TIME = "medTime";
    private static final String COLUMN_STATUS = "Status";

    public HistoryDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HISTORY = "CREATE TABLE "
                + TABLE_HISTORY + "("
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public ArrayList<History> listHistory() {
        String sql = "SELECT * FROM " + TABLE_HISTORY;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<History> history = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                String status = cursor.getString(3);

                history.add(new History(name, date, time, status));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return history;
    }

    public void addHistory(History history) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, history.getMedName());
        values.put(COLUMN_DATE, history.getMedDate());
        values.put(COLUMN_TIME, history.getMedTime());
        values.put(COLUMN_STATUS, history.getStatus());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_HISTORY, null, values);
    }
}
