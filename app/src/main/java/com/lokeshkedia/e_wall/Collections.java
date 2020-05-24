package com.lokeshkedia.e_wall;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.SearchView;

//import com.kc.unsplash.Unsplash;
//import com.kc.unsplash.models.Photo;

import java.util.List;

public class Collections extends AppCompatActivity {
   /* private Integer[] walls = {
            R.mipmap.animal,
            R.mipmap.background,
            R.mipmap.buildings,
            R.mipmap.business,
            R.mipmap.computer,
            R.mipmap.education,
            R.mipmap.feelings,
            R.mipmap.food,
            R.mipmap.health,
            R.mipmap.nature,
            R.mipmap.fashion,
            R.mipmap.car,
            R.mipmap.sport,
            R.mipmap.science,
            R.mipmap.transportation,
            R.mipmap.travel,
            R.mipmap.religion,
            R.mipmap.people,
            R.mipmap.places,
            R.mipmap.industry,
            R.mipmap.music,

    };
    Integer[] cat = {R.id.cat1, R.id.cat2, R.id.cat3, R.id.cat4, R.id.cat5, R.id.cat6, R.id.cat7, R.id.cat8, R.id.cat9};
   // Unsplash unsplash;
    private final String CLIENT_ID = "88d4389a42ed3af25ad0f4f2c6a29312c42d93e46b9849e6bfeb1d518b316235";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collections);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
       // unsplash = new Unsplash(CLIENT_ID);
        GridView gridView1 = findViewById(R.id.gridView1);
        GridView gridView2 = findViewById(R.id.gridView2);
        GridView gridView3 = findViewById(R.id.gridView3);
        GridView gridView4 = findViewById(R.id.gridView4);
        GridView gridView5 = findViewById(R.id.gridView5);
        GridView gridView6 = findViewById(R.id.gridView6);
        GridView gridView7 = findViewById(R.id.gridView7);
        GridView gridView8 = findViewById(R.id.gridView8);
        GridView gridView9 = findViewById(R.id.gridView9);
        final CollectionViewAdapter adapter1 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter2 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter3 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter4 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter5 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter6 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter7 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter8 = new CollectionViewAdapter(this);
        final CollectionViewAdapter adapter9 = new CollectionViewAdapter(this);
        String[] cats = {"Animals", "Wallpapers", "Textures & Patterns", "Nature", "Architecture", "Travel", "Fashion", "Food & Drink",};
        //RecyclerView recyclerView = findViewById(R.id.colls);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false));

        //final PhotoRecyclerAdapter adapter = new PhotoRecyclerAdapter(this);
        //recyclerView.setAdapter(adapter);
        gridView1.setAdapter(adapter1);
        gridView2.setAdapter(adapter2);
        gridView3.setAdapter(adapter3);
        gridView4.setAdapter(adapter4);
        gridView5.setAdapter(adapter5);
        gridView6.setAdapter(adapter6);
        gridView7.setAdapter(adapter7);
        gridView8.setAdapter(adapter8);
        gridView9.setAdapter(adapter9);

      *//*  unsplash.getCollectionPhotos("1368747", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter1.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("357786", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter2.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("181581", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter3.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("1127828", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter4.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("2270935", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter5.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("369", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter6.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("1111575", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter7.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("827741", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter8.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        unsplash.getCollectionPhotos("827737", 1, 20, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                adapter9.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });*//*
        // unsplash.searchPhotos();


    }

    public void seeAll(View view) {
        switch (view.getId()) {
            case R.id.cat1:
                Intent intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "1368747");
                intent.putExtra("name", "Background");
                startActivity(intent);
                break;
            case R.id.cat2:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "357786");
                intent.putExtra("name", "Nature");
                startActivity(intent);
                break;
            case R.id.cat3:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "181581");
                intent.putExtra("name", "Animals");
                startActivity(intent);
                break;
            case R.id.cat4:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "1127828");
                intent.putExtra("name", "Ammold");
                startActivity(intent);
                break;
            case R.id.cat5:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "2270935");
                intent.putExtra("name", "Shapes & pattern");
                startActivity(intent);
                break;
            case R.id.cat6:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "369");
                intent.putExtra("name", "Adventure");
                startActivity(intent);
                break;
            case R.id.cat7:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "1111575");
                intent.putExtra("name", "Space");
                startActivity(intent);
                break;
            case R.id.cat8:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "827741");
                intent.putExtra("name", "Music");
                startActivity(intent);
                break;
            case R.id.cat9:
                intent = new Intent(Collections.this, AllCollection.class);
                intent.putExtra("id", "827737");
                intent.putExtra("name", "Sport");
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main2, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) Collections.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Collections.this.getComponentName()));
        }
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
    }*/
}
