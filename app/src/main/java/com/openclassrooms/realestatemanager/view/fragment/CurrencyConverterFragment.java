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

/**
 * Fragment for currency conversion between USD and EUR.
 * Allows users to input an amount and convert it using predefined exchange rates.
 */
public class CurrencyConverterFragment extends Fragment {

    private EditText amountEditText;
    private TextView resultTextView;
    private Button convertButton;
    private Spinner currencySpinner;

    // Conversion options for the Spinner dropdown
    private static final String[] conversionOptions = {
            "USD → EUR",
            "EUR → USD"
    };

    private boolean isDollarToEuro = true; // Default conversion direction: USD → EUR

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_converter, container, false);

        // Initialize UI elements
        amountEditText = view.findViewById(R.id.edit_text_amount);
        resultTextView = view.findViewById(R.id.text_view_result);
        convertButton = view.findViewById(R.id.button_convert);
        currencySpinner = view.findViewById(R.id.spinner_currency);

        // Configure Spinner dropdown with currency conversion options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, conversionOptions);
        currencySpinner.setAdapter(adapter);
        currencySpinner.setSelection(0); // Default selection: USD → EUR

        // Handle selection changes in the Spinner
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isDollarToEuro = position == 0; // If "USD → EUR" is selected, set true
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isDollarToEuro = true; // Default to USD → EUR if nothing is selected
            }
        });

        // Handle the conversion button click event
        convertButton.setOnClickListener(v -> convertCurrency());

        return view;
    }

    /**
     * Converts the input currency amount based on the selected conversion direction.
     * If no amount is entered, it displays an error message.
     */
    private void convertCurrency() {
        String input = amountEditText.getText().toString();

        // Validate input: Ensure an amount is entered
        if (input.isEmpty()) {
            resultTextView.setText("Please enter an amount");
            return;
        }

        double amount = Double.parseDouble(input);
        double convertedAmount;

        // Perform currency conversion based on the selected conversion type
        if (isDollarToEuro) {
            convertedAmount = Utils.convertDollarToEuro(amount);
            resultTextView.setText(String.format(Locale.US, "%.2f USD = %.2f EUR", amount, convertedAmount));
        } else {
            convertedAmount = Utils.convertEuroToDollar(amount);
            resultTextView.setText(String.format(Locale.US, "%.2f EUR = %.2f USD", amount, convertedAmount));
        }
    }
}