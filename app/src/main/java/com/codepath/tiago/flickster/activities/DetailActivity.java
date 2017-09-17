package com.codepath.tiago.flickster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.tiago.flickster.R;
import com.codepath.tiago.flickster.helpers.DeviceDimensionsHelper;
import com.codepath.tiago.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    ImageView ivMovieImage;
    TextView tvTitle;
    TextView tvReleaseDate;
    RatingBar rbVoteAverage;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the movie from the intent.
        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        // Get references to the views in the layout.
        ivMovieImage = (ImageView) findViewById(R.id.ivMovieImage);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvOverview = (TextView) findViewById(R.id.tvOverview);

        // Set up the views information.
        int imageWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        Picasso.with(this).load(movie.getBackdropPath()).resize(imageWidth, 0).placeholder(R.drawable.movie_placeholder_landscape).into(ivMovieImage);

        tvTitle.setText(movie.getOriginalTitle());
        tvReleaseDate.setText(movie.getReleaseDate());

        rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2.0));
        tvOverview.setText(movie.getOverview());
    }
}
