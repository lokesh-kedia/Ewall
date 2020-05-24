package com.lokeshkedia.e_wall;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class Main2Activity extends AppCompatActivity {
    SharedPref sharedPref;
    DrawerLayout drawerLayout;
    private DemoFragmentStateAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = findViewById(R.id.pager);
        /*sharedPref = new SharedPref(this);
        if (sharedPref.loadFirstTimeState() == true) {
            startActivity(new Intent(Main2Activity.this, IntroActivity.class));
            finish();
        }*/
        adapter = new DemoFragmentStateAdapter(getSupportFragmentManager());

        drawerLayout = findViewById(R.id.drawer);
        mViewPager.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.home);
        tabLayout.getTabAt(1).setIcon(R.mipmap.fire);
        tabLayout.getTabAt(2).setIcon(R.mipmap.diagram);*/
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.bot_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.bot_trend);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.bot_categories);
                        break;
                   /* case 3:
                        bottomNavigationView.setSelectedItemId(R.id.bot_photographers);
                        break;*/
                   /* case 4:
                        bottomNavigationView.setSelectedItemId(R.id.bot_painters);
                        break;*/
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.bot_home);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bot_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.bot_trend:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.bot_categories:
                        mViewPager.setCurrentItem(2);
                        break;
                   /* case R.id.bot_photographers:
                        mViewPager.setCurrentItem(3);
                        break;*/
                   /* case R.id.bot_painters:
                        mViewPager.setCurrentItem(4);
                        break;*/
                    default:
                        mViewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        NavigationView navigationView = findViewById(R.id.navigation);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        /*MobileAds.initialize(this, "ca-app-pub-5314021439844328~3628546561");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                FirstFragment newFragment;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    /*case R.id.nav_bus:
                        startActivity(new Intent(Main2Activity.this, IntroActivity.class));
                        break;*/
                    case R.id.nav_gen:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_health:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_science:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_saved:
                        startActivity(new Intent(Main2Activity.this, About.class));
                        break;
                    case R.id.nav_tech:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                        break;
                    case R.id.nav_sports:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nE-wall\nDownload the application For awesome wallpapers.\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    /*case R.id.nav_top:
                        startActivity(new Intent(Main2Activity.this, VideoWall.class));
                        break;
                    case R.id.nav_blog:
                        startActivity(new Intent(Main2Activity.this, Register.class));
                        break;*/

                }


                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
