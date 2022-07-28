package com.example.smartscheduler.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartscheduler.R;
import com.example.smartscheduler.model.TimeTableModel;
import com.example.smartscheduler.util.BaseUtil;
import com.example.smartscheduler.util.TimeSlots;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class WednesdayFragment extends Fragment {
    private ProgressBar loading;
    private final ArrayList<TimeTableModel> list = new ArrayList<>();
    private TextView SubjName1, SubjName2, SubjName3, SubjName4, SubjName5, SubjName6;
    private TextView Faculty1, Faculty2, Faculty3, Faculty4, Faculty5, Faculty6;

    public WednesdayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);
        SubjName1 = view.findViewById(R.id.Slot1SubName);
        SubjName2 = view.findViewById(R.id.Slot2SubName);
        SubjName3 = view.findViewById(R.id.Slot3SubName);
        SubjName4 = view.findViewById(R.id.Slot4SubName);
        SubjName5 = view.findViewById(R.id.Slot5SubName);
        SubjName6 = view.findViewById(R.id.Slot6SubName);

        Faculty1 = view.findViewById(R.id.Faculty1Name);
        Faculty2 = view.findViewById(R.id.Faculty2Name);
        Faculty3 = view.findViewById(R.id.Faculty3Name);
        Faculty4 = view.findViewById(R.id.Faculty4Name);
        Faculty5 = view.findViewById(R.id.Faculty5Name);
        Faculty6 = view.findViewById(R.id.Faculty6Name);

        loading = view.findViewById(R.id.loading);

        getTimeSlot();
        return view;
    }


    private void getTimeSlot() {
        loading.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        list.clear();
        databaseReference.child("Schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.setVisibility(View.VISIBLE);
                if (snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String MyID = new BaseUtil(getContext()).getID();
                        String MyRole = new BaseUtil(getContext()).getLoginRole();
                        String mDepartment = new BaseUtil(getContext()).getDepartment();
                        String mSemester = new BaseUtil(getContext()).getSemester();
                        String FacultyID = dataSnapshot.child("FACULTY_ID").getValue(String.class);

                        if (MyRole.equalsIgnoreCase("Student")) {
                            String department = Objects.requireNonNull(dataSnapshot.child("DEPARTMENT").getValue()).toString();
                            String semester = Objects.requireNonNull(dataSnapshot.child("SEMESTER").getValue()).toString();

                            if (department.equalsIgnoreCase(mDepartment) && semester.equalsIgnoreCase(mSemester)) {
                                String day = Objects.requireNonNull(dataSnapshot.child("DAY").getValue()).toString();
                                if (day.equals("WEDNESDAY")) {
                                    list.add(new TimeTableModel(Objects.requireNonNull(dataSnapshot.child("TIMESLOT").getValue()).toString(), dataSnapshot.child("COURSE").getValue().toString(), dataSnapshot.child("FACULTY").getValue().toString()));
                                }
                            }
                        } else {
                            if (Objects.equals(MyID, FacultyID)) {
                                String day = Objects.requireNonNull(dataSnapshot.child("DAY").getValue()).toString();
                                if (day.equals("WEDNESDAY")) {
                                    list.add(new TimeTableModel(Objects.requireNonNull(dataSnapshot.child("TIMESLOT").getValue()).toString(), dataSnapshot.child("COURSE").getValue().toString(), dataSnapshot.child("FACULTY").getValue().toString()));
                                }
                            }
                        }
                    }

                String Slot1 = TimeSlots.SLOT1.toString();
                String Slot2 = TimeSlots.SLOT2.toString();
                String Slot3 = TimeSlots.SLOT3.toString();
                String Slot4 = TimeSlots.SLOT4.toString();
                String Slot5 = TimeSlots.SLOT5.toString();
                String Slot6 = TimeSlots.SLOT6.toString();

                String MyRole = new BaseUtil(getContext()).getLoginRole();
                if (list.size() != 0)
                    for (int i = 0; i < list.size(); i++) {
                        TimeTableModel timeTableModel = list.get(i);

                        if (Slot1.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName1.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty1.setText(timeTableModel.getFaculty());
                            }
                        } else if (Slot2.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName2.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty2.setText(timeTableModel.getFaculty());
                            }
                        } else if (Slot3.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName3.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty3.setText(timeTableModel.getFaculty());
                            }
                        } else if (Slot4.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName4.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty4.setText(timeTableModel.getFaculty());
                            }
                        } else if (Slot5.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName5.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty5.setText(timeTableModel.getFaculty());
                            }
                        } else if (Slot6.equalsIgnoreCase(timeTableModel.getTimeslot())) {
                            SubjName6.setText(timeTableModel.getCourse());
                            if (MyRole.equalsIgnoreCase("Student")) {
                                Faculty6.setText(timeTableModel.getFaculty());
                            }
                        }
                    }

                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // getTimeSlot();
    }
}