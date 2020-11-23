package com.example.task.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {


    @Query("SELECT * FROM user WHERE user_name = :uName AND user_password= :uPassword")
    User loginAuthentication(String uName, String uPassword);

    @Query("SELECT * FROM user WHERE user_name = :uName")
    User usernameAuthentication(String uName);

    @Insert
    void insert(User...users);
}