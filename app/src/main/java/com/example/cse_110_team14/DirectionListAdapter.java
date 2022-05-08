package com.example.cse_110_team14;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Pretty standard Adapter class for the recycler view which has a bunch of setters and getters
public class DirectionListAdapter extends RecyclerView.Adapter<DirectionListAdapter.ViewHolder> {
    List<String> directions;
    public DirectionListAdapter(List<String> directions) {
        this.directions = directions;

    }


    public void setDirections(List<String> newDirections) {
        this.directions = newDirections;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.visit_animal_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDirection(directions.get(position));
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView directionTextView;
        private String directionString;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            this.directionTextView = itemView.findViewById(R.id.direction);
        }
        public void setDirection(String newDirection) {
            directionString = newDirection;
            this.directionTextView.setText(directionString);
        }

    }
}
