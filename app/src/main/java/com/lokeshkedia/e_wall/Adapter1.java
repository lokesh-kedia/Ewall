package com.lokeshkedia.e_wall;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lokeshkedia.e_wall.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter1 extends BaseAdapter {

    private Context context;
    private String[] cats = {
            "Animals", "Backgrounds", "Buildings", "Business", "Computer", "Education", "Feelings", "Food", "Health", "Nature", "Fashion", "Cars", "Sports", "Science", "Transportation", "Travel", "Religion", "People", "Places", "Industry", "Music"
    };
    private List<String> cats1 = new ArrayList<>();
    private List<String> catimg = new ArrayList<>();

    public Adapter1(Context context, List<String> cats1, List<String> catimg) {
        this.context = context;
        this.cats1 = cats1;
        this.catimg = catimg;

    }

    @Override
    public int getCount() {
        return catimg.size();
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
            view = inflater.inflate(R.layout.item_cat, parent, false);
        } else {
            view = convertView;
        }
        ImageView view1 = view.findViewById(R.id.imageView);
        Glide.with(context).load(catimg.get(position)).into(view1);
        TextView textView = view.findViewById(R.id.cat_name);
        textView.setText(cats1.get(position));

        return view;
    }

}
