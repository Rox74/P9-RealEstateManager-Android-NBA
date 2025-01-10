package com.openclassrooms.realestatemanager.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying a list of photos in a RecyclerView.
 * Supports both view-only mode and editable mode where users can remove photos.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos = new ArrayList<>();
    private boolean isEditable; // Determines if the delete button is visible
    private OnPhotoRemovedListener photoRemovedListener; // Listener to notify parent component

    /**
     * Constructor for PhotoAdapter.
     *
     * @param isEditable           Determines if the delete button should be visible.
     * @param photoRemovedListener Listener to notify when a photo is removed.
     */
    public PhotoAdapter(boolean isEditable, OnPhotoRemovedListener photoRemovedListener) {
        this.isEditable = isEditable;
        this.photoRemovedListener = photoRemovedListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);

        // Load image into ImageView using Glide
        Glide.with(holder.itemView.getContext()).load(photo.uri).into(holder.photoImageView);

        // Set photo description
        holder.photoDescription.setText(photo.description);

        // Show or hide delete button based on edit mode
        if (isEditable) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> updateList(position));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    /**
     * Removes a photo from the list and updates the RecyclerView.
     *
     * @param position The position of the photo to remove.
     */
    private void updateList(int position) {
        if (position >= 0 && position < photos.size()) {
            photos.remove(position);

            // If the list becomes empty, refresh the entire RecyclerView
            if (photos.isEmpty()) {
                notifyDataSetChanged();
            } else {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, photos.size()); // Update indices of remaining items
            }

            // Notify parent component of the updated photo list
            if (photoRemovedListener != null) {
                photoRemovedListener.onPhotoRemoved(new ArrayList<>(photos));
            }
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    /**
     * Updates the photo list and refreshes the RecyclerView if necessary.
     *
     * @param newPhotos The new list of photos.
     */
    public void setPhotos(List<Photo> newPhotos) {
        if (!photos.equals(newPhotos)) { // Prevents unnecessary updates
            photos = newPhotos;
            notifyDataSetChanged(); // Force a full refresh
        }
    }

    /**
     * Interface to notify parent components when a photo is removed.
     */
    public interface OnPhotoRemovedListener {
        void onPhotoRemoved(List<Photo> updatedPhotos);
    }

    /**
     * ViewHolder class for managing the layout of a photo item.
     */
    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private TextView photoDescription;
        private ImageButton deleteButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
            photoDescription = itemView.findViewById(R.id.photo_description);
            deleteButton = new ImageButton(itemView.getContext());

            // Set up delete button properties
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setPadding(16, 16, 16, 16);

            // Dynamically add the delete button to the layout
            ((FrameLayout) itemView).addView(deleteButton);
        }
    }
}