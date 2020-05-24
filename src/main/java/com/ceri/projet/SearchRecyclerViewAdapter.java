package com.ceri.projet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerViewAdapter implements Filterable {

    private List<Item> result;
    private SearchFilter filter;

    public SearchRecyclerViewAdapter(Context context, List<Item> catalog) {
        super(context, catalog);

        this.result = new ArrayList<>(this.catalog);
        this.filter = new SearchFilter();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = this.result.get(position);

        ((ViewHolder) holder).itemName.setText(item.getName());
        ((ViewHolder) holder).itemBrand.setText(item.getBrand());
        ((ViewHolder) holder).itemYear.setText(String.valueOf(item.getYear()));
        ((ViewHolder)holder).idBdd = item.getId();

        GlideBny.loadFromCache(holder.itemView, item.getThumbnail(), ((ViewHolder)holder).image, GlideBny.Center.CROP);

        ((ViewHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on id " + item.getId() + "  ;  " + item.getName());
                Intent intent = new Intent(v.getContext(), ItemActivity.class);
                intent.putExtra(Item.TAG, item);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.result.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();
            List<Item> priority = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
                filteredList.addAll(SearchRecyclerViewAdapter.this.catalog);
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Item item : SearchRecyclerViewAdapter.this.catalog) {
                    if(item.getName().toLowerCase().startsWith(filterPattern)) {
                        priority.add(item);
                        continue;
                    }
                    if(item.getBrand().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(item);
                        continue;
                    }
                    if(String.valueOf(item.getYear()).startsWith(filterPattern)) {
                        filteredList.add(item);
                        continue;
                    }
                    for(String categorie : item.getCategories()) {
                        if(categorie.toLowerCase().equals(filterPattern) && !filteredList.contains(item)) {
                            filteredList.add(item);
                            continue;
                        }
                        if(categorie.toLowerCase().startsWith(filterPattern) && !filteredList.contains(item)) {
                            filteredList.add(item);
                            continue;
                        }
                    }
                }
                filteredList.addAll(0, priority);
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            SearchRecyclerViewAdapter.this.result.clear();
            SearchRecyclerViewAdapter.this.result.addAll((List)results.values);

            SearchRecyclerViewAdapter.this.notifyDataSetChanged();
        }
    }
}
