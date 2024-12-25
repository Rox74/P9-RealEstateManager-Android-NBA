package com.openclassrooms.realestatemanager.view.adapter;

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

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> properties = new ArrayList<>();
    private final OnPropertyClickListener listener;

    public interface OnPropertyClickListener {
        void onPropertyClick(Property property);
    }

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
        holder.bind(property, listener);
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
        notifyDataSetChanged();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeTextView;
        private final TextView locationTextView;
        private final TextView priceTextView;
        private final ImageView propertyImageView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Liez les éléments de la vue avec leurs IDs respectifs
            typeTextView = itemView.findViewById(R.id.property_type);
            locationTextView = itemView.findViewById(R.id.property_location);
            priceTextView = itemView.findViewById(R.id.property_price);
            propertyImageView = itemView.findViewById(R.id.property_image);
        }

        public void bind(Property property, OnPropertyClickListener listener) {
            typeTextView.setText(property.type);
            locationTextView.setText(property.address != null
                    ? property.address.city + ", " + property.address.country
                    : "Location not available");
            priceTextView.setText("$" + property.price);
            // Placeholder : chargez l'image si disponible (exemple avec Glide)
            Glide.with(itemView.getContext())
                    .load(property.photos != null && !property.photos.isEmpty() ? property.photos.get(0).uri : R.drawable.ic_placeholder)
                    .into(propertyImageView);

            // Définir le comportement au clic
            itemView.setOnClickListener(v -> listener.onPropertyClick(property));
        }
    }
}