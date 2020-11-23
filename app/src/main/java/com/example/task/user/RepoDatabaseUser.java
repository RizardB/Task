package com.example.task.user;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class RepoDatabaseUser extends RoomDatabase {

    private static final String DB_NAME = "user.db";
    private static volatile RepoDatabaseUser instance;

    public static synchronized RepoDatabaseUser getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static RepoDatabaseUser create (final Context context){
        return Room.databaseBuilder(context, RepoDatabaseUser.class, DB_NAME).allowMainThreadQueries().build();
    }

    public abstract UserDao getUserDao();
}