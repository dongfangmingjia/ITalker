package com.warner.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by warner on 2018/1/16.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
