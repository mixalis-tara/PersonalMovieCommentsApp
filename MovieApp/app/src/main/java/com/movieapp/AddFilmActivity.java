package com.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

public class AddFilmActivity extends AppCompatActivity {

    private EditText addFilmTitleEditText;
    private Spinner categorySpinner;
    private EditText addFilmCommentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_film);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addFilm), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addFilmTitleEditText = findViewById(R.id.addFilmTitle);
        categorySpinner = findViewById(R.id.categorySpinnerAdd);
        addFilmCommentEditText = findViewById(R.id.addFilmComments);
        loadCategories();


        DatePicker datePicker = findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();



        Button backBtn = findViewById(R.id.btnAddFilmBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button submitBtn = findViewById(R.id.btnSubmitNewFIlm);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = categorySpinner.getSelectedItem().toString();

                String title = addFilmTitleEditText.getText().toString();
                String comments = addFilmCommentEditText.getText().toString();

                // Get selected date from DatePicker
                DatePicker datePicker = findViewById(R.id.datePicker);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                // Format selected date as a string
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);


                DataSourses dataSourses = new DataSourses(AddFilmActivity.this);
                dataSourses.open();
                dataSourses.addFilm(title, category, comments, selectedDate);
                dataSourses.close();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("filmAdded", true);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });



    }
    private void loadCategories() {
        DataSourses dataSourses = new DataSourses(this);
        dataSourses.open();
        ArrayList<String> categories = dataSourses.getAllCategories();
        dataSourses.close();

        // Create an ArrayAdapter using the categories array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
    }
}