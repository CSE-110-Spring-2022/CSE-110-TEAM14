package com.example.cse_110_team14;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private List<ZooData.VertexInfo> searchItems;
    private final List<ZooData.VertexInfo> searchItemsFull;

    public SearchListAdapter(List<ZooData.VertexInfo> searchItems) {
        this.searchItemsFull = searchItems;
        this.searchItems = new ArrayList<>(searchItemsFull);
    }

    public void setSearchItems(List<ZooData.VertexInfo> newSearchItems) {
        this.searchItems.clear();
        this.searchItems = newSearchItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchItem(searchItems.get(position));
        holder.setChecked(searchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ZooData.VertexInfo searchItem;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.search_item_text);
            this.checkBox = itemView.findViewById(R.id.search_item_checkbox);
            this.checkBox.setOnClickListener( view -> {
                    searchItem.checked = checkBox.isChecked();
                Log.d("SearchListAdapter", searchItem.checked + " ");

            });

        }

        public ZooData.VertexInfo getSearchItem() { return searchItem; }

        public void setSearchItem(ZooData.VertexInfo searchItem) {
            this.searchItem = searchItem;
            this.textView.setText(searchItem.name);
        }

        public void setChecked(ZooData.VertexInfo searchItem) {
            this.checkBox.setChecked(searchItem.checked);
        }

    }
}
