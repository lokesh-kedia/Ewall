package com.lokeshkedia.e_wall;

import android.Manifest;
import android.app.SearchManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import com.kc.unsplash.Unsplash;


public class MainActivity extends AppCompatActivity {

    private final String CLIENT_ID = "88d4389a42ed3af25ad0f4f2c6a29312c42d93e46b9849e6bfeb1d518b316235";
    int pos;
    Adapter adapter1;
    String supername;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9, Count;
    String query1, place;
    //private Unsplash unsplash;
    private PhotoRecyclerAdapter adapter;
    private String JsonUrl = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q= &image_type=photo&per_page=200&safesearch=true";
    private int STORAGE_PERMISSION_CODE = 1;
    private List<Wall> walls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, ImageFull.class);
                intent.putExtra("img", walls.get(position).getoriginal());
                intent.putExtra("pos", position);
                intent.putExtra("query", query1);
                intent.putExtra("place", place);
                intent.putExtra("type", "photo");
                intent.putExtra("firewall", (Serializable) walls);
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FloatingActionButton setWall = findViewById(R.id.setWall);
                FloatingActionButton shareWall = findViewById(R.id.share);
                pos = position;
                setWall.show();
                shareWall.show();
                return true;
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                FloatingActionButton setWall = findViewById(R.id.setWall);
                FloatingActionButton shareWall = findViewById(R.id.share);
                setWall.hide();
                shareWall.hide();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        FloatingActionButton setWall = findViewById(R.id.setWall);
        setWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadToast lt = new LoadToast(MainActivity.this);
                lt.setText("Setting...");
                int mHeight = MainActivity.this.getResources().getDisplayMetrics().heightPixels;
                lt.setTranslationY(mHeight / 2);
                lt.setBorderColor(Color.BLACK);
                lt.setBorderWidthDp(4);
                v.setVisibility(View.GONE);
                lt.show();
                Glide.with(MainActivity.this).asBitmap()
                        .load(walls.get(pos).getoriginal())
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
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                                startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                                lt.success();
                            }
                        });
            }
        });
        FloatingActionButton shareWall = findViewById(R.id.share);
        shareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission() == true) {
                    Glide.with(MainActivity.this).asBitmap()
                            .load(walls.get(pos).getoriginal())
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

        });
        query1 = getIntent().getExtras().getString("search");
        place = getIntent().getExtras().getString("place");
        toolbar.setTitle(query1);
        if (!place.equals("firebase"))
            search(query1);
        else {


            Count = getIntent().getExtras().getInt("count");
            supername = getIntent().getExtras().getString("supername");
            toolbar.setTitle(supername);
            loadFirebase();
        }

        /*MobileAds.initialize(this, "ca-app-pub-5314021439844328/2500566647");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

    }

    private void loadFirebase() {
        walls = new ArrayList<>();
        adapter1 = new Adapter(this, walls);
        final GridView view = findViewById(R.id.gridView);
        view.setAdapter(adapter1);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        for (int i = 1; i <= Count; i++) {
            //System.out.println("holi2 " + String.valueOf(i));
            storageReference.child("Superhero/" + supername + "/1 (" + i + ").webp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Wall wall = new Wall(uri.toString(), uri.toString());
                    walls.add(wall);
                    adapter1.notifyDataSetChanged();
                }
            });
        }

    }

    public void search(String query) {
        JsonUrl = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q=" + query + "&image_type=photo&per_page=200&safesearch=true";
        loadWall();
    }


    public void loadWall() {
        walls = new ArrayList<>();
        adapter1 = new Adapter(/*new Adapter.AdapterCallback() {
            @Override
            public void show(int pos) {

            }
        }, */this, walls);
        final GridView view = findViewById(R.id.gridView);
        view.setAdapter(adapter1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            //Log.e("mil gaya", String.valueOf(obj));
                            JSONArray wallArray = obj.getJSONArray("hits");
                            for (int i = 0; i < wallArray.length(); i++) {
                                JSONObject wallObj = wallArray.getJSONObject(i);
                                //JSONObject object=wallObj.getJSONObject("src");
                                Wall wall = new Wall(wallObj.getString("largeImageURL"), wallObj.getString("webformatURL"));
                                walls.add(wall);
                            }
                            adapter1 = new Adapter(/*new Adapter.AdapterCallback() {
                                @Override
                                public void show(int pos) {

                                }
                            }, */MainActivity.this, walls);
                            view.setAdapter(adapter1);
                        } catch (JSONException e) {
                            Log.e("Nhi mila ", String.valueOf(e));
                            e.getStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Nhi mila ", String.valueOf(error));

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main2, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportActionBar().setTitle(query);
                query1 = query;
                JsonUrl = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q=" + query + "&image_type=photo&per_page=200&safesearch=true";
                loadWall();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    public void show(int position) {
        pos = position;
        FloatingActionButton setWall = findViewById(R.id.setWall);
        FloatingActionButton shareWall = findViewById(R.id.share);
        setWall.show();
        shareWall.show();

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
                //shareWall();

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

    private boolean isNetworkAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
}
