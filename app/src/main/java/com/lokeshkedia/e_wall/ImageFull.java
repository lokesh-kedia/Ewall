package com.lokeshkedia.e_wall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageFull extends AppCompatActivity {
    String img, type, place;
    int pos;
    int STORAGE_PERMISSION_CODE = 1;
    FloatingActionButton shareWall;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    RequestQueue rq;
    List<Wall> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    String request_url = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q= &image_type=photo&per_page=200";
    ViewPager viewPager;
    String query;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    List<Wall> walls = new ArrayList<>();
    Adapter adapter1;
    String query1;
    private String JsonUrl = "https://pixabay.com/api/videos/?key=11692914-b271cf931a0e5e55eed743e36&q= &safesearch=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_full);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        MobileAds.initialize(this, "ca-app-pub-5314021439844328~3628546561");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

//        ca-app-pub-5314021439844328/9845823750

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5314021439844328/7411232100");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Bundle b = getIntent().getExtras();
        img = b.getString("img");
        query = b.getString("query");
        place = b.getString("place");
        request_url = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q=" + query + "&image_type=photo&per_page=200";
        pos = b.getInt("pos");
        type = b.getString("type");
        /*ImageView imageView = findViewById(R.id.image_full);
        Glide.with(this).load(img).into(imageView);*/

        sliderImg = new ArrayList<>();
        // Toast.makeText(this, String.valueOf(pos), Toast.LENGTH_LONG).show();
        viewPager = findViewById(R.id.viewPager);
        viewPager.setCurrentItem(pos);
        //sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        if (type.equals("photo")) {
            if (!place.equals("firebase")) {
                sliderImg = (List<Wall>) getIntent().getSerializableExtra("firewall");
                sendRequest();
            } else {
                sliderImg = (List<Wall>) getIntent().getSerializableExtra("firewall");
                loadFirebase();

            }
        } else
            loadVideo();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                img = sliderImg.get(position).getoriginal();
                if (position % 5 == 0 && position != 0) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        shareWall = findViewById(R.id.share);
        shareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWall();
            }

        });
        //RelativeLayout relativeLayout=findViewById(R.id.ifrel);
        Toast.makeText(this, "Swipe left to change images", Toast.LENGTH_LONG).show();
    }

    private void shareWall() {
        if (checkWriteExternalPermission() == true) {
            Glide.with(ImageFull.this).asBitmap()
                    .load(img)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");
                            String shareMessage = "\nE-wall\nDownload the application For awesome wallpapers.\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                            share.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }

                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(share, "Share Image"));
                        }
                    });
        } else {
            requestStoragePermission();
        }
    }

    private void loadFirebase() {
        viewPagerAdapter = new ViewPagerAdapter(sliderImg, ImageFull.this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(pos);
    }

    public void sendRequest() {

       /* JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray wallArray = null;
                try {
                    wallArray = response.getJSONArray("hits");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < wallArray.length(); i++) {

                    Wall sliderUtils = new Wall();

                    JSONObject wallObj = null;
                    try {
                        wallObj = wallArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sliderUtils.setOriginal(wallObj.getString("largeImageURL"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderImg.add(sliderUtils);

                }*/


                viewPagerAdapter = new ViewPagerAdapter(sliderImg, ImageFull.this);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(pos);
/*
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);*/

    }

    public void setWall(View view) {
        if (checkWriteExternalPermission()) {
            final LoadToast lt = new LoadToast(this);
            lt.setText("Setting...");
            int mHeight = this.getResources().getDisplayMetrics().heightPixels;
            lt.setTranslationY(mHeight / 2);
            lt.setBorderColor(Color.BLACK);
            lt.setBorderWidthDp(4);
            //view.setVisibility(View.GONE);
            lt.show();
            Glide.with(this).asBitmap()
                    .load(img)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");
                            String shareMessage = "\nE-wall\nDownload the application For awesome wallpapers.\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                            share.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            mRewardedVideoAd.loadAd("ca-app-pub-5314021439844328/9845823750",
                                    new AdRequest.Builder().build());
                            /*if (mRewardedVideoAd.isLoaded()) {
                                mRewardedVideoAd.show();
                            }*/
                            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                                @Override
                                public void onRewardedVideoAdLoaded() {
                                    mRewardedVideoAd.show();
                                    // Toast.makeText(ImageFull.this, "Loaded", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onRewardedVideoAdOpened() {
                                    // Toast.makeText(ImageFull.this, "Opened", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onRewardedVideoStarted() {
                                    // Toast.makeText(ImageFull.this, "Started", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onRewardedVideoAdClosed() {
                                    // Toast.makeText(ImageFull.this, "Closed", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onRewarded(RewardItem rewardItem) {

                                }

                                @Override
                                public void onRewardedVideoAdLeftApplication() {

                                }

                                @Override
                                public void onRewardedVideoAdFailedToLoad(int i) {
                                    // Toast.makeText(ImageFull.this, String.valueOf(i), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onRewardedVideoCompleted() {

                                }
                            });
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ImageFull.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                            lt.success();

                        }
                    });
        } else {
            requestStoragePermission();
        }
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

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                // shareWall();

            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkWriteExternalPermission() {
        boolean r;
        r = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return r;
    }

    public void loadVideo() {

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, JsonUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray wallArray = null;
                try {
                    wallArray = response.getJSONArray("hits");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < wallArray.length(); i++) {

                    Wall sliderUtils = new Wall();

                    JSONObject wallObj = null;
                    JSONObject wallArray2 = null;
                    try {
                        wallObj = wallArray.getJSONObject(i);
                        JSONObject wallArray1 = wallObj.getJSONObject("videos");
                        wallArray2 = wallArray1.getJSONObject("tiny");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sliderUtils.setOriginal(wallArray2.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderImg.add(sliderUtils);

                }


                viewPagerAdapter = new ViewPagerAdapter(sliderImg, ImageFull.this);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(pos);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);


    }

}
