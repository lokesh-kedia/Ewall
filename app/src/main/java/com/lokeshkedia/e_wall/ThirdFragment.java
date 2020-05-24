package com.lokeshkedia.e_wall;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ThirdFragment extends Fragment {
    List<Wall> walls = new ArrayList<>();
    List<String> cats1 = new ArrayList<>();
    List<String> catimg = new ArrayList<>();
    Adapter1 adapter;
    List<String> names = new ArrayList<>();
    List<Integer> counts = new ArrayList<>();
    SuperAdapter superAdapter;
    List<Wall> wallList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String[] cats = {
            "Animals", "Backgrounds", "Buildings", "Business", "Computer", "Education", "Feelings", "Food", "Health", "Nature", "Fashion", "Cars", "Sports", "Science", "Transportation", "Travel", "Religion", "People", "Places", "Industry", "Music"
    };

    public static ThirdFragment newInstance(String text) {

        ThirdFragment f = new ThirdFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        final GridView view = v.findViewById(R.id.categories);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Categories");
        adapter = new Adapter1(getActivity(), cats1, catimg);
        final GridView gridView = v.findViewById(R.id.gridView1);
        superAdapter = new SuperAdapter(getActivity(), wallList);
        gridView.setAdapter(superAdapter);
        view.setAdapter(adapter);
        final ProgressBar progressBar = v.findViewById(R.id.pb);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                    temp.add(dataSnapshot2.getValue(String.class));
                }
                cats1.add(temp.get(0));
                catimg.add(temp.get(1));
                progressBar.setVisibility(View.GONE);
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("search", cats1.get(position));
                intent.putExtra("place", "pixabay");
                Toast.makeText(getActivity(), cats1.get(position), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Superhero");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String Name = dataSnapshot.child("Name").getValue(String.class);
                int Count = dataSnapshot.child("Count").getValue(Integer.class);
                String Url = dataSnapshot.child("Url").getValue(String.class);
                names.add(Name);
                counts.add(Count);
                //Toast.makeText(getActivity(), Name, Toast.LENGTH_LONG).show();
                Wall wall = new Wall(Url, Url);

                wallList.add(wall);
                superAdapter.notifyDataSetChanged();


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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("supername", names.get(position));
                intent.putExtra("count", counts.get(position));
                intent.putExtra("place", "firebase");
                //Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


        return v;
    }
}