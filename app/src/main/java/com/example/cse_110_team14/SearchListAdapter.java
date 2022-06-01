package com.example.cse_110_team14;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private List<ZooData.VertexInfo> searchItems;
    public final List<ZooData.VertexInfo> searchItemsFull;

    public SAStorage sas;

    public ItemsDao itemsDao;

    public void setSas(SAStorage sas){this.sas = sas;}

    public SearchListAdapter(List<ZooData.VertexInfo> searchItems) {
        this.searchItemsFull = searchItems;
        this.searchItems = new ArrayList<>(searchItemsFull);
    }

    public void setSearchItems(List<ZooData.VertexInfo> newSearchItems) {
        this.searchItems.clear();
        this.searchItems.addAll(newSearchItems);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ZooData.VertexInfo searchItem;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.search_item_text);
            this.checkBox = itemView.findViewById(R.id.search_item_checkbox);
            this.checkBox.setOnClickListener( view -> {

                searchItem.checked = checkBox.isChecked();
                Log.d("SearchListAdapter", searchItem.checked + " ");
                if(itemsDao != null) {
                    if (checkBox.isChecked()){
                        itemsDao.insert(new CheckedName(searchItem.name));
                    }
                    else
                        itemsDao.delete(searchItem.name);
                }
                else{
                    System.out.println("???????????????");
                }

                if(sas != null) {
                    int count = 0;
                    for(ZooData.VertexInfo v : sas.sa.presenter.getExhibitList())
                        if(v.checked)
                            count ++;
                    sas.setPlanCount(count);
                    sas.updateSelectedList(searchItem);
                }
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