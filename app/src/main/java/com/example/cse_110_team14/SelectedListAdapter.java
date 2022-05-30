package com.example.cse_110_team14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectedListAdapter extends RecyclerView.Adapter<SelectedListAdapter.selectedViewHolder> {
    private List<String> selectedItems;

    public SelectedListAdapter(List<String> selectedItems) {
        this.selectedItems = new ArrayList<>(selectedItems);
    }

    public void setSelectedItems(List<String> newSelectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(newSelectedItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public selectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_list_item, parent, false);

        return new selectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull selectedViewHolder holder, int position) {
        holder.setSelectedItem(selectedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class selectedViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;
        private String selectedItem;

        public selectedViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.selected_item_text);
        }

        public void setSelectedItem(String selectedItem) {
            this.selectedItem = selectedItem;
            this.textView.setText(selectedItem);
        }

        public String getSelectedItem() { return selectedItem; }
    }
}
