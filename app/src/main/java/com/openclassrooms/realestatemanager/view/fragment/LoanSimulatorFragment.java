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

/**
 * Fragment for simulating a real estate loan.
 * Allows users to input property price, down payment, interest rate, and duration
 * to calculate the monthly mortgage payment and total loan cost.
 */
public class LoanSimulatorFragment extends Fragment {

    private EditText priceEditText, downPaymentEditText, interestRateEditText, durationEditText;
    private Button calculateButton;
    private TextView resultTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_simulator, container, false);

        // Initialize UI components
        priceEditText = view.findViewById(R.id.edit_text_price);
        downPaymentEditText = view.findViewById(R.id.edit_text_down_payment);
        interestRateEditText = view.findViewById(R.id.edit_text_interest_rate);
        durationEditText = view.findViewById(R.id.edit_text_duration);
        calculateButton = view.findViewById(R.id.button_calculate);
        resultTextView = view.findViewById(R.id.text_view_result);

        // Set up click listener for the "Calculate" button
        calculateButton.setOnClickListener(v -> calculateLoan());

        return view;
    }

    /**
     * Performs loan calculations based on user inputs.
     * Uses the mortgage formula to compute monthly payments, total cost, and total interest.
     */
    private void calculateLoan() {
        // Retrieve input values from EditText fields
        String priceStr = priceEditText.getText().toString().trim();
        String downPaymentStr = downPaymentEditText.getText().toString().trim();
        String interestRateStr = interestRateEditText.getText().toString().trim();
        String durationStr = durationEditText.getText().toString().trim();

        // Validate that all fields are filled
        if (priceStr.isEmpty() || downPaymentStr.isEmpty() || interestRateStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert user input into numerical values
        double price = Double.parseDouble(priceStr);
        double downPayment = Double.parseDouble(downPaymentStr);
        double interestRate = Double.parseDouble(interestRateStr) / 100; // Convert to decimal percentage
        int duration = Integer.parseInt(durationStr); // Loan duration in years

        // Validate that the down payment is less than the property price
        if (downPayment >= price) {
            Toast.makeText(getContext(), "Down payment must be lower than property price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate loan amount (total price minus down payment)
        double loanAmount = price - downPayment;

        // Convert annual interest rate to monthly rate
        double monthlyRate = interestRate / 12;

        // Total number of months in the loan term
        int numberOfMonths = duration * 12;

        // Calculate monthly payment using the mortgage formula
        double monthlyPayment = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -numberOfMonths));

        // Calculate total loan cost over the duration
        double totalCost = monthlyPayment * numberOfMonths;

        // Calculate total interest paid over the loan duration
        double totalInterest = totalCost - loanAmount;

        // Display calculated results in the TextView
        String resultText = String.format(
                Locale.US,
                "Loan Amount: %.2f $\nMonthly Payment: %.2f $\nTotal Cost: %.2f $\nTotal Interest: %.2f $",
                loanAmount, monthlyPayment, totalCost, totalInterest
        );
        resultTextView.setText(resultText);
    }
}