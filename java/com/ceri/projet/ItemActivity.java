package com.ceri.projet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = ItemActivity.class.getSimpleName();
    static final public int ASK_WRITE_PERMISSIONS_REQUEST = 1002;
    private TextView textTeamName, textLeague, textManager, textStadium, textStadiumLocation,
            textTotalScore, textRanking, textLastMatch, textLastUpdate;


    private int totalPoints;
    private int ranking;
    private Match lastEvent;
    private String lastUpdate;

    private ImageView imageBadge;
    private Item item;

    public void setItem(Item item) { this.item = item; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        item = (Item) getIntent().getParcelableExtra(Item.TAG);

        textTeamName = (TextView) findViewById(R.id.nameTeam);
        textLeague = (TextView) findViewById(R.id.league);
        textStadium = (TextView) findViewById(R.id.editStadium);
        textStadiumLocation = (TextView) findViewById(R.id.editStadiumLocation);
        textTotalScore = (TextView) findViewById(R.id.editTotalScore);
        textRanking = (TextView) findViewById(R.id.editRanking);
        textLastMatch = (TextView) findViewById(R.id.editLastMatch);
        textLastUpdate = (TextView) findViewById(R.id.editLastUpdate);
        imageBadge = (ImageView) findViewById(R.id.imageView);

        updateView();

        final Button but = (Button) findViewById(R.id.button);


        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new UpdateTeamTask(ItemActivity.this.item).execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Item.TAG, this.item);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateView() {

        textTeamName.setText(item.getName());
        textLeague.setText(item.getLeague());
        textStadium.setText(item.getStadium());
        textStadiumLocation.setText(item.getStadiumLocation());
        textTotalScore.setText(Integer.toString(item.getTotalPoints()));
        textRanking.setText(Integer.toString(item.getRanking()));
        textLastMatch.setText(item.getLastEvent().toString());
        textLastUpdate.setText(item.getLastUpdate());

//        ajout du logo
        String dirPath = getApplicationContext().getExternalFilesDir(null).toString();
        File imageFile = new File(dirPath, this.item.getId() + ".png");
        if(imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap img = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            imageBadge.setImageBitmap(img);
        }
    }

    class UpdateTeamTask extends AsyncTask {

        private Item item;

        UpdateTeamTask(Item item) {
            this.item = item;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
//                mise a jour infos de l'equipe dans l'activité
                boolean exist = ApiComBny.updateTeam(this.item);

//                recuperation du logo
                if(exist)
                {
                    String path = ItemActivity.this.getApplicationContext().getExternalFilesDir(null).toString();
                    File file = new File(path, this.item.getId() + ".png");
                    if(!file.exists())
                    {
                        Bitmap img = ApiComBny.downloadTeamBadge(this.item.getTeamBadge());

                        if(img != null && ContextCompat.checkSelfPermission(ItemActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            OutputStream os = null;
                            os = new FileOutputStream(file);
                            img.compress(Bitmap.CompressFormat.PNG, 85, os);
                            os.close();

                            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ItemActivity.this.setItem(this.item);
            ItemActivity.this.updateView();
        }
    }

}
