package com.soling.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final int VERSION = 1;

    private static final String CREATE_MUSIC_LIKE = "create table " + Table.Music.TABLE_NAME + " (" +
            Table.Music.COLUMN_ID + " integer primary key autoincrement, " +
            Table.Music.COLUMN_MUSIC_ID + " integer, " +
            Table.Music.COLUMN_COVER_PATH + " text, " +
            Table.Music.COLUMN_LYRIC_PATH + " text, " +
            Table.Music.COLUMN_IS_LIKE + " integer" +
            ")";
    
    private Context context;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MUSIC_LIKE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    
}
