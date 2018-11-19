package com.soling.utils.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.soling.App;
import com.soling.model.Music;

public class MusicLikeHelper {

    public static void insertLike(Music music) {
        App.getInstance().getLikeMusics().getMusics().add(music);
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.Table.MusicLike.COLUMN_MUSIC_ID, music.getId());
        db.insert(DBHelper.Table.MusicLike.TABLE_NAME, null, values);
        db.close();
    }

    public static void deleteLike(Music music) {
        App.getInstance().getLikeMusics().getMusics().remove(music);
        SQLiteDatabase db = App.getInstance().getDbHelper().getWritableDatabase();
        db.delete(DBHelper.Table.MusicLike.TABLE_NAME, DBHelper.Table.MusicLike.COLUMN_MUSIC_ID + "==?", new String[]{Integer.toString(music.getId())});
        db.close();
    }

}
