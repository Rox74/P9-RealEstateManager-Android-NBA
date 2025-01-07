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

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<Photo> photos = new ArrayList<>();
    private boolean isEditable;
    private OnPhotoRemovedListener photoRemovedListener;

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
        Glide.with(holder.itemView.getContext()).load(photo.uri).into(holder.photoImageView);
        holder.photoDescription.setText(photo.description);

        // Afficher le bouton de suppression uniquement si en mode édition
        if (isEditable) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                updateList(position);
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    private void updateList(int position) {
        if (position >= 0 && position < photos.size()) {
            photos.remove(position);

            // Si la liste devient vide après suppression
            if (photos.isEmpty()) {
                notifyDataSetChanged(); // Rafraîchit toute la RecyclerView
            } else {
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, photos.size()); // Met à jour les indices restants
            }

            // Notifier le fragment parent de la mise à jour
            if (photoRemovedListener != null) {
                photoRemovedListener.onPhotoRemoved(new ArrayList<>(photos));
            }
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> newPhotos) {
        if (!photos.equals(newPhotos)) { // Évite les mises à jour inutiles
            photos = newPhotos;
            notifyDataSetChanged(); // Force un rafraîchissement global
        }
    }

    public interface OnPhotoRemovedListener {
        void onPhotoRemoved(List<Photo> updatedPhotos);
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private TextView photoDescription;
        private ImageButton deleteButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photo_image_view);
            photoDescription = itemView.findViewById(R.id.photo_description);
            deleteButton = new ImageButton(itemView.getContext());
            deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT);
            deleteButton.setPadding(16, 16, 16, 16);

            // Ajouter dynamiquement le bouton
            ((FrameLayout) itemView).addView(deleteButton);
        }
    }
}