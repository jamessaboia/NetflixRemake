package com.jamessaboia.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamessaboia.netflixremake.model.Movie;
import com.jamessaboia.netflixremake.model.MovieDetail;
import com.jamessaboia.netflixremake.util.ImageDownloaderTask;
import com.jamessaboia.netflixremake.util.MovieDatailTask;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieDatailTask.MovieDetailLoader {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.text_view_title);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        recyclerView = findViewById(R.id.recycler_view_similar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.cover_drawable, movieCover);
            ((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(drawable);
        }

        List<Movie> movies = new ArrayList<>();
        movieAdpter = new MovieAdapter(movies);
        recyclerView.setAdapter(movieAdpter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            MovieDatailTask movieDatailTask = new MovieDatailTask(this);
            movieDatailTask.setMovieDetailLoader(this);
            movieDatailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }

    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        txtTitle.setText(movieDetail.getMovie().getTitle());
        txtDesc.setText(movieDetail.getMovie().getDesc());
        txtCast.setText(movieDetail.getMovie().getCast());

        movieAdpter.setMovies(movieDetail.getMovieSimilar());
        movieAdpter.notifyDataSetChanged();
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {

        final ImageView imageViewCover;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);

        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        void setMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageDownloaderTask(holder.imageViewCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

}