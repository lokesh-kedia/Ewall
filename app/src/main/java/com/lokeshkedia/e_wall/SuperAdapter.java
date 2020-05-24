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

public class SuperAdapter extends BaseAdapter {

    private Context context;
    private List<Wall> walls = new ArrayList<>();

    public SuperAdapter(Context context, List<Wall> walls) {
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
        //Toast.makeText(context,"yeh "+walls.get(position).getoriginal(),Toast.LENGTH_LONG).show();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_super, parent, false);


        } else {

            view = convertView;

        }

        ImageView view1 = view.findViewById(R.id.wolv);
        Glide.with(context).load(walls.get(position).getoriginal()).into(view1);
        return view;
    }


}
