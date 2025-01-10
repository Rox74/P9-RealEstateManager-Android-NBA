package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.view.adapter.PropertyAdapter;
import com.openclassrooms.realestatemanager.viewmodel.PropertyListViewModel;

/**
 * Fragment responsible for displaying a list of properties.
 * Supports both tablet and phone layouts and provides search functionality.
 */
public class PropertyListFragment extends Fragment {
    private PropertyListViewModel propertyListViewModel;
    private RecyclerView recyclerView;
    private PropertyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Mandatory LayoutManager setup

        // Initialize adapter with click listener
        adapter = new PropertyAdapter(property -> {
            if (isTablet()) {
                showPropertyDetailInTablet(property);
            } else {
                navigateToPropertyDetail(property);
            }
        });

        recyclerView.setAdapter(adapter); // Attach adapter to RecyclerView

        // Initialize ViewModel using ViewModelFactory
        propertyListViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(PropertyListViewModel.class);

        // Observe filtered property list from ViewModel
        propertyListViewModel.getFilteredProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.setProperties(properties);
            }
        });

        // Listen for property updates after adding or modifying a property
        getParentFragmentManager().setFragmentResultListener("update_property_list", this, (requestKey, bundle) -> {
            boolean updated = bundle.getBoolean("property_updated", false);
            if (updated) {
                refreshPropertyList();
            }
        });

        setupMenu(); // Adds the search menu using MenuProvider
        return view;
    }

    /**
     * Sets up the search menu using MenuProvider.
     */
    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu); // Load search menu
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_search) {
                    openSearchDialog(); // Open the search dialog
                    return true;
                } else if (menuItem.getItemId() == R.id.action_reset_search) { // Reset search filters
                    propertyListViewModel.resetSearch();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    /**
     * Displays the advanced search dialog.
     */
    private void openSearchDialog() {
        SearchDialogFragment searchDialog = new SearchDialogFragment();
        searchDialog.setOnSearchListener(criteria -> {
            propertyListViewModel.searchProperties(criteria); // Apply search filters
        });
        searchDialog.show(getParentFragmentManager(), "SearchDialog");
    }

    /**
     * Refreshes the property list after an update.
     */
    private void refreshPropertyList() {
        propertyListViewModel.getFilteredProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.setProperties(properties);
            }
        });
    }

    /**
     * Checks if the application is running on a tablet.
     *
     * @return True if running on a tablet, false otherwise.
     */
    private boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet); // Defined in res/values/bools.xml
    }

    /**
     * Opens the property detail view for phones.
     *
     * @param property The selected property.
     */
    private void navigateToPropertyDetail(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment) // Use fragment_container for phones
                .addToBackStack(null)
                .commit();
    }

    /**
     * Opens the property detail view for tablets.
     *
     * @param property The selected property.
     */
    private void showPropertyDetailInTablet(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.property_detail_container, detailFragment) // Use property_detail_container for tablets
                .commit();
    }
}