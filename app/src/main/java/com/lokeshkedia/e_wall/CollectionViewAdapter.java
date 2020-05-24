package com.lokeshkedia.e_wall;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.kc.unsplash.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class CollectionViewAdapter {//extends BaseAdapter {
    /*Integer[] mValues;
    Context context;
   // private List<Photo> photos;

    public CollectionViewAdapter(Context context) {
        // Toast.makeText(context,String.valueOf(values.length),Toast.LENGTH_LONG).show();
        photos = new ArrayList<>();
        this.context = context;
    }


    @Override
    public int getCount() {
        return photos.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_coll, parent, false);
        } else {
            view = convertView;
        }
        final Photo photo = photos.get(position);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(context).load(photo.getUrls().getSmall()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageFull.class);
                i.putExtra("img", photo.getUrls().getFull());
                context.startActivity(i);
            }
        });
        return view;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();

    }
*/

}
