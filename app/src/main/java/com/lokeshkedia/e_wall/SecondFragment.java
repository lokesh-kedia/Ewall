package com.lokeshkedia.e_wall;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
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
//import com.kc.unsplash.Unsplash;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment implements Adapter.AdapterCallback {
    private final String CLIENT_ID = "88d4389a42ed3af25ad0f4f2c6a29312c42d93e46b9849e6bfeb1d518b316235";
    int pos;
    Adapter adapter1;
    boolean ch = false;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;
    // private Unsplash unsplash;
    private PhotoRecyclerAdapter adapter;
    private String JsonUrl = "https://pixabay.com/api/?key=11708241-4f427f9d829eb00e4ff78f36c&q= &image_type=photo&per_page=200";
    private String curWall = "";
    private int STORAGE_PERMISSION_CODE = 1;
    private List<Wall> walls = new ArrayList<>();

    public static SecondFragment newInstance(String text) {

        SecondFragment f = new SecondFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_second, container, false);
        ProgressBar progressBar = v.findViewById(R.id.progress);
        while (ch == false) {
            if (getActivity() != null) {
                loadWall(v);

            }
        }

        GridView gridView = v.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ImageFull.class);
                intent.putExtra("img", walls.get(position).getoriginal());
                intent.putExtra("pos", position);
                intent.putExtra("query", "");
                intent.putExtra("type", "photo");
                intent.putExtra("place", "pixabay");
                intent.putExtra("firewall", (Serializable) walls);
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FloatingActionButton setWall = v.findViewById(R.id.setWall);
                FloatingActionButton shareWall = v.findViewById(R.id.share);
                setWall.show();
                pos = position;
                shareWall.show();
                return true;
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                FloatingActionButton setWall = v.findViewById(R.id.setWall);
                FloatingActionButton shareWall = v.findViewById(R.id.share);
                setWall.hide();
                shareWall.hide();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        FloatingActionButton setWall = v.findViewById(R.id.setWall);
        FloatingActionButton shareWall = v.findViewById(R.id.share);
        shareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission()) {
                    Glide.with(getActivity()).asBitmap()
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

    public void loadWall(final View v) {
        ch = true;
        walls = new ArrayList<>();
        adapter1 = new Adapter(/*new Adapter.AdapterCallback() {
            @Override
            public void show(int position) {
                FloatingActionButton setWall = v.findViewById(R.id.setWall);
                FloatingActionButton shareWall = v.findViewById(R.id.share);
                pos = position;
                setWall.show();
                shareWall.show();
            }
        },*/ getActivity(), walls);
        final GridView view = v.findViewById(R.id.gridView);
        view.setAdapter(adapter1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            // Log.e("mil gaya",String.valueOf(obj));
                            JSONArray wallArray = obj.getJSONArray("hits");
                            for (int i = 0; i < wallArray.length(); i++) {
                                JSONObject wallObj = wallArray.getJSONObject(i);
                                //JSONObject object=wallObj.getJSONObject("src");
                                Wall wall = new Wall(wallObj.getString("largeImageURL"), wallObj.getString("webformatURL"));
                                walls.add(wall);
                            }
                            adapter1 = new Adapter(/*new Adapter.AdapterCallback() {
                                @Override
                                public void show(int position) {
                                    FloatingActionButton setWall = v.findViewById(R.id.setWall);
                                    FloatingActionButton shareWall = v.findViewById(R.id.share);
                                    pos = position;
                                    setWall.show();
                                    shareWall.show();
                                }
                            },*/ getActivity(), walls);
                            view.setAdapter(adapter1);
                        } catch (JSONException e) {
                            Log.e("Nhi mila1", String.valueOf(e));
                            e.getStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Nhi mila2 ", String.valueOf(error));

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


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

    @Override
    public void show(int position) {
        FloatingActionButton setWall = getActivity().findViewById(R.id.setWall);
        FloatingActionButton shareWall = getActivity().findViewById(R.id.share);
        pos = position;
        setWall.show();
        shareWall.show();
    }

    @Override
    public void onStart() {


        super.onStart();

    }
}