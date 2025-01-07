package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;

import java.util.Locale;

public class LoanSimulatorFragment extends Fragment {

    private EditText priceEditText, downPaymentEditText, interestRateEditText, durationEditText;
    private Button calculateButton;
    private TextView resultTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_simulator, container, false);

        // Initialize views
        priceEditText = view.findViewById(R.id.edit_text_price);
        downPaymentEditText = view.findViewById(R.id.edit_text_down_payment);
        interestRateEditText = view.findViewById(R.id.edit_text_interest_rate);
        durationEditText = view.findViewById(R.id.edit_text_duration);
        calculateButton = view.findViewById(R.id.button_calculate);
        resultTextView = view.findViewById(R.id.text_view_result);

        // Handle "Calculate" button click
        calculateButton.setOnClickListener(v -> calculateLoan());

        return view;
    }

    private void calculateLoan() {
        // Retrieve input values
        String priceStr = priceEditText.getText().toString().trim();
        String downPaymentStr = downPaymentEditText.getText().toString().trim();
        String interestRateStr = interestRateEditText.getText().toString().trim();
        String durationStr = durationEditText.getText().toString().trim();

        // Validate user input
        if (priceStr.isEmpty() || downPaymentStr.isEmpty() || interestRateStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        double downPayment = Double.parseDouble(downPaymentStr);
        double interestRate = Double.parseDouble(interestRateStr) / 100; // Convert to percentage
        int duration = Integer.parseInt(durationStr);

        if (downPayment >= price) {
            Toast.makeText(getContext(), "Down payment must be lower than property price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate loan amount
        double loanAmount = price - downPayment;
        // Monthly interest rate
        double monthlyRate = interestRate / 12;
        // Total number of months
        int numberOfMonths = duration * 12;

        // Mortgage formula calculation
        double monthlyPayment = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -numberOfMonths));
        double totalCost = monthlyPayment * numberOfMonths;
        double totalInterest = totalCost - loanAmount;

        // Display result
        String resultText = String.format(
                Locale.US,
                "Loan Amount: %.2f $\nMonthly Payment: %.2f $\nTotal Cost: %.2f $\nTotal Interest: %.2f $",
                loanAmount, monthlyPayment, totalCost, totalInterest
        );
        resultTextView.setText(resultText);
    }
}