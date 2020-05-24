package com.lokeshkedia.e_wall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PFFirst extends Fragment {
    List<String> cats1 = new ArrayList<>();
    List<Wall> catimg = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    Adapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> owners = new ArrayList<>();

    public static PFFirst newInstance(String text) {

        PFFirst f = new PFFirst();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_pffirst, container, false);
        final GridView view = v.findViewById(R.id.categories);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("All");
        adapter = new Adapter(getActivity(),catimg);
        view.setAdapter(adapter);


        databaseReference.orderByChild("likecount").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                // List<String> temp = new ArrayList<>();
              /*  for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                    temp.add(dataSnapshot2.getValue(String.class));
                }*/
                String key = dataSnapshot.getKey();
                String photo = dataSnapshot.child("Img").getValue(String.class);
                String tags = dataSnapshot.child("tags").getValue(String.class);
                String owner = dataSnapshot.child("Owner").getValue(String.class);
                cats1.add("");
                catimg.add(new Wall(photo,photo));
                owners.add(owner);
                keys.add(key);
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

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PImageFull.class);
                intent.putExtra("img", catimg.get(position).getoriginal());
                intent.putExtra("owner", owners.get(position));
                intent.putExtra("key", keys.get(position));
                startActivity(intent);
                //startActivity(intent);
            }
        });
        return v;
    }
}
