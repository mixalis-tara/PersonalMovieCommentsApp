package com.movieapp;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Films> films;

    private Context context;

    private int requestCode;

    public FilmAdapter(Context context, ArrayList<Films> films, int requestCode){
        this.context = context;
        this.films = films;
        this.requestCode = requestCode;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.films, viewGroup, false );

        Item item = new Item(row);

        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        Films film = films.get(i);
        // Check if the film title is not empty or null
        if (film.getFilmtitle() != null && !film.getFilmtitle().isEmpty()) {
            ((Item)viewHolder).filmTitle.setText(film.getFilmtitle());
            ((Item)viewHolder).filmCategory.setText(film.getFilmcategory());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Films clickedFilm = films.get(viewHolder.getAdapterPosition());

                Intent intent = new Intent(context, FilmDetails.class);

                intent.putExtra("filmTitle", clickedFilm.getFilmtitle());
                intent.putExtra("filmCategory", clickedFilm.getFilmcategory());
                intent.putExtra("filmComments", clickedFilm.getFilmcomments());
                intent.putExtra("filmdate", clickedFilm.getFilmdate());

                ((Activity) context).startActivityForResult(intent, requestCode);

            }
        });
        } else {
            // If the film title is empty or null, hide the ViewHolder
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public void setFilms(ArrayList<Films> films) {
        this.films = films;
    }

    public ArrayList<Films> getFilms() {
        return films;
    }

    public class Item extends RecyclerView.ViewHolder{

        TextView filmTitle;

        TextView filmCategory;

        public Item(View itemView){
            super(itemView);

            filmTitle = itemView.findViewById(R.id.filmTitle);
            filmCategory = itemView.findViewById(R.id.filmCategory);
        }
    }
}
