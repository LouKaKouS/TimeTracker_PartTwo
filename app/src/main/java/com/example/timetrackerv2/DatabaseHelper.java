package com.example.timetrackerv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TIMETRACKER_DATABASE";
    private static final String TABLE_NAME = "TIMETRACKER_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static final String COL_4 = "DATE";
    private static final String COL_5 = "START_TIME";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER, DATE TEXT, START_TIME TEXT, END_TIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int startHour, int startMinute, int endHour, int endMinute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, startHour + ":" + startMinute); // Insérer l'heure de début sous forme de chaîne HH:MM
        contentValues.put("END_TIME", endHour + ":" + endMinute); // Insérer l'heure de fin sous forme de chaîne HH:MM

        long result = db.insert(TABLE_NAME, null, contentValues);

        // Fermer la connexion à la base de données
        db.close();

        // Retourner true si les données sont insérées avec succès, sinon false
        return result != -1;
    }

    public void insertTask(AppModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTask());
        values.put(COL_3 , 0);
        values.put(COL_4 , model.getDate());
        values.put(COL_5 , model.getStartTime());
        values.put("END_TIME", model.getEndTime());
        db.insert(TABLE_NAME , null , values);
    }

    public void updateTask(int id, String task, String startTime, String endTime) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        values.put("START_TIME", startTime); // Mettre à jour l'heure de début
        values.put("END_TIME", endTime); // Mettre à jour l'heure de fin
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<AppModel> getAllTasks(String selectedDate) {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<AppModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, COL_4 + " = ?", new String[]{selectedDate}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        AppModel task = new AppModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        task.setDate(cursor.getString(cursor.getColumnIndex(COL_4)));

                        modelList.add(task);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return modelList;
    }
}
