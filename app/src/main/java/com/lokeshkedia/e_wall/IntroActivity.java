package com.lokeshkedia.e_wall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends AppCompatActivity {
    SharedPref sharedPref;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mViewPager = findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(this);
        mViewPager.setAdapter(adapterView);
        sharedPref = new SharedPref(this);
        sharedPref.setFirstTimeState(false);

    }

    public void skip(View view) {
        startActivity(new Intent(IntroActivity.this, Main2Activity.class));
        finish();
    }

    public void next(View view) {
        if (mViewPager.getCurrentItem() != 3)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        else {
            startActivity(new Intent(IntroActivity.this, Main2Activity.class));
            finish();
        }
    }
}
