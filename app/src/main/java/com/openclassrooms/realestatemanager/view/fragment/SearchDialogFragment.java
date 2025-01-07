package com.openclassrooms.realestatemanager.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SearchDialogFragment extends DialogFragment {

    private EditText minPriceEditText, maxPriceEditText, minSurfaceEditText, maxSurfaceEditText;
    private EditText minRoomsEditText, minPhotosEditText;
    private EditText locationEditText;
    private Button searchButton, cancelButton;

    private OnSearchListener searchListener;

    public interface OnSearchListener {
        void onSearch(SearchCriteria criteria);
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.searchListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_search, null);

        // Initialisation des champs de recherche
        minPriceEditText = view.findViewById(R.id.edit_text_min_price);
        maxPriceEditText = view.findViewById(R.id.edit_text_max_price);
        minSurfaceEditText = view.findViewById(R.id.edit_text_min_surface);
        maxSurfaceEditText = view.findViewById(R.id.edit_text_max_surface);
        minRoomsEditText = view.findViewById(R.id.edit_text_min_rooms);
        minPhotosEditText = view.findViewById(R.id.edit_text_min_photos);
        locationEditText = view.findViewById(R.id.edit_text_location);
        searchButton = view.findViewById(R.id.button_search);
        cancelButton = view.findViewById(R.id.button_cancel);

        // Gestion du bouton de recherche
        searchButton.setOnClickListener(v -> {
            SearchCriteria criteria = new SearchCriteria(
                    safeParseDouble(minPriceEditText.getText().toString()),
                    safeParseDouble(maxPriceEditText.getText().toString()),
                    safeParseDouble(minSurfaceEditText.getText().toString()),
                    safeParseDouble(maxSurfaceEditText.getText().toString()),
                    safeParseInt(minRoomsEditText.getText().toString()),
                    safeParseInt(minPhotosEditText.getText().toString()),
                    locationEditText.getText().toString().trim()
            );

            if (searchListener != null) {
                searchListener.onSearch(criteria);
            }
            dismiss();
        });

        // Gestion du bouton Annuler
        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        builder.setTitle("Advanced Search");

        return builder.create();
    }

    /**
     * Convertit une chaîne en double, en évitant les erreurs de conversion.
     */
    private double safeParseDouble(String text) {
        try {
            return text != null && !text.trim().isEmpty() ? Double.parseDouble(text.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0; // Retourne 0 en cas d'erreur
        }
    }

    /**
     * Convertit une chaîne en int, en évitant les erreurs de conversion.
     */
    private int safeParseInt(String text) {
        try {
            return text != null && !text.trim().isEmpty() ? Integer.parseInt(text.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0; // Retourne 0 en cas d'erreur
        }
    }
}