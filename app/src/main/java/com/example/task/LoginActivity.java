package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.session.SessionManager;
import com.example.task.user.RepoDatabaseUser;
import com.example.task.user.User;
import com.example.task.user.UserDao;

import static com.example.task.session.SessionManager.key_password;
import static com.example.task.session.SessionManager.key_username;

public class LoginActivity extends AppCompatActivity {

    Button loginButton,loginRegisterButton;
    TextView textViewUserName, textViewPassword;
    Toast toast = null;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        loginRegisterButton = findViewById(R.id.loginRegisterButton);
        textViewUserName = findViewById(R.id.editTextLoginUserName);
        textViewPassword= findViewById(R.id.editTextLoginPassword);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()){
            String loggedUsername = (String) session.getUserDetails().get(key_username);
            String loggedPassword = (String) session.getUserDetails().get(key_password);
            User loginUser = validateLogin(loggedUsername, loggedPassword);
            int loginUserId = loginUser.getUserId();
            setExtraData(loginUserId, loginUser.getUserName());
        }

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loginUsername = textViewUserName.getText().toString();
                final String loginPassword = textViewPassword.getText().toString();
                if(toast != null)
                    toast.cancel();
                if(loginUsername.isEmpty() || loginPassword.isEmpty()){
                    toast = Toast.makeText(getApplicationContext(), "Boş alanları doldurun!", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    RepoDatabaseUser databaseUser = RepoDatabaseUser.getInstance(getApplicationContext());
                    final UserDao userDao = databaseUser.getUserDao();
                    User user = userDao.loginAuthentication(loginUsername, loginPassword);
                    if (user == null) {
                        toast = Toast.makeText(getApplicationContext(), "Kullanıcı adı veya şifre yanlış!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Giriş Başarılı!", Toast.LENGTH_SHORT);
                        toast.show();
                        session.createLoginSession(user.getUserName(), user.getUserPassword());
                        setExtraData(user.getUserId(), user.getUserName());
                    }
                }
            }
        });
    }

    public void setExtraData(int userId, String username){
        Intent intent = new Intent(LoginActivity.this, HomeScreenMapsActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    public User validateLogin(String loginUsername, String loginPassword){
        RepoDatabaseUser databaseUser = RepoDatabaseUser.getInstance(getApplicationContext());
        final UserDao userDao = databaseUser.getUserDao();
        User user = userDao.loginAuthentication(loginUsername, loginPassword);
        return user;
    }
}