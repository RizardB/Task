package com.example.task.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.task.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String sharedPreferencesName = "SharedPreferences";
    private static final String isLogged = "IsLoggedIn";
    public static final String key_username = "username";
    public static final String key_password = "password";

    public SessionManager(Context context){

        this.context = context;
        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String username, String password){
        editor.putBoolean(isLogged, true);
        editor.putString(key_username, username);
        editor.putString(key_password, password);
        editor.commit();
    }

    public HashMap getUserDetails(){

        HashMap user = new HashMap();
        user.put(key_username, sharedPreferences.getString(key_username, null));
        user.put(key_password, sharedPreferences.getString(key_password, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(isLogged, false);
    }

}
