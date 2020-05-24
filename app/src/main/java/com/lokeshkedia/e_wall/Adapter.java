package com.lokeshkedia.e_wall;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.lokeshkedia.e_wall.R;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Adapter extends BaseAdapter {
    int STORAGE_PERMISSION_CODE = 1;
    private Context context;
    private List<Wall> walls = new ArrayList<>();
    private AdapterCallback mAdapterCallback;
    private InterstitialAd mInterstitialAd;

    public Adapter(Context context, List<Wall> walls) {
        this.context = context;
        this.walls = walls;
        //this.mAdapterCallback = callback;
    }

    @Override
    public int getCount() {
        return walls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        /*if (position % 5 != 0) {*/

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_photo, parent, false);


        } else {

            view = convertView;

        }
        ImageView view1 = view.findViewById(R.id.imageView);
        Glide.with(context).load(walls.get(position).getWebformat()).into(view1);
        ImageView view2 = view.findViewById(R.id.set);
        ImageView view3 = view.findViewById(R.id.share1);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission() == true) {
                    final LoadToast lt = new LoadToast(context);
                    lt.setText("Setting...");
                    int mHeight = context.getResources().getDisplayMetrics().heightPixels;
                    lt.setTranslationY(mHeight / 2);
                    lt.setBorderColor(Color.BLACK);
                    lt.setBorderWidthDp(4);
                    lt.show();
                    Glide.with(context).asBitmap()
                            .load(walls.get(position).getoriginal())
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
                                    Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = context.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }
                                    mInterstitialAd = new InterstitialAd(context);

                                    mInterstitialAd.setAdUnitId("ca-app-pub-5314021439844328/7411232100");
                                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    }
                                    mInterstitialAd.setAdListener(new AdListener() {
                                        @Override
                                        public void onAdLoaded() {
                                            mInterstitialAd.show();
                                            //Toast.makeText(context,"Loaded",Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onAdFailedToLoad(int errorCode) {
                                            // Code to be executed when an ad request fails.
                                            //Toast.makeText(context,String.valueOf(errorCode),Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onAdOpened() {
                                            // Code to be executed when the ad is displayed.
                                        }

                                        @Override
                                        public void onAdLeftApplication() {
                                            // Code to be executed when the user has left the app.
                                        }

                                        @Override
                                        public void onAdClosed() {
                                            // Code to be executed when the interstitial ad is closed.
                                        }
                                    });
                                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                                    context.startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                                    lt.success();
                                }
                            });
                } else {
                    Toast.makeText(context, "Grant Permission", Toast.LENGTH_LONG).show();
                }

            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission() == true) {
                    Glide.with(context).asBitmap()
                            .load(walls.get(position).getoriginal())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");
                                    String shareMessage = "\nE-wall\nDownload the application For awesome wallpapers.\n\n";
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.lokesh.e_wall" + BuildConfig.APPLICATION_ID + "\n\n";
                                    share.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = context.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    context.startActivity(Intent.createChooser(share, "Share Image"));
                                }
                            });
                } else {
                    //requestStoragePermission();
                    Toast.makeText(context, "Grant Permission", Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* ImageView view2 = view.findViewById(R.id.more);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof MainActivity) {
                    ((MainActivity) context).show(position);
                } else {
                    //  mOnItemClickLister.onItemClicked(v, position);
                    mAdapterCallback.show(position);
                }


            }
        });*/

        // } else {

          /*  if(convertView==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_ad, parent, false);

            }
            else{
                view=convertView;
                MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
                AdView mAdView = view.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
*/


        //}


        return view;
    }

    private boolean checkWriteExternalPermission() {
        boolean r;
        r = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return r;
    }

    public interface AdapterCallback {
        void show(int pos);
    }


}
