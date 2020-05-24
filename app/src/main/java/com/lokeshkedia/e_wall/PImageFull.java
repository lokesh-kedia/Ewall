package com.lokeshkedia.e_wall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class PImageFull extends AppCompatActivity {
    String img, owner, key;
    int STORAGE_PERMISSION_CODE = 1;
    FloatingActionButton shareWall;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    private InterstitialAd mInterstitialAd;
    long fcount = 0;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pimage_full);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5314021439844328/7411232100");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Bundle b = getIntent().getExtras();
        img = b.getString("img");
        owner = b.getString("owner");
        key = b.getString("key");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(owner).child("Profile");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String photo = dataSnapshot.child("Img").getValue(String.class);
                String name = dataSnapshot.child("Name").getValue(String.class);
                ImageView imageView = findViewById(R.id.profile_image);
                Glide.with(PImageFull.this).load(photo).into(imageView);
                TextView textView = findViewById(R.id.name);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       /* databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(owner).child("Followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.followers);
                String fcount = String.valueOf(dataSnapshot.getChildrenCount()) + " followers";
                textView.setText(fcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key).child("Likes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.likes);
                String fcount = String.valueOf(dataSnapshot.getChildrenCount()) + "   ";
                textView.setText(fcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key).child("SetWalls");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = findViewById(R.id.walls);
                String fcount = "   "+String.valueOf(dataSnapshot.getChildrenCount()) + "   ";
                textView.setText(fcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView imageView = findViewById(R.id.image_full);
        Glide.with(this).load(img).into(imageView);
        shareWall = findViewById(R.id.share);
        shareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWall();
            }

        });


    }

    private void shareWall() {
        if (checkWriteExternalPermission() == true) {
            Glide.with(PImageFull.this).asBitmap()
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
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(PImageFull.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key);
                            databaseReference.child("SetWalls").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(owner).child("Uploads").child(key);
                            databaseReference.child("SetWalls").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
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
                shareWall();

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

    public void like(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key);
        databaseReference.child("Likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

        databaseReference.child("Likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fcount = dataSnapshot.getChildrenCount();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("All").child(key);
                databaseReference.child("likecount").setValue(-fcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(owner).child("Uploads").child(key);
        databaseReference.child("Likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);


    }

    public void follow(View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(owner).child("Followers");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }
}
