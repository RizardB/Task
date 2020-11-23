package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import com.example.task.user.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    Button registerLoginButton;
    EditText registerUserName, registerPassword, registerRepeatPassword;
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        registerLoginButton = findViewById(R.id.registerLoginButton);
        registerUserName = findViewById(R.id.editTextRegisterUserName);
        registerPassword = findViewById(R.id.editTextRegisterPassword);
        registerRepeatPassword = findViewById(R.id.editTextRegisterRepeatPassword);

        registerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = registerUserName.getText().toString();
                final String password = registerPassword.getText().toString();
                final String repeatPassword = registerRepeatPassword.getText().toString();
                if(toast != null)
                    toast.cancel();
                registerValidate(username,password,repeatPassword);
            }
        });
    }
    public void registerValidate(String username, String password, String repeatPassword){
        if(username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            toast = Toast.makeText(getApplicationContext(), "Boş Alanları Doldurun!", Toast.LENGTH_SHORT);
            toast.show();
        }else if(!password.equals(repeatPassword)){
            toast = Toast.makeText(getApplicationContext(), "Şifreler Uyuşmuyor!", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            RepoDatabaseUser databaseUser = RepoDatabaseUser.getInstance(getApplicationContext());
            final UserDao userDao = databaseUser.getUserDao();
            User user = userDao.usernameAuthentication(username);
            if(user==null){
                userDao.insert(new User(username,password));
                toast = Toast.makeText(getApplicationContext(), "Kayıt Başarılı!", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }else{
                toast = Toast.makeText(getApplicationContext(), "Kullanıcı Adı Mevcut!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}