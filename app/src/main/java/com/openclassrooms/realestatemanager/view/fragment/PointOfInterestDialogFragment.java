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

public class PointOfInterestDialogFragment extends DialogFragment {

    private static final String ARG_POINTS_OF_INTEREST = "points_of_interest";
    private List<PointOfInterest> pointsOfInterest;

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
        View view = inflater.inflate(R.layout.dialog_point_of_interest, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_poi);
        Button closeButton = view.findViewById(R.id.button_close_poi);

        if (getArguments() != null) {
            pointsOfInterest = getArguments().getParcelableArrayList(ARG_POINTS_OF_INTEREST);
        }

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PointOfInterestAdapter adapter = new PointOfInterestAdapter(pointsOfInterest, false, null);
        recyclerView.setAdapter(adapter);

        // Bouton de fermeture
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}