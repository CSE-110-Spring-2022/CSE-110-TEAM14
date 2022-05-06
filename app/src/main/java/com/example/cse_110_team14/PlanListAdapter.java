package com.example.cse_110_team14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {

    private List<Pair<String, Integer>> planItems;

    public PlanListAdapter(List<Pair<String, Integer>> planItems) {
        this.planItems = planItems;
    }

    @NonNull
    @Override
    public PlanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plan_list_item, parent, false);

        return new PlanListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanListAdapter.ViewHolder holder, int position) {
        holder.setPlanItem(planItems.get(position));
    }

    @Override
    public int getItemCount() {
        return planItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView planItemText;
        private final TextView planItemDistance;
        private Pair<String, Integer> planItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.planItemText = itemView.findViewById(R.id.plan_item_text);
            this.planItemDistance = itemView.findViewById(R.id.plan_item_distance);
        }

        public void setPlanItem(Pair<String, Integer> planItem) {
            this.planItem = planItem;
            this.planItemText.setText(planItem.first);
            this.planItemDistance.setText(planItem.second.toString() + "ft");
        }
    }
}