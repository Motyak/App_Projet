package com.ceri.projet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getName();

    private Context context;
    public List<Item> catalog;


    public RecyclerViewAdapter(Context context, List<Item> catalog) {
        this.context = context;
        this.catalog = catalog;
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

        final Item item = this.catalog.get(position);

        ((ViewHolder) holder).itemName.setText(item.getName());
        ((ViewHolder) holder).itemBrand.setText(item.getBrand());
        ((ViewHolder) holder).itemYear.setText(String.valueOf(item.getYear()));
        ((ViewHolder)holder).idBdd = item.getId();

        Glide.with(((ViewHolder)holder).itemView)
                .load(item.getThumbnail())
                .centerCrop()
                .into(((ViewHolder)holder).image);

        ((ViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clickec on id " + item.getId() + "  ;  " + item.getName());
                Intent intent = new Intent(v.getContext(), ItemActivity.class);
                intent.putExtra(Item.TAG, item);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.catalog.size();
    }

    public void sortItemsAlphabetically() {
        Collections.sort(this.catalog, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return i1.getName().compareTo(i2.getName());
            }
        });
    }

    public void sortItemsChronologically() {
        Collections.sort(this.catalog, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                Integer i1_year = new Integer(i1.getYear());
                Integer i2_year = new Integer(i2.getYear());
                return i1_year.compareTo(i2_year);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView itemName;
        TextView itemBrand;
        TextView itemYear;
        LinearLayout parentLayout;
        long idBdd;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemBrand = itemView.findViewById(R.id.itemBrand);
            itemYear = itemView.findViewById(R.id.itemYear);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
