package com.example.smartscheduler;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartscheduler.model.UserData;
import com.example.smartscheduler.util.BaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Signup extends AppCompatActivity {
    Spinner CreditSpinner, SemesterSpinner, DepartmentSpinner;
    ArrayAdapter<String> adapterCreditHours, adaptersemester, adapterdeapartment;
    ArrayList<String> CreditList = new ArrayList<>();
    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();
    LinearLayout teacherLayout, studentLayout;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference myref;
    private EditText editText_TeacherFullName, editText_TeacherEmail, editText_TeacherPassword, editText_TeacherConfirm_password;
    private EditText editText_StudentFullName,editText_StudentEmail,editText_StudentPassword, editText_StudentConfirm_password;
    private FirebaseAuth auth;
    private Uri filePath;
    String userType, semester, department;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        myref = FirebaseDatabase.getInstance().getReference();

        //Input Teacher
        editText_TeacherFullName = findViewById(R.id.full_name_input);
        editText_TeacherEmail = findViewById(R.id.email_input);
        editText_TeacherPassword = findViewById(R.id.password_input);
        editText_TeacherConfirm_password = findViewById(R.id.re_enter_password);

        //Input Student
        editText_StudentFullName = findViewById(R.id.full_name_input_st);
        editText_StudentEmail = findViewById(R.id.email_input_st);
        editText_StudentPassword = findViewById(R.id.password_input_st);
        editText_StudentConfirm_password = findViewById(R.id.re_enter_password_st);

        teacherLayout = findViewById(R.id.teacherLayout);
        studentLayout = findViewById(R.id.studentLayout);

        TextView alreadyRegistered_TextView = findViewById(R.id.newUserTextView);

        TextView img_Sign_Up = findViewById(R.id.signUpButton);

        CreditSpinner = findViewById(R.id.creditHourSpinner);
        SemesterSpinner = findViewById(R.id.semesterspinner);
        DepartmentSpinner = findViewById(R.id.depspinner);

        CreditList.add("Teacher");
        CreditList.add("Student");

        SemesterList.add("1");
        SemesterList.add("2");
        SemesterList.add("3");
        SemesterList.add("4");
        SemesterList.add("5");
        SemesterList.add("6");
        SemesterList.add("7");
        SemesterList.add("8");

        adapterCreditHours = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CreditList);
        adapterCreditHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreditSpinner.setAdapter(adapterCreditHours);

        adaptersemester = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SemesterList);
        adaptersemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adaptersemester);

        CreditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType = adapterView.getItemAtPosition(i).toString();
                if (userType.equals("Student")) {
                    studentLayout.setVisibility(View.VISIBLE);
                    teacherLayout.setVisibility(View.GONE);
                } else {
                    teacherLayout.setVisibility(View.VISIBLE);
                    studentLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterdeapartment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DepartmentList);
        adapterdeapartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapterdeapartment);

        DepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDepartments();

        img_Sign_Up.setOnClickListener(v -> {
            if (CreditSpinner.getSelectedItemPosition() == 0)
            {
                if (editText_TeacherFullName.getText().toString().trim().equals("")) {
                    editText_TeacherFullName.setError("Full Name is required");
                    editText_TeacherFullName.requestFocus();
                    return;
                }
                if (editText_TeacherEmail.getText().toString().trim().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_TeacherEmail.getText().toString().trim()).matches())
                {
                    editText_TeacherEmail.setError("Email is required");
                    editText_TeacherEmail.requestFocus();
                    return;
                }

                if (editText_TeacherConfirm_password.getText().toString().equals("")) {
                    editText_TeacherConfirm_password.setError("Password is required");
                    editText_TeacherConfirm_password.requestFocus();
                    return;
                }

                if (editText_TeacherConfirm_password.getText().toString().length() < 8) {
                    editText_TeacherConfirm_password.setError("Password length should be greater or equal to 8 characters");
                    editText_TeacherConfirm_password.requestFocus();
                    return;
                }

                if (!editText_TeacherPassword.getText().toString().equals(editText_TeacherConfirm_password.getText().toString())) {
                    editText_TeacherConfirm_password.setError("Password did not match with confirm password");
                    editText_TeacherConfirm_password.requestFocus();
                    return;
                }

                myref.child("Faculty").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String TEmail = dataSnapshot.child("EMAIL").getValue().toString();
                                if (editText_TeacherEmail.getText().toString().equals(TEmail)) {
                                    Toast.makeText(Signup.this, "Email Found", Toast.LENGTH_SHORT).show();
                                    String facID = dataSnapshot.getKey();

                                    new BaseUtil(Signup.this).SetLoginRole("Teacher");
                                    new BaseUtil(Signup.this).SetID(facID);

                                    pDialog = new SweetAlertDialog(Signup.this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Creating User ...");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    // userType = CreditSpinner.getSelectedItem().toString();

                                    //create user
                                    auth.createUserWithEmailAndPassword(editText_TeacherEmail.getText().toString().trim(), editText_TeacherPassword.getText().toString().trim())
                                            .addOnCompleteListener(Signup.this, task -> {
                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                if (!task.isSuccessful()) {
                                                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                    pDialog.setTitleText("Oops...");
                                                    pDialog.setContentText("Authentication failed , " + task.getException());
                                                    pDialog.setCancelable(true);
                                                    pDialog.show();
                                                } else {
                                                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    UserData user = new UserData(editText_TeacherFullName.getText().toString(), editText_TeacherEmail.getText().toString(), userType,facID, uid);
                                                    uploadDB(user);
                                                }
                                            });

                                } else {
                                    Toast.makeText(Signup.this, "Email Not Found", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            else {
                if (editText_StudentFullName.getText().toString().trim().equals("")) {
                    editText_StudentFullName.setError("Full Name is required");
                    editText_StudentFullName.requestFocus();
                    return;
                }

                if (editText_StudentEmail.getText().toString().trim().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_StudentEmail.getText().toString().trim()).matches())
                {
                    editText_StudentEmail.setError("Email is required");
                    editText_StudentEmail.requestFocus();
                    return;
                }

                if (editText_StudentPassword.getText().toString().equals("")) {
                    editText_StudentPassword.setError("Password is required");
                    editText_StudentPassword.requestFocus();
                    return;
                }

                if (editText_StudentConfirm_password.getText().toString().length() < 8) {
                    editText_StudentConfirm_password.setError("Password length should be greater or equal to 8 characters");
                    editText_StudentConfirm_password.requestFocus();
                    return;
                }

                if (!editText_StudentPassword.getText().toString().equals(editText_StudentConfirm_password.getText().toString())) {
                    editText_StudentConfirm_password.setError("Password did not match with confirm password");
                    editText_StudentConfirm_password.requestFocus();
                    return;
                }

                pDialog = new SweetAlertDialog(Signup.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Creating User ...");
                pDialog.setCancelable(false);
                pDialog.show();

                //create user
                auth.createUserWithEmailAndPassword(editText_StudentEmail.getText().toString().trim(), editText_StudentPassword.getText().toString().trim())
                        .addOnCompleteListener(Signup.this, task -> {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText("Oops...");
                                pDialog.setContentText("Authentication failed , " + task.getException());
                                pDialog.setCancelable(true);
                                pDialog.show();
                            } else {
                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                new BaseUtil(Signup.this).SetLoginRole("Student");
                                new BaseUtil(Signup.this).SetID(uid);
                                new BaseUtil(Signup.this).SetDepartment(DepartmentSpinner.getSelectedItem().toString());
                                new BaseUtil(Signup.this).SetSemester(SemesterSpinner.getSelectedItem().toString());

                                UserData user = new UserData(editText_StudentFullName.getText().toString(), editText_StudentEmail.getText().toString(),
                                        userType,SemesterSpinner.getSelectedItem().toString(),DepartmentSpinner.getSelectedItem().toString(), uid);
                                uploadDB(user);
                            }
                        });
            }
        });
        alreadyRegistered_TextView.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            this.finish();
        });

    }

    private void uploadDB(UserData user) {
        pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Adding User Information to Database ...");
        pDialog.setCancelable(false);
        pDialog.show();

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("AppUsers").child("Users");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            databaseReference.child(firebaseUser.getUid()).setValue(user);
        }

        pDialog.dismissWithAnimation();
        if (userType.equals("Teacher")) {
            new BaseUtil(this).SetLoginRole("Teacher");
            startActivity(new Intent(this, TeacherActivity.class));
        } else {
            new BaseUtil(this).SetLoginRole("Student");
            startActivity(new Intent(this, StudentActivity.class));
        }
        finish();
    }

    private void getDepartments() {
        myref.child("Departments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DepartmentList.add(dataSnapshot.child("NAME").getValue().toString());
                }
                DepartmentSpinner.setAdapter(adapterdeapartment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Login.class));
    }
}