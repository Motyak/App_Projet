package com.ceri.projet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    public List<Item> equipes;


    public RecyclerViewAdapter(Context context, List<Item> equipes) {
        this.context = context;
        this.equipes = equipes;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final Item equipe = this.equipes.get(position);
        final String nomEquipe = equipe.getName();
        String dernierMatch = equipe.getLastEvent().toString();

//        badge
        String dirPath = this.context.getExternalFilesDir(null).toString();
        File imageFile = new File(dirPath, equipe.getId() + ".png");
        if(imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap img = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            ((ViewHolder) holder).image.setImageBitmap(img);
        }


        ((ViewHolder) holder).nomEquipe.setText(nomEquipe);
        ((ViewHolder) holder).dernierMatch.setText(dernierMatch);
        ((ViewHolder)holder).idBdd = equipe.getId();//
        ((ViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on : "+nomEquipe);

                Intent intent = new Intent(v.getContext(), ItemActivity.class);
                intent.putExtra(Item.TAG, equipe);
                Log.d("wouloulou", "onClick: " + equipe.getId());
                ((Activity)v.getContext()).startActivityForResult(intent, MainActivity.UPDATE_TEAM_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.equipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nomEquipe;
        TextView dernierMatch;
        LinearLayout parentLayout;
        long idBdd;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nomEquipe = itemView.findViewById(R.id.nomEquipe);
            dernierMatch = itemView.findViewById(R.id.dernierMatch);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
