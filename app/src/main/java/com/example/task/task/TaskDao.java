package com.example.task.task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task WHERE user_id = :userId AND task_Id = :taskId")
    Task getTask(int userId, int taskId);

    @Query("SELECT * FROM task WHERE user_id = :userId")
    List<Task> getTaskUserId(int userId);

    @Insert
    void insertTask(Task...tasks);
}
