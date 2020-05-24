package com.lokeshkedia.e_wall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImgSlider extends AppCompatActivity {

    ViewPager viewPager;
    InterstitialAd mInterstitialAd;
    RequestQueue rq;
    List<Wall> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    String request_url = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q= &image_type=photo&per_page=200";
    // LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_slider);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();
        mInterstitialAd = new InterstitialAd(ImgSlider.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        //AdView mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        sliderImg = new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);

        //sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        sendRequest();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position % 5 == 0 && position != 0) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onPageSelected(int position) {

               /* for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void sendRequest() {

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
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

                }


                viewPagerAdapter = new ViewPagerAdapter(sliderImg, ImgSlider.this);

                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

}