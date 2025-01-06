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

public class PointOfInterestAdapter extends RecyclerView.Adapter<PointOfInterestAdapter.PointOfInterestViewHolder> {
    private final List<PointOfInterest> pointsOfInterest;
    private final boolean isEditable;
    private final OnPointOfInterestRemovedListener pointOfInterestRemovedListener;

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
        holder.nameTextView.setText(pointOfInterest.name);
        holder.typeTextView.setText(pointOfInterest.type);

        if (isEditable) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                updateList(position);
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    public void setPointsOfInterest(List<PointOfInterest> updatedList) {
        pointsOfInterest.clear();
        pointsOfInterest.addAll(updatedList);
        notifyDataSetChanged();
    }

    private void updateList(int position) {
        if (position >= 0 && position < pointsOfInterest.size()) {
            List<PointOfInterest> updatedList = new ArrayList<>(pointsOfInterest); // Cloner la liste avant modification
            updatedList.remove(position);
            setPointsOfInterest(updatedList); // Mettre à jour l'affichage

            // Notifier le fragment parent de la mise à jour
            if (pointOfInterestRemovedListener != null) {
                pointOfInterestRemovedListener.onPointOfInterestRemoved(updatedList);
            }
        }
    }

    @Override
    public int getItemCount() {
        return pointsOfInterest.size();
    }

    public interface OnPointOfInterestRemovedListener {
        void onPointOfInterestRemoved(List<PointOfInterest> updatedPointsOfInterest);
    }

    static class PointOfInterestViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView typeTextView;
        ImageButton deleteButton;

        public PointOfInterestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_poi_name);
            typeTextView = itemView.findViewById(R.id.text_view_poi_type);
            deleteButton = new ImageButton(itemView.getContext());
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setPadding(16, 16, 16, 16);

            // Ajouter dynamiquement le bouton
            ((LinearLayout) itemView).addView(deleteButton);
        }
    }
}