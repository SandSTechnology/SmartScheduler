package com.example.smartscheduler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.smartscheduler.Adapter.TimeTableAdapter;
import com.example.smartscheduler.model.TimeTableWithFacultyModel;
import com.example.smartscheduler.util.BaseUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StudentActivity extends AppCompatActivity implements PDFUtility.OnDocumentClose {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private static final String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    File file;
    String path = "";
    private ProgressBar loading;
    private final ArrayList<TimeTableWithFacultyModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String token = new BaseUtil(this).getDeviceToken();
            if (token != null && !token.equals(""))
                dRef.child("DeviceTokens").child("Students").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token")
                        .setValue(token);
        } else {
            startActivity(new Intent(this, Login.class));
        }

        //Side bar
        drawerLayout = findViewById(R.id.drawerLayout);
        loading = findViewById(R.id.loading);
        ActionBarDrawerToggle drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        final NavigationView navigationView = findViewById(R.id.NavigationViewMainActivity);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            int id = item.getItemId();
            if (id == R.id.home_button) {
                // startActivity(new Intent(TeacherActivity.this,TeacherActivity.class));
                //  finish();
                return true;
            } else if (id == R.id.about_app_button) {

                return true;
            } else if (id == R.id.user_feedback) {

                // startActivity(new Intent(getApplicationContext(),Analyser.class));
                return true;
            } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                new BaseUtil(this).ClearPreferences();
                startActivity(new Intent(StudentActivity.this, Login.class));
                finish();
                return true;
            } else
                return false;
        });
//Side bar end

        final Toolbar toolbar = findViewById(R.id.default_toolBar);
        setSupportActionBar(toolbar);

        final ActionBar DActionBar = getSupportActionBar();
        if (DActionBar != null) {
            DActionBar.setDisplayHomeAsUpEnabled(true);
            DActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            DActionBar.setTitle("Student");
        }

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        TimeTableAdapter myPagerAdapter = new TimeTableAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(this);
                tabTextView.setGravity(Gravity.CENTER);
                tab.setCustomView(tabTextView);
                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.setText(tab.getText());
                if (i == 0) {
                    tabTextView.setTextColor(getResources().getColor(R.color.white));
                    tabTextView.setTextSize(16);
                    tabTextView.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.gray_btn_bg_color));
                    tabTextView.setTextSize(14);
                    tabTextView.setTypeface(Typeface.DEFAULT);
                }

            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) tabViewChild).setTextSize(16);
                        ((TextView) tabViewChild).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(tab.getPosition());
                int tabChildCount = vgTab.getChildCount();
                for (int i = 0; i < tabChildCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.gray_btn_bg_color));
                        ((TextView) tabViewChild).setTextSize(14);
                        ((TextView) tabViewChild).setTypeface(Typeface.DEFAULT);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        verifyStoragePermissionsOnStartup(this);
        checkInternet();
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED) {
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED)
                Toast.makeText(this, "Internet is not connected", Toast.LENGTH_LONG).show();
        }
    }

    private void getTimeTable(String path){
        loading.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        list.clear();
        databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.setVisibility(View.VISIBLE);
                if (snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String MyID = new BaseUtil(StudentActivity.this).getID();
                        String MyRole = new BaseUtil(StudentActivity.this).getLoginRole();
                        String mDepartment = new BaseUtil(StudentActivity.this).getDepartment();
                        String mSemester = new BaseUtil(StudentActivity.this).getSemester();
                        String FacultyID = dataSnapshot.child("FACULTY_ID").getValue(String.class);

                        if (MyRole.equalsIgnoreCase("Student")) {
                            String department = Objects.requireNonNull(dataSnapshot.child("DEPARTMENT").getValue()).toString();
                            String semester = Objects.requireNonNull(dataSnapshot.child("SEMESTER").getValue()).toString();

                            if (department.equalsIgnoreCase(mDepartment) && semester.equalsIgnoreCase(mSemester)) {
                                list.add(new TimeTableWithFacultyModel(Objects.requireNonNull(dataSnapshot.child("TIMESLOT").getValue()).toString(), dataSnapshot.child("COURSE").getValue().toString(),dataSnapshot.child("FACULTY").getValue().toString(),dataSnapshot.child("DAY").getValue().toString()));
                            }
                        } else {
                            if (Objects.equals(MyID, FacultyID)) {
                                list.add(new TimeTableWithFacultyModel(Objects.requireNonNull(dataSnapshot.child("TIMESLOT").getValue()).toString(), dataSnapshot.child("COURSE").getValue().toString(),dataSnapshot.child("FACULTY").getValue().toString(),dataSnapshot.child("DAY").getValue().toString()));
                            }
                        }
                    }

                if (list.size()!=0)
                {
                    try {
                        PDFUtility.createPdf(getApplicationContext(), StudentActivity.this, path, true,list);
                    } catch (Exception e) {
                        Toast.makeText(StudentActivity.this, "Error Creating Pdf", Toast.LENGTH_SHORT).show();
                    }
                    showPdfSuccessDialog();
                }
                else
                {
                    new SweetAlertDialog(StudentActivity.this).setTitleText("EMPTY").setContentText("Timetable not added by The Admin for You").setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }

                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // verifying if storage permission is given or not
    public void verifyStoragePermissionsOnStartup(Activity activity) {
        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE);
        }
        //else
        //{
        //View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        //LetsCreatePDF(rootView);
        //}
    }

    // verifying if storage permission is given or not
    public void verifyStoragePermissions(Activity activity) {
        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE);
        }
        else
        {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            LetsCreatePDF(rootView);
        }
    }

    private void LetsCreatePDF(View view) {
        String path = "";

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + currentDateAndTime + "-Timetable.pdf";
        } else {
            path = Environment.getExternalStorageDirectory() + "/" + currentDateAndTime + "-Timetable.pdf";
        }
        //file = new File(path);

        this.path = path;

        try {
            getTimeTable(path);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Error Creating Pdf", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPdfSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.pdf_custom_dialog, null);
        TextView FolderName = view.findViewById(R.id.FolderName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            FolderName.setText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath());
        } else {
            FolderName.setText(Environment.getExternalStorageDirectory() .getPath());
        }

        builder.setView(view);
        builder.setCancelable(true);
        Button sharepdfbtn = view.findViewById(R.id.btnShare);
        ImageView closebtn = view.findViewById(R.id.close);
        AlertDialog alertDialog = builder.create();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        sharepdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(path);
                Uri uri = FileProvider.getUriForFile(StudentActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                //  Uri imageUri = Uri.parse(MainActivity.imageurl.getAbsolutePath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //Target whatsapp:
                // shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(Intent.createChooser(shareIntent, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StudentActivity.this, "App Not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button viewbtn = view.findViewById(R.id.btnView);
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(path);
                Uri uri = FileProvider.getUriForFile(StudentActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                //  Uri imageUri = Uri.parse(MainActivity.imageurl.getAbsolutePath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_VIEW);
                //Target whatsapp:
                // shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(uri, "application/pdf");

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StudentActivity.this, "No Pdf Viewer install", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialog.show();

    }

    @Override
    public void onPDFDocumentClose(File file) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.download_timetable) {
            verifyStoragePermissions(this);
            return true;
        } else if (item.getItemId() == R.id.share_timetable) {
            verifyStoragePermissions(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("Do you want to Exit App?")
                .setConfirmText("Exit")
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    this.finish();
                    super.onBackPressed();
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
    }
}