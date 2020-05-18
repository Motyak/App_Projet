package com.ceri.projet;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final private MuseumDbHelper dbHelper = new MuseumDbHelper(this);

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.button = findViewById(R.id.button);

        this.dbHelper.populate();   //only one example

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class UpdateAllItemsTask extends AsyncTask {

        @Override
        protected Void doInBackground(Object[] objects) {
            try {
//                Item item = MainActivity.this.dbHelper.getAllItems().get(0);
//                ApiComBny.updateItem(item);
//                MainActivity.this.dbHelper.updateItem(item);

                ArrayList<Item> catalog = ApiComBny.fetchAllItems();
                MainActivity.this.dbHelper.synchronize(catalog);

            } catch (IOException e) { e.printStackTrace(); }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ArrayList<Item> items = new ArrayList<>(MainActivity.this.dbHelper.getAllItems());
            Intent intent = new Intent(MainActivity.this, ItemActivity.class);
            intent.putExtra(Item.TAG, items.get(0));
            startActivity(intent);
        }
    }
}
