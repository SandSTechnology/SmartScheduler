package com.example.smartscheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartscheduler.util.BaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    BaseUtil preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = new BaseUtil(SplashActivity.this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            if (preferenceManager.getLoginRole().equals("Teacher")) {
                Intent intent = new Intent(SplashActivity.this, TeacherActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, StudentActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        } else {

            if (!preferenceManager.isWelcomeScreenShown()) {
                Intent intent = new Intent(SplashActivity.this, WelcomeScreenActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                this.finish();
            }
        }
    }
}