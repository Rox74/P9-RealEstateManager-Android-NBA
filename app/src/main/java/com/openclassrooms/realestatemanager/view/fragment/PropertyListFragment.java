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
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Photo;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.view.adapter.PropertyAdapter;
import com.openclassrooms.realestatemanager.viewmodel.PropertyListViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyListFragment extends Fragment {
    private PropertyListViewModel propertyListViewModel;
    private RecyclerView recyclerView;
    private PropertyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);

        // Configuration du RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);

        // Initialisation du LayoutManager (obligatoire)
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialisation de l'adapteur
        adapter = new PropertyAdapter(property -> {
            if (isTablet()) {
                showPropertyDetailInTablet(property);
            } else {
                navigateToPropertyDetail(property);
            }
        });

        // Lier l'adapteur au RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialisation du ViewModel avec ViewModelFactory
        propertyListViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(PropertyListViewModel.class);

        // Observer les données filtrées
        propertyListViewModel.getFilteredProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.setProperties(properties);
            }
        });

        // Écoute des mises à jour après ajout ou modification d'une propriété
        getParentFragmentManager().setFragmentResultListener("update_property_list", this, (requestKey, bundle) -> {
            boolean updated = bundle.getBoolean("property_updated", false);
            if (updated) {
                refreshPropertyList();
            }
        });

        setupMenu(); // 🔹 Ajout du menu de recherche avec MenuProvider
        return view;
    }

    /**
     * Configure le menu de recherche en utilisant MenuProvider.
     */
    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu); // Charge le menu de recherche
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_search) {
                    openSearchDialog(); // Ouvre la boîte de dialogue de recherche
                    return true;
                } else if (menuItem.getItemId() == R.id.action_reset_search) { // Réinitialise la recherche
                    propertyListViewModel.resetSearch();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    /**
     * Affiche la boîte de dialogue de recherche avancée.
     */
    private void openSearchDialog() {
        SearchDialogFragment searchDialog = new SearchDialogFragment();
        searchDialog.setOnSearchListener(criteria -> {
            propertyListViewModel.searchProperties(criteria); // Applique le filtre
        });
        searchDialog.show(getParentFragmentManager(), "SearchDialog");
    }

    /**
     * Rafraîchit la liste des propriétés après une mise à jour.
     */
    private void refreshPropertyList() {
        propertyListViewModel.getFilteredProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.setProperties(properties);
            }
        });
    }

    /**
     * Vérifie si l'application est exécutée sur une tablette.
     */
    private boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet); // Définir "is_tablet" dans res/values/bools.xml
    }

    /**
     * Ouvre le détail d'une propriété pour les téléphones.
     */
    private void navigateToPropertyDetail(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment) // Utilisez fragment_container pour les téléphones
                .addToBackStack(null)
                .commit();
    }

    /**
     * Ouvre le détail d'une propriété pour les tablettes.
     */
    private void showPropertyDetailInTablet(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.property_detail_container, detailFragment) // Utilisez property_detail_container pour les tablettes
                .commit();
    }
}