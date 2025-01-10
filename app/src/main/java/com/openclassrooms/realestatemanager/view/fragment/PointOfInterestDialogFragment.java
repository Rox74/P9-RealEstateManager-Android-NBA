package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;
import com.openclassrooms.realestatemanager.view.adapter.PointOfInterestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * DialogFragment for displaying a list of points of interest (POI) related to a property.
 * This dialog presents a RecyclerView with POI information and a close button.
 */
public class PointOfInterestDialogFragment extends DialogFragment {

    private static final String ARG_POINTS_OF_INTEREST = "points_of_interest"; // Key for arguments bundle
    private List<PointOfInterest> pointsOfInterest; // List of points of interest

    /**
     * Creates a new instance of PointOfInterestDialogFragment with the provided POI list.
     *
     * @param pointsOfInterest List of points of interest to display.
     * @return A new instance of PointOfInterestDialogFragment.
     */
    public static PointOfInterestDialogFragment newInstance(List<PointOfInterest> pointsOfInterest) {
        PointOfInterestDialogFragment fragment = new PointOfInterestDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_POINTS_OF_INTEREST, new ArrayList<>(pointsOfInterest));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the dialog layout
        View view = inflater.inflate(R.layout.dialog_point_of_interest, container, false);

        // Initialize UI components
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_poi);
        Button closeButton = view.findViewById(R.id.button_close_poi);

        // Retrieve the list of points of interest from the arguments
        if (getArguments() != null) {
            pointsOfInterest = getArguments().getParcelableArrayList(ARG_POINTS_OF_INTEREST);
        }

        // Configure RecyclerView with a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter for displaying POIs (not editable mode)
        PointOfInterestAdapter adapter = new PointOfInterestAdapter(pointsOfInterest, false, null);
        recyclerView.setAdapter(adapter);

        // Handle close button click to dismiss the dialog
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}