package com.lokeshkedia.e_wall;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.jaago.jaago.OnSwipeTouchListener;
//import com.kc.unsplash.Unsplash;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {
    private final String CLIENT_ID = "88d4389a42ed3af25ad0f4f2c6a29312c42d93e46b9849e6bfeb1d518b316235";
    SharedPref sharedPref;
    String query;
    String curWall = null;
    int pos = 0;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    private String JsonUrl = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q= &image_type=photo&per_page=30";
    private String unsplashUrl = "https://api.unsplash.com/photos/random?featured=true&client_id=fed0dabe1669e7643163440ed99a7307c01efc69c1c2db9c51ef5ada9c20ee18";
    private List<Wall> walls = new ArrayList<>();
    // private Unsplash unsplash;
    private List<String> wallList = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 1;

    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_first, container, false);
        //unsplash = new Unsplash(CLIENT_ID);
        final ImageView imageView = v.findViewById(R.id.imagec);

        // loadWall(v);
        //loadUnsplash(v);
        sharedPref = new SharedPref(getActivity());
        if (sharedPref.loadFirstTimeState() == true) {
            //ImageView imageView1=v.findViewById(R.id.swipe_up);
            //imageView1.setVisibility(View.VISIBLE);
            //TextView textView=v.findViewById(R.id.swipe_up_text);
            //textView.setVisibility(View.VISIBLE);
            //startActivity(new Intent(getActivity(),IntroActivity.class));

        } else {
            ImageView imageView1 = v.findViewById(R.id.swipe_up);
            imageView1.setVisibility(View.GONE);
            TextView textView = v.findViewById(R.id.swipe_up_text);
            textView.setVisibility(View.GONE);
        }
        final EditText editText = v.findViewById(R.id.edit_search);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        query = editText.getText().toString();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("search", query);
                        intent.putExtra("place", "firstFragment");
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
    /*    final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
        if (getActivity() != null) {
                   /* AdView adView = new AdView(getActivity());
                    adView.setAdSize(AdSize.BANNER);
                    adView.setAdUnitId("ca-app-pub-5314021439844328~3628546561");*/
                   /* MobileAds.initialize(getActivity(), "ca-app-pub-5314021439844328~3628546561");
                    AdView mAdView = v.findViewById(R.id.adView);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);
                    mAdView.bringToFront();*/
        }
        // }
        //}, 0000);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (editText.getText() != null) {

                        query = editText.getText().toString();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("place", "firstFragment");
                        intent.putExtra("search", query);
                        startActivity(intent);

                    }
                }
                return false;
            }
        });

        final FloatingActionButton setWall = v.findViewById(R.id.setWall);
        FloatingActionButton shareWall = v.findViewById(R.id.share);
        shareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission() == true) {
                    Glide.with(getActivity()).asBitmap()
                            .load(curWall)
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
                                    Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = getActivity().getContentResolver().openOutputStream(uri);
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

        });
        setWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission() == true) {
                    final LoadToast lt = new LoadToast(getActivity());
                    lt.setText("Setting...");
                    int mHeight = getActivity().getResources().getDisplayMetrics().heightPixels;
                    lt.setTranslationY(mHeight / 2);
                    lt.setBorderColor(Color.BLACK);
                    lt.setBorderWidthDp(4);
                    v.setVisibility(View.GONE);
                    lt.show();
                    Glide.with(getActivity()).asBitmap()
                            .load(curWall)
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
                                    Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = getActivity().getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }
                                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
                                    startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                                    lt.success();
                                }
                            });
                } else {
                    requestStoragePermission();

                }
            }

        });
        return v;
    }

    /* private void loadUnsplash(final View v) {
         final ProgressBar progressBar=v.findViewById(R.id.progress);
         progressBar.setVisibility(View.VISIBLE);
         unsplash.getRandomPhoto("1368747,357786,1111575", true, null, "", null, null, null, new Unsplash.OnPhotoLoadedListener() {

             @Override
             public void onComplete(Photo photo) {
                 if (getActivity() != null) {
                     ImageView imageView = v.findViewById(R.id.imagec);

                     Glide.with(getActivity()).load(photo.getUrls().getRegular()).into(imageView);
                     progressBar.setVisibility(View.GONE);
                     wallList.add(photo.getUrls().getRegular());
                     curWall = photo.getUrls().getFull();
                 }
                 //Toast.makeText(getActivity(),photo.getUrls().getRegular(),Toast.LENGTH_SHORT).show();

             }

             @Override
             public void onError(String error) {

             }
         });
         final ImageView imageView = v.findViewById(R.id.imagec);
         imageView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
             @Override
             public void onSwipeTop() {
                 ImageView imageView1 = v.findViewById(R.id.logoimg);
                 imageView1.setVisibility(View.VISIBLE);
                 pos++;
                 if (pos < wallList.size())
                     Glide.with(getActivity()).load(wallList.get(pos)).into(imageView);
                 else
                     loadUnsplash(v);

             }

             @Override
             public void onSwipeBottom() {
                 ImageView imageView1 = v.findViewById(R.id.logoimg);
                 imageView1.setVisibility(View.VISIBLE);
                 pos--;
                 Glide.with(getActivity()).load(wallList.get(pos)).into(imageView);
             }

             @Override
             public void onClick() {
                 FloatingActionButton setWall = v.findViewById(R.id.setWall);
                 FloatingActionButton shareWall = v.findViewById(R.id.share);
                 EditText editText = v.findViewById(R.id.edit_search);
                 if (editText.getVisibility() == View.VISIBLE) {
                     editText.setVisibility(View.GONE);
                     setWall.show();
                     shareWall.show();
                 } else {
                     editText.setVisibility(View.VISIBLE);
                     setWall.hide();
                     shareWall.hide();
                 }
                 super.onClick();
             }
         });
     }
 */
    public void loadWall(final View v) {
        final ProgressBar progressBar = v.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        walls = new ArrayList<>();
        final ImageView imageView = v.findViewById(R.id.imagec);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray wallArray = obj.getJSONArray("hits");
                            for (int i = 0; i < wallArray.length(); i++) {
                                JSONObject wallObj = wallArray.getJSONObject(i);
                                Wall wall = new Wall(wallObj.getString("largeImageURL"), wallObj.getString("webformatURL"));
                                walls.add(wall);
                            }

                            final Random random = new Random();
                            int n = random.nextInt(walls.size() - 1);
                            if (getActivity() != null)
                                Glide.with(getActivity()).load(walls.get(n).getoriginal()).into(imageView);
                            //progressBar.setVisibility(View.GONE);
                            final ImageView imageView1 = v.findViewById(R.id.logoimg);
                            imageView1.setVisibility(View.GONE);
                            wallList.add(walls.get(n).getoriginal());
                            curWall = walls.get(n).getoriginal();
                            pos++;
                            imageView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                                @Override
                                public void onSwipeTop() {
                                    // progressBar.setVisibility(View.VISIBLE);
                                    imageView1.setVisibility(View.VISIBLE);

                                    pos++;
                                    if (pos < wallList.size())
                                        Glide.with(getActivity()).load(wallList.get(pos)).into(imageView);
                                    else {
                                        int n = random.nextInt(walls.size() - 1);
                                        curWall = walls.get(n).getoriginal();
                                        wallList.add(walls.get(n).getoriginal());
                                        Glide.with(getActivity()).load(walls.get(n).getoriginal()).into(imageView);
                                        imageView1.setVisibility(View.GONE);
                                    }
                                    //progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onSwipeBottom() {
                                    //progressBar.setVisibility(View.VISIBLE);
                                    imageView1.setVisibility(View.VISIBLE);
                                    pos--;
                                    Glide.with(getActivity()).load(wallList.get(pos)).into(imageView);
                                    // progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onClick() {
                                    FloatingActionButton setWall = v.findViewById(R.id.setWall);
                                    FloatingActionButton shareWall = v.findViewById(R.id.share);
                                    EditText editText = v.findViewById(R.id.edit_search);
                                    if (editText.getVisibility() == View.VISIBLE) {
                                        editText.setVisibility(View.GONE);
                                        setWall.show();
                                        shareWall.show();
                                    } else {
                                        editText.setVisibility(View.VISIBLE);
                                        setWall.hide();
                                        shareWall.hide();
                                    }
                                    super.onClick();
                                }
                            });
                        } catch (JSONException e) {
                            Log.e("Nhi mila3 ", String.valueOf(e));
                            e.getStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Nhi mila4 ", String.valueOf(error));

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    @Override
    public void onStart() {
        boolean c = isNetworkAvailable();
        if (c == true) {
            ImageView imageView = getActivity().findViewById(R.id.nointernet);
            imageView.setVisibility(View.GONE);
            EditText editText = getActivity().findViewById(R.id.edit_search);
            editText.setVisibility(View.VISIBLE);
            TextView editText1 = getActivity().findViewById(R.id.textNo);
            editText1.setVisibility(View.GONE);
            Button button = getActivity().findViewById(R.id.material_button);
            button.setVisibility(View.GONE);
            //Toast.makeText(getActivity(),"connected",Toast.LENGTH_SHORT).show();
            loadWall(getView());
        } else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
            ImageView imageView = getActivity().findViewById(R.id.nointernet);
            imageView.setVisibility(View.VISIBLE);
            EditText editText = getActivity().findViewById(R.id.edit_search);
            editText.setVisibility(View.GONE);
            TextView editText1 = getActivity().findViewById(R.id.textNo);
            editText1.setVisibility(View.VISIBLE);
            Button button = getActivity().findViewById(R.id.material_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStart();
                }
            });
        }

        super.onStart();

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
                //shareWall();

            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkWriteExternalPermission() {
        boolean r;
        r = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return r;
    }

    private boolean isNetworkAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
}