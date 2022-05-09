package com.example.remindme.offline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class reminderDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "RemindME-OFFLINE";
    private static final String TABLE_REMINDERS = "Reminders";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DESC = "Description";
    private static final String COLUMN_NAME = "medicationName";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DATE = "date";

    private static final String COLUMN_REPEAT = "REPEAT";

    public reminderDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_REMINDER = "CREATE TABLE "
                + TABLE_REMINDERS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_REPEAT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    public ArrayList<Reminders> listReminders() {
        String sql = "SELECT * FROM " + TABLE_REMINDERS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<Reminders> reminders = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String desc = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String repeat = cursor.getString(5);

                reminders.add(new Reminders(id, name, desc, date, time, repeat));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return reminders;
    }

    void addReminder(Reminders reminders) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, reminders.getName());
        values.put(COLUMN_DESC, reminders.getMedDesc());
        values.put(COLUMN_DATE, reminders.getDate());
        values.put(COLUMN_TIME, reminders.getTime());
        values.put(COLUMN_REPEAT, reminders.getIsRepeating());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_REMINDERS, null, values);
    }

    void editReminder(Reminders reminders) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, reminders.getName());
        values.put(COLUMN_DESC, reminders.getMedDesc());
        values.put(COLUMN_DATE, reminders.getDate());
        values.put(COLUMN_TIME, reminders.getTime());
        values.put(COLUMN_REPEAT, reminders.getIsRepeating());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_REMINDERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(reminders.getId())});
    }

    void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}