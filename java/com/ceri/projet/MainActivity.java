package com.ceri.projet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.FloatingActionButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final public int ADD_TEAM_REQUEST = 1000;
    static final public int UPDATE_TEAM_REQUEST = 1001;

    final private MuseumDbHelper dbHelper = new MuseumDbHelper(this);

    private SwipeRefreshLayout refresh;
    private RecyclerViewAdapter adapter;
    private RecyclerView rvTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(findViewById(R.id.rvTeams));

        if(dbHelper.getAllTeams().isEmpty())
            dbHelper.populate();    //seulement lorsque la table est vide

        this.adapter = new RecyclerViewAdapter(this, this.dbHelper.getAllTeams());

        this.rvTeams = findViewById(R.id.rvTeams);
        rvTeams.setAdapter(adapter);
        rvTeams.setLayoutManager(new LinearLayoutManager(this));
        rvTeams.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTeamActivity.class);
                startActivityForResult(intent, MainActivity.ADD_TEAM_REQUEST);
            }
        });

        this.refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        this.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateAllTeamTask().execute();
                MainActivity.this.adapter.notifyItemRangeChanged(0, MainActivity.this.adapter.equipes.size());
            }
        });

        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int idBdd = (int)((RecyclerViewAdapter.ViewHolder)viewHolder).idBdd;
                int position = viewHolder.getAdapterPosition();
                Log.d("wouloulou", "deleting " + position + "->" + idBdd);
//                supprimer equipe de la base de données
                MainActivity.this.dbHelper.deleteTeam(idBdd);
//                supprimer equipe de la liste de l'adaptateur
                MainActivity.this.adapter.equipes.remove(position);
//                supprimer image logo equipe (si y'en a une)
                String path = MainActivity.this.getApplicationContext().getExternalFilesDir(null).toString();
                new File(path, idBdd + ".png").delete();

                MainActivity.this.adapter.notifyItemRemoved(position);
            }
        });
        ith.attachToRecyclerView(rvTeams);

//        ask for writing permissions if not already granted
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ItemActivity.ASK_WRITE_PERMISSIONS_REQUEST);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        if(item.getTitle().toString().equals("Supprimer")) {
//            WineDbHelper wineDbHelper = new WineDbHelper(this);
//            wineDbHelper.getWritableDatabase();
//
//            wineDbHelper.deleteWine(MainActivity.this.selectedWine);
//
//            finish();
//            startActivity(getIntent());
//
//            return true;
//        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MainActivity.ADD_TEAM_REQUEST && resultCode == RESULT_OK) {
            if(data.hasExtra(Item.TAG)) {
                Item item = data.getParcelableExtra(Item.TAG);
                this.dbHelper.addTeam(item);
                item = this.dbHelper.getTeam(item.getName(), item.getLeague()); //recuperer id genere par la base de donnes
                this.adapter.equipes.add(item);
                this.adapter.notifyItemInserted(this.adapter.equipes.size() - 1);
            }

        }
        else if(requestCode == MainActivity.UPDATE_TEAM_REQUEST && resultCode == RESULT_OK) {
            if(data.hasExtra(Item.TAG)) {
                Item item = data.getParcelableExtra(Item.TAG);
                List<Item> list = this.adapter.equipes;

                int position = 0;
                for(Item t : list)
                {
                    if(t.getId() == item.getId())
                        break;
                    else
                        position++;
                }

                this.dbHelper.updateTeam(item);
                this.adapter.equipes.set(position, item);
                this.adapter.notifyItemChanged(position);
            }
        }

    }

    class UpdateAllTeamTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
//            on recupere l'ensemble des teams de la BDD et on les store dans un array
            List<Item> items = MainActivity.this.dbHelper.getAllTeams();

//            pour chaque Team, on update ses valeurs
            for(Item t : items) {
                try {
                    ApiComBny.updateTeam(t);

                    int position = 0;
                    for(Item e : items)
                    {
                        if(e.getId() == t.getId())
                            break;
                        else
                            position++;
                    }
                    MainActivity.this.dbHelper.updateTeam(t);
                    MainActivity.this.adapter.equipes.set(position, t);

//                    badge
                    String path = MainActivity.this.getApplicationContext().getExternalFilesDir(null).toString();
                    File file = new File(path, t.getId() + ".png");
                    if(!file.exists())
                    {
                        Bitmap img = ApiComBny.downloadTeamBadge(t.getTeamBadge());

                        if(img != null && ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            OutputStream os = null;
                            os = new FileOutputStream(file);
                            img.compress(Bitmap.CompressFormat.PNG, 85, os);
                            os.close();

                            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MainActivity.this.refresh.setRefreshing(false);
            MainActivity.this.adapter.notifyDataSetChanged();   //a utiliser si besoin d'update la liste depuis un autre thread.
        }
    }
}
