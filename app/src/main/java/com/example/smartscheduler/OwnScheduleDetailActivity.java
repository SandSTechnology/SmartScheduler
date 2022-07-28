package com.example.smartscheduler;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class OwnScheduleDetailActivity extends AppCompatActivity {
    EditText etDay;
    EditText etStartingTime;
    EditText etEndingTime;
    EditText etCreditHour;

    Spinner CourseSpinner;
    Spinner RoomSpinner;
    Spinner SemesterSpinner;
    Spinner DepartmentSpinner;

    Button updateNewSchedule;

    ArrayAdapter<String> adapterCourse, adapterRoom, adapterSemester, adapterDepartment;

    DatabaseReference myRef;
    FirebaseAuth mAuth;

    ArrayList<String> CourseList = new ArrayList<>();
    ArrayList<String> RoomList = new ArrayList<>();
    ArrayList<String> SemesterList = new ArrayList<>();
    ArrayList<String> DepartmentList = new ArrayList<>();

    String day;
    String creditHour;
    String startingTime;
    String endingTime;
    String course;
    String room;
    String department;
    String semester;

    TextView facultyName ;

    ScheduleModel scheduleModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_schedule_detail);

        scheduleModel = (ScheduleModel) getIntent().getSerializableExtra("ScheduleModel"); 
        
        CourseList.add("COURSE 1");
        CourseList.add("COURSE 2");
        CourseList.add("COURSE 3");
        CourseList.add("COURSE 4");
        CourseList.add("COURSE 5");
        CourseList.add("COURSE 6");

        RoomList.add("11");
        RoomList.add("12");
        RoomList.add("13");
        RoomList.add("14");
        RoomList.add("15");

        DepartmentList.add("IT");
        DepartmentList.add("BBA");
        DepartmentList.add("BSM");
        DepartmentList.add("LAW");
        DepartmentList.add("BBA");

        SemesterList.add("1");
        SemesterList.add("2");
        SemesterList.add("3");
        SemesterList.add("4");
        SemesterList.add("5");
        SemesterList.add("6");
        SemesterList.add("7");
        SemesterList.add("8");

        etDay = findViewById(R.id.editText_Day);
        etCreditHour = findViewById(R.id.editText_CreditHour);
        etStartingTime = findViewById(R.id.editText_StartingTime);
        etEndingTime = findViewById(R.id.editText_EndingTime);

        facultyName = findViewById(R.id.facultyName);
        CourseSpinner = findViewById(R.id.allCoursesSpinner);
        RoomSpinner = findViewById(R.id.allRoomSpinner);
        SemesterSpinner = findViewById(R.id.allSemesterSpinner);
        DepartmentSpinner = findViewById(R.id.allDepartmentSpinner);

        adapterCourse = new ArrayAdapter<>(OwnScheduleDetailActivity.this, android.R.layout.simple_spinner_item, CourseList);
        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CourseSpinner.setAdapter(adapterCourse);

        adapterSemester = new ArrayAdapter<>(OwnScheduleDetailActivity.this, android.R.layout.simple_spinner_item, SemesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SemesterSpinner.setAdapter(adapterSemester);

        adapterDepartment = new ArrayAdapter<>(OwnScheduleDetailActivity.this, android.R.layout.simple_spinner_item, DepartmentList);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentSpinner.setAdapter(adapterDepartment);

        adapterRoom = new ArrayAdapter<>(OwnScheduleDetailActivity.this, android.R.layout.simple_spinner_item, RoomList);
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoomSpinner.setAdapter(adapterRoom);

        setData();

        updateNewSchedule = findViewById(R.id.update_Schedule_Button);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        updateNewSchedule.setOnClickListener(v -> {
            day = etDay.getText().toString().toLowerCase(Locale.ROOT).trim();
            creditHour = etCreditHour.getText().toString().toLowerCase(Locale.ROOT).trim();
            startingTime = etStartingTime.getText().toString().toLowerCase(Locale.ROOT).trim();
            endingTime = etEndingTime.getText().toString().toLowerCase(Locale.ROOT).trim();
            course = CourseSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);
            room = RoomSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);
            department = DepartmentSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);
            semester = SemesterSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);

            if (day.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Add Day", Toast.LENGTH_SHORT).show();
                return;
            }
            if (creditHour.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Add Credit Hour", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startingTime.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Add Starting Time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endingTime.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Add Ending Time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (course.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Select Course", Toast.LENGTH_SHORT).show();
                return;
            }
            if (room.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Select Room", Toast.LENGTH_SHORT).show();
                return;
            }
            if (semester.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Select Semester", Toast.LENGTH_SHORT).show();
                return;
            }
            if (department.equalsIgnoreCase("")) {
                Toast.makeText(OwnScheduleDetailActivity.this, "Select Department", Toast.LENGTH_SHORT).show();
                return;
            }

            CheckOldData(day, creditHour, startingTime, endingTime, course, room, department, semester);
        });
    }

    private void setData() {
        etStartingTime.setText(scheduleModel.getS_TIME());
        etEndingTime.setText(scheduleModel.getE_TIME());
        etDay.setText(scheduleModel.getDAY());
        etCreditHour.setText(scheduleModel.getCREDIT_HOUR());
        facultyName.setText(scheduleModel.getFACULTY());

        int posSem = adapterSemester.getPosition(scheduleModel.getSEMESTER().toUpperCase());
            SemesterSpinner.setSelection(posSem);
        int posDep = adapterDepartment.getPosition(scheduleModel.getDEPARTMENT().toUpperCase());
            DepartmentSpinner.setSelection(posDep);
        int posCourse = adapterCourse.getPosition(scheduleModel.getCOURSE().toUpperCase());
            CourseSpinner.setSelection(posCourse);
        int posRoom = adapterRoom.getPosition(scheduleModel.getROOM().toUpperCase());
            RoomSpinner.setSelection(posRoom);
    }

    private void SaveDB() {
        DatabaseReference newRef = myRef.child("Schedule").child(scheduleModel.getID() + "");

        Map<String,Object> map = new HashMap<>();

        map.put("DAY",day);
        map.put("S_TIME",startingTime);
        map.put("E_TIME",endingTime);
        map.put("COURSE",course);
        map.put("ROOM",room);
        map.put("SEMESTER",semester);
        map.put("CREDIT_HOUR",creditHour);
        map.put("DEPARTMENT",department);

        newRef.updateChildren(map);

        Toast.makeText(OwnScheduleDetailActivity.this, "Schedule Updated", Toast.LENGTH_SHORT).show();
    }

    private void CheckOldData(String day, String creditHour, String startingTime,
                              String endingTime, String course, String room,
                              String department, String semester) {
        DatabaseReference newRef = myRef.getRef();
        newRef.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;
                for (DataSnapshot s: snapshot.getChildren()) {
                    String DAY = s.child("DAY").getValue(String.class).toLowerCase(Locale.ROOT);
                    String S_TIME = s.child("S_TIME").getValue(String.class).toLowerCase(Locale.ROOT);
                    String E_TIME = s.child("E_TIME").getValue(String.class).toLowerCase(Locale.ROOT);
                    String COURSE = s.child("COURSE").getValue(String.class).toLowerCase(Locale.ROOT);
                    String ROOM = s.child("ROOM").getValue(String.class).toLowerCase(Locale.ROOT);
                    String SEMESTER = s.child("SEMESTER").getValue(String.class).toLowerCase(Locale.ROOT);
                    String CREDIT_HOUR = s.child("CREDIT_HOUR").getValue(String.class).toLowerCase(Locale.ROOT);
                    String DEPARTMENT = s.child("DEPARTMENT").getValue(String.class).toLowerCase(Locale.ROOT);

                    if (Objects.equals(DAY, day) && Objects.equals(S_TIME, startingTime)
                            && Objects.equals(E_TIME, endingTime) && Objects.equals(COURSE, course)
                            && Objects.equals(ROOM, room)
                            && Objects.equals(SEMESTER, semester) && Objects.equals(CREDIT_HOUR, creditHour)
                            && Objects.equals(DEPARTMENT, department))
                    {
                        Toast.makeText(OwnScheduleDetailActivity.this, "Already added Time Table to this Slot !", Toast.LENGTH_LONG).show();
                        exist = true ;
                        break;
                    }
                }
                if (!exist)
                    SaveDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}