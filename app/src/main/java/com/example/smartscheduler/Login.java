package com.example.smartscheduler;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartscheduler.util.BaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    EditText mEmailEditText, mPasswordEditText;
    TextView forgotPasswordTextView, LoginTextView;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    String userType = "";
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference();
        TextView alreadyRegistered_TextView = findViewById(R.id.newUserTextView);
        //Initialization
        mEmailEditText = findViewById(R.id.username_input);
        mPasswordEditText = findViewById(R.id.pass);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);
        LoginTextView = findViewById(R.id.loginButton);

        forgotPasswordTextView.setOnClickListener(v -> StartForgotPasswordActivity());

        alreadyRegistered_TextView.setOnClickListener(v -> {
            startActivity(new Intent(this, Signup.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            this.finish();
        });
        // Login with Email and Password
        LoginTextView.setOnClickListener(v -> {

            if (ValidateEmailAndPassword()) {
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Login in process ...");
                pDialog.setCancelable(false);
                pDialog.show();

                String email = Objects.requireNonNull(mEmailEditText.getText()).toString().trim();
                String password = Objects.requireNonNull(mPasswordEditText.getText()).toString().trim();

                SearchEmailID(email, password);
            }

        });
    }

    private void SearchEmailID(String email, String password) {
        myRef.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String Email = "";
                        String ID = areaSnapshot.getKey();
                        if (areaSnapshot.child("EMAIL").exists())
                            Email = areaSnapshot.child("EMAIL").getValue(String.class);
                        if (Objects.requireNonNull(Email).equalsIgnoreCase(email)) {
                            String FacultyID = areaSnapshot.getKey();

                            new BaseUtil(Login.this).SetLoginRole("Teacher");
                            new BaseUtil(Login.this).SetEmail(FacultyID);

                            break;
                        }
                    }

                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, task -> {
                            if (task.isSuccessful()) {
                                String uid = mAuth.getCurrentUser().getUid();
                                myRef.child("AppUsers").child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String cat = dataSnapshot.child("usertype").getValue().toString();

                                            if (cat.equals("Teacher")) {
                                                String faculty = "";
                                                pDialog.dismiss();
                                                if (dataSnapshot.child("facultyID").exists())
                                                    faculty = dataSnapshot.child("facultyID").getValue().toString();

                                                new BaseUtil(Login.this).SetLoginRole("Teacher");
                                                new BaseUtil(Login.this).SetID(faculty);

                                                Intent intent = new Intent(Login.this, TeacherActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (cat.equals("Student")) {
                                                pDialog.dismiss();
                                                if (dataSnapshot.child("semester").exists())
                                                    new BaseUtil(Login.this).SetSemester(dataSnapshot.child("semester").getValue().toString());
                                                if (dataSnapshot.child("department").exists())
                                                    new BaseUtil(Login.this).SetDepartment(dataSnapshot.child("department").getValue().toString());
                                                new BaseUtil(Login.this).SetLoginRole("Student");
                                                Intent intent = new Intent(Login.this, StudentActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "User not found!", Toast.LENGTH_SHORT).show();
                                            pDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                // there was an error
                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitle("Oops...");
                                pDialog.setContentText("Authentication failed! User not registered");
                                pDialog.setCancelable(true);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean ValidateEmailAndPassword() {
        String emailAddress = Objects.requireNonNull(mEmailEditText.getText()).toString().trim();
        if (mEmailEditText.getText().toString().equals("")) {
            mEmailEditText.setError("please enter email address");
            mEmailEditText.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            mEmailEditText.setError("please enter valid email address");
            mEmailEditText.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(mPasswordEditText.getText()).toString().equals("")) {
            mPasswordEditText.setError("please enter password");
            mPasswordEditText.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(mPasswordEditText.getText()).toString().length() < 8) {
            mPasswordEditText.setError("password minimum contain 8 character");
            mPasswordEditText.requestFocus();
            return false;
        }
        return true;
    }

    private void StartForgotPasswordActivity() {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_enter_back, R.anim.animation_back_leave);
        finish();
    }
}