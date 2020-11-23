package com.example.task.task;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int taskId = 0;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "taskDetails")
    private String taskDetails;

    @ColumnInfo(name = "task_location")
    private String taskLocation;

    public Task(int userId, String taskName, String taskDetails, String taskLocation){
        this.userId = userId;
        this.taskName = taskName;
        this.taskDetails =taskDetails;
        this.taskLocation = taskLocation;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }
}