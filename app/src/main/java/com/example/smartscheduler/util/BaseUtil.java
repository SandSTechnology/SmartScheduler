package com.example.smartscheduler.util;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class BaseUtil {
    public static String PREF_NAME="SmartScheduler_PREFERENCE_MANAGER";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String LOGIN_ROLE = "login_role";
    public static final String EMAIL = "email";
    public static final String MOBILE_NUMBER = "mobile_number";
    public static final String ID = "ID";
    private static final String IS_WELCOME_SCREEN_SHOWN = "is_welcome_shown";
    public static final String DEPARTMENT = "Department";
    public static final String SEMESTER = "Semester";
    private Context context;
    //private SharedPreferences preferences;

    public BaseUtil(Context context)
    {
        this.context=context;
        //preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
    }

    public void seWelcomeScreenShown(boolean isWelcomeScreenShown) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_WELCOME_SCREEN_SHOWN, isWelcomeScreenShown);
        editor.apply();
    }

    public boolean isWelcomeScreenShown() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getBoolean(IS_WELCOME_SCREEN_SHOWN, false);
    }

    public void ClearPreferences()
    {
        String DeviceToken = getDeviceToken();
        context.getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit().clear().apply();
        SetDeviceToken(DeviceToken);
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public String getLoginRole(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(LOGIN_ROLE,"");
    }

    public void SetLoginRole(String loginRole){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN_ROLE,loginRole);
        editor.apply();
    }

    public void SetDeviceToken(String tokenId){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_TOKEN,tokenId);
        editor.apply();
    }

    public String getDeviceToken(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(DEVICE_TOKEN,"");
    }

    public void SetID(String id){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ID,id);
        editor.apply();
    }

    public String getID(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(ID,"");
    }

    public void SetEmail(String email){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL,email);
        editor.apply();
    }

    public String getEmail(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(EMAIL,"");
    }

    public void SetMobileNumber(String mobile){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NUMBER,mobile);
        editor.apply();
    }

    public String getMobileNumber(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(MOBILE_NUMBER,"");
    }

    public void SetDepartment(String department){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEPARTMENT,department);
        editor.apply();
    }

    public String getDepartment(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(DEPARTMENT,"");
    }

    public void SetSemester(String semester){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SEMESTER,semester);
        editor.apply();
    }

    public String getSemester(){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        return preferences.getString(SEMESTER,"");
    }
}
