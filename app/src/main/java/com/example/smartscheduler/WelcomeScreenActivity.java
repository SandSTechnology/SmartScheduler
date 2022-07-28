package com.example.smartscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartscheduler.Adapter.WelcomeScreenPageAdapter;
import com.example.smartscheduler.util.BaseUtil;

public class WelcomeScreenActivity extends AppCompatActivity {
    BaseUtil preferenceManager;
    private ViewPager myViewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private Button btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        myViewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        preferenceManager = new BaseUtil(WelcomeScreenActivity.this);
        preferenceManager.seWelcomeScreenShown(true);

        makeTransparentStatusBar();

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        btnNext.setOnClickListener(v -> {
            int CurrentPosition = myViewPager.getCurrentItem() + 1;
            if (CurrentPosition < layouts.length) {
                myViewPager.setCurrentItem(CurrentPosition);
            } else {
                StartLoginActivity();
            }
        });

        btnSkip.setOnClickListener(v -> StartLoginActivity());

        WelcomeScreenPageAdapter welcomeScreenPageAdapter = new WelcomeScreenPageAdapter(layouts, WelcomeScreenActivity.this);
        myViewPager.setAdapter(welcomeScreenPageAdapter);
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == layouts.length - 1) {
                    btnNext.setText(R.string.start);
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText(R.string.next);
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotsStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotsStatus(0);
    }

    private void setDotsStatus(int page) {
        dotsLayout.removeAllViews();
        TextView[] dots = new TextView[layouts.length];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(WelcomeScreenActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            switch (page) {
                case 0:
                    dots[0].setTextColor(getResources().getColor(R.color.dot_dark_screen1));
                    dots[1].setTextColor(getResources().getColor(R.color.dot_light_screen1));
                    dots[2].setTextColor(getResources().getColor(R.color.dot_light_screen1));
                    dots[3].setTextColor(getResources().getColor(R.color.dot_light_screen1));
                    break;
                case 1:
                    dots[0].setTextColor(getResources().getColor(R.color.dot_light_screen2));
                    dots[1].setTextColor(getResources().getColor(R.color.dot_dark_screen2));
                    dots[2].setTextColor(getResources().getColor(R.color.dot_light_screen2));
                    dots[3].setTextColor(getResources().getColor(R.color.dot_light_screen2));
                    break;
                case 2:
                    dots[0].setTextColor(getResources().getColor(R.color.dot_light_screen3));
                    dots[1].setTextColor(getResources().getColor(R.color.dot_light_screen3));
                    dots[2].setTextColor(getResources().getColor(R.color.dot_dark_screen3));
                    dots[3].setTextColor(getResources().getColor(R.color.dot_light_screen3));
                    break;
                case 3:
                    dots[0].setTextColor(getResources().getColor(R.color.dot_light_screen4));
                    dots[1].setTextColor(getResources().getColor(R.color.dot_light_screen4));
                    dots[2].setTextColor(getResources().getColor(R.color.dot_light_screen4));
                    dots[3].setTextColor(getResources().getColor(R.color.dot_dark_screen4));
                    break;
            }
        }
    }

    private void makeTransparentStatusBar() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StartLoginActivity() {
        Intent intent = new Intent(WelcomeScreenActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}