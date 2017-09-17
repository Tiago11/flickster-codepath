package com.codepath.tiago.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tiago.flickster.R;
import com.codepath.tiago.flickster.helpers.DeviceDimensionsHelper;
import com.codepath.tiago.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tiago on 9/15/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    private static class ViewHolderRegularMovie {
        TextView title;
        TextView overview;
        ImageView image;
    }

    private static class ViewHolderPopularMovie {
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        return Movie.DisplayValues.values().length;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup) for the
    // specified item.
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDisplayValue().ordinal();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position.
        Movie movie = getItem(position);

        // Get the item type for this position
        int type = getItemViewType(position);

        if (type == Movie.DisplayValues.POPULAR.ordinal()) {

            ViewHolderPopularMovie holderPopularMovie;
            if (convertView == null) {

                holderPopularMovie = new ViewHolderPopularMovie();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);

                holderPopularMovie.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                holderPopularMovie.image.setImageResource(0);

                convertView.setTag(holderPopularMovie);

            } else {
                holderPopularMovie = (ViewHolderPopularMovie) convertView.getTag();
            }

            // Populate
            populateHolderPopularMovie(holderPopularMovie, movie);

        } else if (type == Movie.DisplayValues.REGULAR.ordinal()) {

            ViewHolderRegularMovie holderRegularMovie;
            if (convertView == null) {

                holderRegularMovie = new ViewHolderRegularMovie();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);

                holderRegularMovie.title = (TextView) convertView.findViewById(R.id.tvTitle);
                holderRegularMovie.overview = (TextView) convertView.findViewById(R.id.tvOverview);
                holderRegularMovie.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);

                convertView.setTag(holderRegularMovie);

            } else {
                holderRegularMovie = (ViewHolderRegularMovie) convertView.getTag();
            }

            // Populate.
            populateHolderRegularMovie(holderRegularMovie, movie);

        } else {
            // Log!
        }

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file.
    private View getInflatedLayoutForType(int type) {
        if (type == Movie.DisplayValues.POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);
        } else if (type == Movie.DisplayValues.REGULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

    private void populateHolderPopularMovie(ViewHolderPopularMovie holderPopularMovie, Movie movie) {
        int imageWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());
        Picasso.with(getContext()).load(movie.getBackdropPath()).resize(imageWidth, 0).placeholder(R.drawable.movie_placeholder_landscape).into(holderPopularMovie.image);

    }

    private void populateHolderRegularMovie(ViewHolderRegularMovie holderRegularMovie, Movie movie) {
        // Check the orientation of the device and choose the correct image path for it.
        int orientation = getContext().getResources().getConfiguration().orientation;
        String imagePath = "";
        int imageWidth = 0;
        int imagePlaceholder = 0;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imagePath = movie.getPosterPath();
            imageWidth = (int) (0.4*DeviceDimensionsHelper.getDisplayWidth(getContext()));
            imagePlaceholder = R.drawable.movie_placeholder_portrait;

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imagePath = movie.getBackdropPath();
            imageWidth = (int) (0.6*DeviceDimensionsHelper.getDisplayWidth(getContext()));
            imagePlaceholder = R.drawable.movie_placeholder_landscape;
        }

        // Load the image.
        holderRegularMovie.image.setImageResource(0);
        Picasso.with(getContext()).load(imagePath).resize(imageWidth, 0).placeholder(imagePlaceholder).into(holderRegularMovie.image);

        holderRegularMovie.title.setText(movie.getOriginalTitle());
        holderRegularMovie.overview.setText(movie.getOverview());
    }
}
