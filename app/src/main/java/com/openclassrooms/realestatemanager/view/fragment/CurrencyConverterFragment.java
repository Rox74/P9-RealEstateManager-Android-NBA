package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Locale;

public class CurrencyConverterFragment extends Fragment {

    private EditText amountEditText;
    private TextView resultTextView;
    private Button convertButton;
    private Spinner currencySpinner;

    private static final String[] conversionOptions = {
            "USD → EUR",
            "EUR → USD"
    };

    private boolean isDollarToEuro = true; // Default: USD → EUR

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_converter, container, false);

        // Initialize views
        amountEditText = view.findViewById(R.id.edit_text_amount);
        resultTextView = view.findViewById(R.id.text_view_result);
        convertButton = view.findViewById(R.id.button_convert);
        currencySpinner = view.findViewById(R.id.spinner_currency);

        // Configure Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, conversionOptions);
        currencySpinner.setAdapter(adapter);
        currencySpinner.setSelection(0); // Default: USD → EUR

        // Handle selection changes in Spinner
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isDollarToEuro = position == 0; // If "USD → EUR" is selected, set true
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isDollarToEuro = true; // Default: USD → EUR
            }
        });

        // Handle conversion button click
        convertButton.setOnClickListener(v -> convertCurrency());

        return view;
    }

    private void convertCurrency() {
        String input = amountEditText.getText().toString();
        if (input.isEmpty()) {
            resultTextView.setText("Please enter an amount");
            return;
        }

        double amount = Double.parseDouble(input);
        double convertedAmount;

        if (isDollarToEuro) {
            convertedAmount = Utils.convertDollarToEuro(amount);
            resultTextView.setText(String.format(Locale.US, "%.2f USD = %.2f EUR", amount, convertedAmount));
        } else {
            convertedAmount = Utils.convertEuroToDollar(amount);
            resultTextView.setText(String.format(Locale.US, "%.2f EUR = %.2f USD", amount, convertedAmount));
        }
    }
}