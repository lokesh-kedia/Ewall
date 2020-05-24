package com.lokeshkedia.e_wall;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void open(View view) {
        switch (view.getId()) {
            case R.id.insta:
                Intent i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://www.instagram.com/lokesh1agarwal/?hl=en");
                startActivity(i);
                break;
            case R.id.facebook:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://www.facebook.com/krrish.lokesh");
                startActivity(i);
                break;
            case R.id.github:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://github.com/lokesh1agarwal");
                startActivity(i);
                break;
            case R.id.gmail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "lokesh1agawal@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Ewall");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.linkedin:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://www.linkedin.com/in/lokesh-kedia-14ab0a148/");
                startActivity(i);
                break;
            case R.id.pixabay:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://www.pixabay.com");
                startActivity(i);
                break;
            case R.id.flaticon:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://www.flaticon.com");
                startActivity(i);
                break;
            case R.id.upload:
                i = new Intent(About.this, WebActivity.class);
                i.putExtra("url", "https://lokesh1agarwal.wixsite.com/to-let/copyright");
                startActivity(i);
                break;

        }
    }

    public void openprivacy(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lokesh1agarwal.wixsite.com/to-let/privacy-policy"));
        startActivity(browserIntent);
    }
}