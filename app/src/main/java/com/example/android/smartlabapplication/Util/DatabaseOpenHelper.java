package com.example.android.smartlabapplication.Util;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
/**
 * Created by DELL on 1/31/2019.
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "device_commands_db.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}