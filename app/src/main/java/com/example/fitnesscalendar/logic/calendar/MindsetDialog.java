package com.example.fitnesscalendar.logic.calendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.MindsetDialogBinding;
import com.example.fitnesscalendar.entities.Quote;

public class MindsetDialog extends DialogFragment {

    private MindsetDialogBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MindsetDialogBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            getDialog().getWindow().setDimAmount(0.5f);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayRandomQuote();
    }

    private void displayRandomQuote() {
        try {
        java.io.InputStream is = getResources().openRawResource(R.raw.quotes);
        java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        String jsonString = scanner.hasNext() ? scanner.next() : "";

        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.List<Quote>>(){}.getType();
        java.util.List<Quote> quotes = gson.fromJson(jsonString, listType);

            if (quotes != null && !quotes.isEmpty()) {
                int randomIndex = new java.util.Random().nextInt(quotes.size());
                Quote randomQuote = quotes.get(randomIndex);

                binding.quoteTextView.setText("\"" + randomQuote.text + "\"");
                binding.authorTextView.setText("- " + randomQuote.author);
            };
        } catch (Exception e) {
            e.printStackTrace();
            binding.quoteTextView.setText("\"Keep going!\"");
            binding.authorTextView.setText("- App");
        };
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
