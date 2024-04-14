package com.movieapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FilmDetails extends AppCompatActivity {

    TextView movieTitle;
    TextView movieCategory;

    TextView movieComments;

    TextView movieDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_film_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize TextViews
        movieTitle = findViewById(R.id.txtVwMovieTitle);
        movieCategory = findViewById(R.id.txtVwMovieCatergory);
        movieComments = findViewById(R.id.txtVwMovieComments);
        movieDate = findViewById(R.id.txtVwDateShowFilm);

        // Retrieve film details from intent extras
        String title = getIntent().getStringExtra("filmTitle");
        String category = getIntent().getStringExtra("filmCategory");
        String comments = getIntent().getStringExtra("filmComments");
        String date = getIntent().getStringExtra("filmdate");



        // Set film details to TextViews
        movieTitle.setText(title);
        movieCategory.setText(category);
        movieComments.setText(comments);
        movieDate.setText(date);

        Button deleteFilm = findViewById(R.id.btnDeleteFilm);
        deleteFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = getIntent().getStringExtra("filmTitle");

                DataSourses dataSourses = new DataSourses(FilmDetails.this);
                dataSourses.open();
                dataSourses.deleteFilmById(filmTitle);
                dataSourses.close();

                // Finish the activity and indicate success to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("filmDeleted", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        Button deleteComment = findViewById(R.id.btnDeleteComment);
        deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = getIntent().getStringExtra("filmTitle");

                DataSourses dataSourses = new DataSourses(FilmDetails.this);
                dataSourses.open();
                dataSourses.clearFilmComments(filmTitle);
                dataSourses.close();

                // Update the UI with the new comment
                movieComments.setText("");

            }
        });

        Button editComment = findViewById(R.id.btnEditComment);
        editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmTitle = getIntent().getStringExtra("filmTitle");
                // Retrieve current comment from the database
                DataSourses dataSourses = new DataSourses(FilmDetails.this);
                dataSourses.open();
                Films film = dataSourses.getFilmByTitle(filmTitle);
                dataSourses.close();

                // Initialize current comment
                String currentComment = (film != null) ? film.getFilmcomments() : "";

                // Create an EditText and set its input type
                final EditText input = new EditText(FilmDetails.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                // Set the current comment as the initial text
                input.setText(currentComment);

                // Build and show the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(FilmDetails.this);
                builder.setTitle("Edit Comment");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newComment = input.getText().toString().trim();
                        if (!newComment.isEmpty()) {
                            // Update the comment in the database
                            dataSourses.open();
                            dataSourses.updateFilmComments(filmTitle, newComment);
                            dataSourses.close();

                            // Refresh the UI to display the updated comment
                            movieComments.setText(newComment);
                        } else {
                            Toast.makeText(FilmDetails.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and indicate success to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("filmDeleted", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


    }
}