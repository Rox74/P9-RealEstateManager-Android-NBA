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

/**
 * Dialog fragment for performing an advanced property search.
 * Users can specify search criteria such as price, surface area, number of rooms, and location.
 */
public class SearchDialogFragment extends DialogFragment {

    private EditText minPriceEditText, maxPriceEditText, minSurfaceEditText, maxSurfaceEditText;
    private EditText minRoomsEditText, minPhotosEditText;
    private EditText locationEditText;
    private Button searchButton, cancelButton;

    private OnSearchListener searchListener;

    /**
     * Interface for handling search actions.
     */
    public interface OnSearchListener {
        void onSearch(SearchCriteria criteria);
    }

    /**
     * Sets the listener for handling search actions.
     *
     * @param listener The listener to be notified when a search is performed.
     */
    public void setOnSearchListener(OnSearchListener listener) {
        this.searchListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_search, null);

        // Initialize search fields
        minPriceEditText = view.findViewById(R.id.edit_text_min_price);
        maxPriceEditText = view.findViewById(R.id.edit_text_max_price);
        minSurfaceEditText = view.findViewById(R.id.edit_text_min_surface);
        maxSurfaceEditText = view.findViewById(R.id.edit_text_max_surface);
        minRoomsEditText = view.findViewById(R.id.edit_text_min_rooms);
        minPhotosEditText = view.findViewById(R.id.edit_text_min_photos);
        locationEditText = view.findViewById(R.id.edit_text_location);
        searchButton = view.findViewById(R.id.button_search);
        cancelButton = view.findViewById(R.id.button_cancel);

        // Handle search button click
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
                searchListener.onSearch(criteria); // Pass search criteria to listener
            }
            dismiss();
        });

        // Handle cancel button click
        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        builder.setTitle("Advanced Search");

        return builder.create();
    }

    /**
     * Safely parses a string into a double to prevent conversion errors.
     *
     * @param text The input string to convert.
     * @return The parsed double value, or 0 if conversion fails.
     */
    private double safeParseDouble(String text) {
        try {
            return text != null && !text.trim().isEmpty() ? Double.parseDouble(text.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0; // Returns 0 in case of an error
        }
    }

    /**
     * Safely parses a string into an integer to prevent conversion errors.
     *
     * @param text The input string to convert.
     * @return The parsed integer value, or 0 if conversion fails.
     */
    private int safeParseInt(String text) {
        try {
            return text != null && !text.trim().isEmpty() ? Integer.parseInt(text.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0; // Returns 0 in case of an error
        }
    }
}