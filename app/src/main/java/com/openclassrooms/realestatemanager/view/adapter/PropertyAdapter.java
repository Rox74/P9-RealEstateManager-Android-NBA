package com.openclassrooms.realestatemanager.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying a list of properties in a RecyclerView.
 * Supports item selection and interaction via an OnPropertyClickListener.
 */
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> properties = new ArrayList<>(); // List of properties to display
    private final OnPropertyClickListener listener; // Click listener for property selection
    private int selectedPosition = RecyclerView.NO_POSITION; // Tracks the currently selected position

    /**
     * Interface for handling property click events.
     */
    public interface OnPropertyClickListener {
        /**
         * Called when a property item is clicked.
         *
         * @param property The clicked property.
         */
        void onPropertyClick(Property property);
    }

    /**
     * Constructor for PropertyAdapter.
     *
     * @param listener Listener to handle property click events.
     */
    public PropertyAdapter(OnPropertyClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = properties.get(position);

        // Bind property data to the view
        holder.bind(property, listener, position == selectedPosition);

        // Handle click events for selecting properties
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();

            // Update selection state in RecyclerView
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Notify listener of the selection
            listener.onPropertyClick(property);
        });
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    /**
     * Updates the property list and refreshes the RecyclerView.
     *
     * @param properties The new list of properties.
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for managing the layout of a property item.
     */
    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeTextView;
        private final TextView locationTextView;
        private final TextView priceTextView;
        private final ImageView propertyImageView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Link view elements to their respective IDs
            typeTextView = itemView.findViewById(R.id.property_type);
            locationTextView = itemView.findViewById(R.id.property_location);
            priceTextView = itemView.findViewById(R.id.property_price);
            propertyImageView = itemView.findViewById(R.id.property_image);
        }

        /**
         * Binds property data to the view and handles UI updates.
         *
         * @param property   The property to display.
         * @param listener   Click listener for the property.
         * @param isSelected Whether the item is currently selected.
         */
        public void bind(Property property, OnPropertyClickListener listener, boolean isSelected) {
            typeTextView.setText(property.type);
            locationTextView.setText(property.address != null
                    ? property.address.city + ", " + property.address.country
                    : "Location not available");
            priceTextView.setText("$" + property.price);

            // Load property image if available, otherwise use placeholder
            Glide.with(itemView.getContext())
                    .load(property.photos != null && !property.photos.isEmpty() ? property.photos.get(0).uri : R.drawable.ic_placeholder)
                    .into(propertyImageView);

            // Update UI based on selection state
            itemView.setSelected(isSelected);
            priceTextView.setTextColor(isSelected
                    ? Color.parseColor("#005f73") // Dark blue for selected text
                    : Color.parseColor("#0077b6")); // Bright blue as default
        }
    }
}