package com.lokeshkedia.e_wall;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
//import com.kc.unsplash.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoRecyclerAdapter {// extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

   /* private List<Photo> photos;
    private Context context;

    public PhotoRecyclerAdapter(Context context) {
        photos = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Photo photo = photos.get(position);

        //Picasso.get().load(photo.getUrls().getSmall()).into(holder.imageView);
        Glide.with(context).load(photo.getUrls().getSmall()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageFull.class);
                i.putExtra("img", photo.getUrls().getFull());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }*/
}
