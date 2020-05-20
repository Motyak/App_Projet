package com.ceri.projet;

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


public class MainActivity extends AppCompatActivity {

    final private MuseumDbHelper dbHelper = new MuseumDbHelper(this);

    private SwipeRefreshLayout refresh;
    private RecyclerViewAdapter simpleAdapter;
    private SimpleSectionedRecyclerViewAdapter adapter;
    private RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(findViewById(R.id.rvItems));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortAlphabetically) {
            this.adapter = AdapterCreator.createAdapterAlpha(this, this.simpleAdapter.catalog);
            this.rvItems.setAdapter(this.adapter);
        }
        else if(id == R.id.sortChronologically)
            this.simpleAdapter.sortItemsChronologically();
        else if(id == R.id.sortByCategories)
            ;

        this.adapter.notifyItemRangeChanged(0, this.adapter.getItemCount());

        return super.onOptionsItemSelected(item);
    }

    class UpdateAllItemsTask extends AsyncTask {

        @Override
        protected Void doInBackground(Object[] objects) {
            try {
                ArrayList<Item> catalog = ApiComBny.fetchAllItems();
                MainActivity.this.dbHelper.synchronize(catalog);
                MainActivity.this.simpleAdapter.catalog = catalog;
                MainActivity.this.adapter = AdapterCreator.createAdapterAlpha(
                        MainActivity.this, MainActivity.this.simpleAdapter.catalog);
            } catch (IOException e) { e.printStackTrace(); }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MainActivity.this.refresh.setRefreshing(false);
            MainActivity.this.rvItems.setAdapter(MainActivity.this.adapter);
            MainActivity.this.adapter.notifyDataSetChanged();
        }
    }
}
