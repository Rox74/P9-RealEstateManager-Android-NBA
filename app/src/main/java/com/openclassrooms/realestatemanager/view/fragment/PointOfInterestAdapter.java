package com.openclassrooms.realestatemanager.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;

import java.util.List;

public class PointOfInterestAdapter extends RecyclerView.Adapter<PointOfInterestAdapter.PointOfInterestViewHolder> {

    private final List<PointOfInterest> pointsOfInterest;

    public PointOfInterestAdapter(List<PointOfInterest> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    @NonNull
    @Override
    public PointOfInterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_of_interest, parent, false);
        return new PointOfInterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointOfInterestViewHolder holder, int position) {
        PointOfInterest pointOfInterest = pointsOfInterest.get(position);
        holder.nameTextView.setText(pointOfInterest.name);
        holder.typeTextView.setText(pointOfInterest.type);
    }

    @Override
    public int getItemCount() {
        return pointsOfInterest.size();
    }

    static class PointOfInterestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView typeTextView;

        public PointOfInterestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_poi_name);
            typeTextView = itemView.findViewById(R.id.text_view_poi_type);
        }
    }
}