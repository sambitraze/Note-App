package com.xstudioo.noteme;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SimpleDatabase extends SQLiteOpenHelper {
    // declare require values
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "SimpleDB";
    private static final String TABLE_NAME = "SimpleTable";
    // declare table column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_STATUS = "status";

    public SimpleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_STATUS + " TEXT"
                + " )";
        db.execSQL(createDb);
    }

    // upgrade db if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, note.getTitle());
        v.put(KEY_CONTENT, note.getContent());
        v.put(KEY_DATE, note.getDate());
        v.put(KEY_TIME, note.getTime());
        v.put(KEY_STATUS, note.getStatus());

        // inserting data into db
        return db.insert(TABLE_NAME, null, v);
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_STATUS};
        Cursor cursor = db.query(TABLE_NAME, query, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new Note(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
        );
    }

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    class sortNotes implements Comparator<Note> {
        // Used for sorting in ascending order of
        // roll number
        @SuppressLint("SimpleDateFormat")
        public int compare(Note a, Note b) {
            String statusA = a.getStatus();
            String statusB = b.getStatus();
            Date dateA = null;
            Date dateB = null;
            try {
                dateA = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(a.getDate().concat(" ").concat(a.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                dateB = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(b.getDate().concat(" ").concat(b.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int StatusCompare = statusB.compareTo(statusA);
            int DateCompare = 0;
            if (dateB.after(
                    dateA)) {
                DateCompare = 1;
            } else if (dateB.before(dateA)) {
                DateCompare = -1;
            } else if (dateB.equals(dateA)) {
                DateCompare = 0;
            }
            return (StatusCompare == 0) ? DateCompare
                    : StatusCompare;
        }
    }

    public List<Note> getAllNotes() {
        ArrayList<Note> allNotes = new ArrayList<>();
//        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_ID + " DESC";
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setStatus(cursor.getString(5));
                allNotes.add(note);
            } while (cursor.moveToNext());
        }

        Collections.sort(allNotes, new sortNotes());

        return allNotes;

    }

    public int editNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_STATUS, note.getStatus());
        return db.update(TABLE_NAME, c, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
    }


    void deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


}
