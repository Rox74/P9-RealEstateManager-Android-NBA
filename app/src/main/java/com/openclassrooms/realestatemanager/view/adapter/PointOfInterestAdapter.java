package com.openclassrooms.realestatemanager.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying a list of Points of Interest (POI) in a RecyclerView.
 * Supports both view-only mode and editable mode where users can remove POIs.
 */
public class PointOfInterestAdapter extends RecyclerView.Adapter<PointOfInterestAdapter.PointOfInterestViewHolder> {

    private final List<PointOfInterest> pointsOfInterest;
    private final boolean isEditable; // Determines if the delete button is visible
    private final OnPointOfInterestRemovedListener pointOfInterestRemovedListener; // Listener to notify parent component

    /**
     * Constructor for PointOfInterestAdapter.
     *
     * @param pointsOfInterest            List of Points of Interest to display.
     * @param isEditable                  Determines if the delete button should be visible.
     * @param listener                     Listener to notify when a POI is removed.
     */
    public PointOfInterestAdapter(List<PointOfInterest> pointsOfInterest, boolean isEditable, OnPointOfInterestRemovedListener listener) {
        this.pointsOfInterest = pointsOfInterest;
        this.isEditable = isEditable;
        this.pointOfInterestRemovedListener = listener;
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

        // Set name and type of the Point of Interest
        holder.nameTextView.setText(pointOfInterest.name);
        holder.typeTextView.setText(pointOfInterest.type);

        // Show or hide delete button based on edit mode
        if (isEditable) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> updateList(position));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the list of Points of Interest and refreshes the RecyclerView.
     *
     * @param updatedList The updated list of Points of Interest.
     */
    public void setPointsOfInterest(List<PointOfInterest> updatedList) {
        pointsOfInterest.clear();
        pointsOfInterest.addAll(updatedList);
        notifyDataSetChanged();
    }

    /**
     * Removes a POI from the list and updates the RecyclerView.
     *
     * @param position The position of the POI to remove.
     */
    private void updateList(int position) {
        if (position >= 0 && position < pointsOfInterest.size()) {
            List<PointOfInterest> updatedList = new ArrayList<>(pointsOfInterest); // Clone list before modification
            updatedList.remove(position);
            setPointsOfInterest(updatedList); // Update display

            // Notify parent component of the updated POI list
            if (pointOfInterestRemovedListener != null) {
                pointOfInterestRemovedListener.onPointOfInterestRemoved(updatedList);
            }
        }
    }

    @Override
    public int getItemCount() {
        return pointsOfInterest.size();
    }

    /**
     * Interface to notify parent components when a POI is removed.
     */
    public interface OnPointOfInterestRemovedListener {
        void onPointOfInterestRemoved(List<PointOfInterest> updatedPointsOfInterest);
    }

    /**
     * ViewHolder class for managing the layout of a Point of Interest item.
     */
    static class PointOfInterestViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView typeTextView;
        ImageButton deleteButton;

        public PointOfInterestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_poi_name);
            typeTextView = itemView.findViewById(R.id.text_view_poi_type);
            deleteButton = new ImageButton(itemView.getContext());

            // Set up delete button properties
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setPadding(16, 16, 16, 16);

            // Dynamically add the delete button to the layout
            ((LinearLayout) itemView).addView(deleteButton);
        }
    }
}