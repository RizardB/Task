package com.example.task.task;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class RepoDatabaseTask extends RoomDatabase {

    private static final String DB_NAME = "task.db";
    private static volatile RepoDatabaseTask instance;

    public static synchronized RepoDatabaseTask getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static RepoDatabaseTask create (final Context context){
        return Room.databaseBuilder(context, RepoDatabaseTask.class, DB_NAME).allowMainThreadQueries().build();
    }

    public abstract TaskDao getTaskDao();
}
