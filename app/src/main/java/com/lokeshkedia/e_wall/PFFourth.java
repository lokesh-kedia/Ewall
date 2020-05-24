package com.lokeshkedia.e_wall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PFFourth extends Fragment {
    List<String> cats1 = new ArrayList<>();
    List<String> catimg = new ArrayList<>();
    Adapter1 adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static PFFourth newInstance(String text) {

        PFFourth f = new PFFourth();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_pffourth, container, false);
        final GridView view = v.findViewById(R.id.gridView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        databaseReference = firebaseDatabase.getReference("Photographers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile").getRef();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String photo = dataSnapshot.child("Img").getValue(String.class);
                String name = dataSnapshot.child("Name").getValue(String.class);
                //String photo = dataSnapshot.child("Img").getValue(String.class);
                ImageView imageView = v.findViewById(R.id.profile_image);
                TextView textView = v.findViewById(R.id.name);
                Glide.with(getActivity()).load(photo).into(imageView);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = firebaseDatabase.getReference("Photographers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String points= String.valueOf(dataSnapshot.child("Points").getValue(Integer.class));
                String level= String.valueOf(dataSnapshot.child("Level").getValue(Integer.class));
                TextView textView=v.findViewById(R.id.level);
                TextView textView1=v.findViewById(R.id.point);
                level="Level "+level+" | ";
                points=points+" Points";
                textView.setText(level);
                textView1.setText(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = firebaseDatabase.getReference("Photographers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Uploads");
        adapter = new Adapter1(getActivity(), cats1, catimg);
        view.setAdapter(adapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                /*List<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                    temp.add(dataSnapshot2.getValue(String.class));
                }*/
                String photo = dataSnapshot.child("Img").getValue(String.class);
                String tags = dataSnapshot.child("tags").getValue(String.class);
                cats1.add(tags);
                catimg.add(photo);
                // Toast.makeText(getActivity(), temp.get(0) + temp.get(1), Toast.LENGTH_LONG).show();
                //  }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView = v.findViewById(R.id.followers);
                String fcount = String.valueOf(dataSnapshot.getChildrenCount()) + " followers";
                textView.setText(fcount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PImageFull.class);
                intent.putExtra("img", catimg.get(position));
                startActivity(intent);
                //startActivity(intent);
            }
        });
        return v;
    }
}
