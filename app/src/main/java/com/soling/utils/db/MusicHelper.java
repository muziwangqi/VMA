package com.soling.utils.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soling.App;
import com.soling.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicHelper {

    private static final String TAG = "MusicHelper";

    public static void update(com.soling.model.Music music) {
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.Music.COLUMN_IS_LIKE, music.isLike());
        values.put(Table.Music.COLUMN_COVER_PATH, music.getLocalCoverPath());
        values.put(Table.Music.COLUMN_LYRIC_PATH, music.getLocalLyricPath());
        db.update(Table.Music.TABLE_NAME, values, Table.Music.COLUMN_MUSIC_ID + "==?", new String[]{Integer.toString(music.getId())});
        db.close();
        Log.d(TAG, "update: " + music);
    }

    public static void insert(com.soling.model.Music music) {
        Log.d(TAG, "insert: ");
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table.Music.COLUMN_MUSIC_ID, music.getId());
        values.put(Table.Music.COLUMN_IS_LIKE, music.isLike());
        values.put(Table.Music.COLUMN_COVER_PATH, music.getLocalCoverPath());
        values.put(Table.Music.COLUMN_LYRIC_PATH, music.getLocalLyricPath());
        db.insert(Table.Music.TABLE_NAME, null, values);
        db.close();
    }

    public static void delete(Music music) {
        Log.d(TAG, "delete: ");
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        db.delete(Table.Music.TABLE_NAME, Table.Music.COLUMN_MUSIC_ID + "==?", new String[]{Integer.toString(music.getId())});
        db.close();
    }

    public static List<Music> query() {
        List<Music> result = new ArrayList<>();
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        Cursor cursor = db.query(Table.Music.TABLE_NAME, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            Music music = new Music();
            music.setId(cursor.getInt(cursor.getColumnIndex(Table.Music.COLUMN_MUSIC_ID)));
            Log.d(TAG, "query: " + cursor.getInt(cursor.getColumnIndex(Table.Music.COLUMN_IS_LIKE)));
            music.setLike(cursor.getInt(cursor.getColumnIndex(Table.Music.COLUMN_IS_LIKE)) != 0);
            music.setLocalLyricPath(cursor.getString(cursor.getColumnIndex(Table.Music.COLUMN_LYRIC_PATH)));
            music.setLocalCoverPath(cursor.getString(cursor.getColumnIndex(Table.Music.COLUMN_COVER_PATH)));
            result.add(music);
        }
        db.close();
        Log.d(TAG, "query: " + result.toString());
        return result;
    }

}
