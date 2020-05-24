package com.ceri.projet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final private MuseumDbHelper dbHelper = new MuseumDbHelper(this);

    private SwipeRefreshLayout refresh;
    private RecyclerViewAdapter simpleAdapter;
    private SimpleSectionedRecyclerViewAdapter adapter;
    private RecyclerView rvItems;

    private Tri tri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(findViewById(R.id.rvItems));

        this.tri = Tri.ALPHA;

        this.simpleAdapter = new RecyclerViewAdapter(this, this.dbHelper.getAllItems());
        this.adapter = AdapterCreator.createAdapterAlpha(this, this.simpleAdapter.catalog);

        this.rvItems = findViewById(R.id.rvItems);
        this.rvItems.setLayoutManager(new LinearLayoutManager(this));
        this.rvItems.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        this.rvItems.setAdapter(this.adapter);

        this.refresh = findViewById(R.id.refresh);
        this.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateAllItemsTask().execute();
            }
        });

//        cache default image in case no picture found
        GlideBny.saveInCache(this, ItemImage.NO_PICTURES_IMAGE);

        if(this.simpleAdapter.catalog.isEmpty()) {
            new UpdateAllItemsTask().execute();
            this.refresh.setRefreshing(true);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortAlphabetically) {
            this.adapter = AdapterCreator.createAdapterAlpha(this, this.simpleAdapter.catalog);
            this.rvItems.setAdapter(this.adapter);
            this.tri = Tri.ALPHA;
        }
        else if(id == R.id.sortChronologically) {
            this.adapter = AdapterCreator.createAdapterChrono(this, this.simpleAdapter.catalog);
            this.rvItems.setAdapter(this.adapter);
            this.tri = Tri.CHRONO;
        }
        else if(id == R.id.sortByCategories) {
            this.adapter = AdapterCreator.createAdapterCategories(this,
                    MainActivity.this.dbHelper.getCategories(), this.simpleAdapter.catalog);
            this.rvItems.setAdapter(this.adapter);
            this.tri = Tri.CATEGORIES;
        }
        else if(id == R.id.action_redirect_to_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putParcelableArrayListExtra("catalog", (ArrayList<Item>)this.simpleAdapter.catalog);
            MainActivity.this.startActivityForResult(intent, 1000);
        }

        this.adapter.notifyItemRangeChanged(0, this.adapter.getItemCount());

        return super.onOptionsItemSelected(menuItem);
    }

    public void cacheCatalogImages() {
        new CacheCatalogImagesTask().execute();
    }

    class UpdateAllItemsTask extends AsyncTask {

        @Override
        protected Void doInBackground(Object[] objects) {
            try {
                ArrayList<Item> catalog = ApiComBny.fetchAllItems();
                MainActivity.this.dbHelper.synchronize(catalog);
                MainActivity.this.simpleAdapter.catalog = catalog;

                if(MainActivity.this.tri == Tri.ALPHA) {
                    MainActivity.this.adapter = AdapterCreator.createAdapterAlpha(
                            MainActivity.this, MainActivity.this.simpleAdapter.catalog);
                }
                else if(MainActivity.this.tri == Tri.CHRONO) {
                    MainActivity.this.adapter = AdapterCreator.createAdapterChrono(
                            MainActivity.this, MainActivity.this.simpleAdapter.catalog);
                }
                else if(MainActivity.this.tri == Tri.CATEGORIES) {
                    MainActivity.this.adapter = AdapterCreator.createAdapterCategories(
                            MainActivity.this, MainActivity.this.dbHelper.getCategories(),
                            MainActivity.this.simpleAdapter.catalog);
                }

            } catch (IOException e) { e.printStackTrace(); }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MainActivity.this.cacheCatalogImages();

            MainActivity.this.refresh.setRefreshing(false);
            MainActivity.this.rvItems.setAdapter(MainActivity.this.adapter);
            MainActivity.this.adapter.notifyDataSetChanged();
        }
    }

    class CacheCatalogImagesTask extends AsyncTask {

        @Override
        protected Void doInBackground(Object[] objects) {

            System.out.println("Début remplissage cache");

            List<Item> catalog = MainActivity.this.dbHelper.getAllItems();
            for (Item item : catalog) {
                GlideBny.saveInCache(MainActivity.this, item.getThumbnail());
                for (ItemImage image : item.getPictures())
                    GlideBny.saveInCache(MainActivity.this, image.getImageUrl());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println("Cache rempli");
        }
    }
}
