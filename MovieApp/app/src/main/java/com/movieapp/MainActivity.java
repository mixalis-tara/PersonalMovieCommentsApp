package com.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_FILM_REQUEST_CODE = 1;

    private static final int DELETE_FILM_REQUEST_CODE = 2;
    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private ArrayList<Films> films;

    private Spinner categorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rclVwMovies);
        categorySpinner = findViewById(R.id.spinner);

        loadCategories();
        loadFilms();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected category
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                // Load films by selected category
                loadFilmsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });


        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFilms();
            }
        });

        Button addFilmBtn = findViewById(R.id.btnAddNewFilm);
        addFilmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFilmActivity.class);
                startActivityForResult(intent, ADD_FILM_REQUEST_CODE);
            }
        });
        Button clearFilter = findViewById(R.id.btnClearFilt);
        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySpinner.setSelection(0);
            }
        });
    }

    private void loadCategories(){
        DataSourses dataSourses = new DataSourses(MainActivity.this);
        dataSourses.open();
        ArrayList<String> categories = dataSourses.getAllCategories();
        dataSourses.close();


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }
    private void loadFilms() {
        DataSourses dataSourses = new DataSourses(MainActivity.this);
        dataSourses.open();
        films = dataSourses.getAllFilms();
        dataSourses.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new FilmAdapter(MainActivity.this, films, DELETE_FILM_REQUEST_CODE);
        recyclerView.setAdapter(adapter);
    }

    private void loadFilmsByCategory(String category){
        DataSourses dataSourses = new DataSourses(MainActivity.this);
        dataSourses.open();
        if ("Choose a category".equals(category)) {
            // If the default option is selected, load all films
            films = dataSourses.getAllFilms();
        } else {
            // Load films by the selected category
            ArrayList<Films> filmsByCategory = dataSourses.getFilmsByCategory(category);
            films = filmsByCategory;
        }
        dataSourses.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new FilmAdapter(MainActivity.this, films, DELETE_FILM_REQUEST_CODE);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FILM_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("filmAdded", false)) {
                loadFilms(); // Refresh the film list
            }
        } else if (requestCode == DELETE_FILM_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("filmDeleted", false)) {
                loadFilms(); // refresh film list
            }
        }
    }


}