package com.lokeshkedia.e_wall;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;

//import com.kc.unsplash.Unsplash;
//import com.kc.unsplash.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class AllCollection extends AppCompatActivity {
   /* private final String CLIENT_ID = "88d4389a42ed3af25ad0f4f2c6a29312c42d93e46b9849e6bfeb1d518b316235";
    private Unsplash unsplash;
    private PhotoRecyclerAdapter adapter;
    int i = 1;
    String id, name;
    GridView gridView1;
    CollectioAdapter adapter1;
    List<Photo> photoList = new ArrayList<>();
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_collection);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        unsplash = new Unsplash(CLIENT_ID);
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        name = b.getString("name");
        toolbar.setTitle(name);
        gridView1 = findViewById(R.id.gridView);
        adapter1 = new CollectioAdapter(this);
        gridView1.setAdapter(adapter1);
        adapter1.setPhotos(photoList);
        //shimmerFrameLayout=findViewById(R.id.shimmer_view_container);
        //shimmerFrameLayout.startShimmer();
        ProgressBar progressBar = findViewById(R.id.mypbb);
        load();
        gridView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;

                        Button button = findViewById(R.id.material_button);
                        button.setVisibility(View.GONE);
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    Button button = findViewById(R.id.material_button);
                    button.setVisibility(View.VISIBLE);
                    loading = true;
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });


    }

    public void loadmore(View view) {
        i++;
        load();
    }

    public void load() {
        unsplash.getCollectionPhotos(id, i, 30, new Unsplash.OnPhotosLoadedListener() {

            @Override
            public void onComplete(List<Photo> photos) {
                photoList.addAll(photos);
                adapter1.notifyDataSetChanged();
                //shimmerFrameLayout.stopShimmer();
                ProgressBar progressBar = findViewById(R.id.mypbb);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // attachDatabaseReadListener();
        // shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //shimmerFrameLayout.stopShimmer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main2, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) AllCollection.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(AllCollection.this.getComponentName()));
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
    }

*/
}
