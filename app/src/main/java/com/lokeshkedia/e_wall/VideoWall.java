package com.lokeshkedia.e_wall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoWall extends AppCompatActivity {
    List<Wall> walls=new ArrayList<>();
    Adapter adapter1;
    String query1;
    private String JsonUrl = "https://pixabay.com/api/videos/?key=11692914-b271cf931a0e5e55eed743e36&q= &safesearch=true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_wall);
        GridView gridView = findViewById(R.id.gridView);
        loadWall();
        query1="";
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(VideoWall.this, ImageFull.class);
                intent.putExtra("img", walls.get(position).getoriginal());
                intent.putExtra("pos", position);
                intent.putExtra("query", query1);
                intent.putExtra("type", "video");
                startActivity(intent);
            }
        });

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
                                JSONObject wallArray1 = wallObj.getJSONObject("videos");
                                JSONObject wallArray2 = wallArray1.getJSONObject("tiny");


                                //JSONObject object=wallObj.getJSONObject("src");
                                Wall wall = new Wall(wallArray2.getString("url"), wallArray2.getString("url"));
                                walls.add(wall);
                            }
                            adapter1 = new Adapter(/*new Adapter.AdapterCallback() {
                                @Override
                                public void show(int pos) {

                                }
                            }, */VideoWall.this, walls);
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

}
