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
import com.codepath.tiago.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tiago on 9/15/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the movie for this position.
        Movie movie = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        ViewHolder viewHolder;
        if (convertView == null) {

            // If there's no view to re-use, inflate a brand new view for row.
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);

            // Cache the viewHolder object inside the fresh view.
            convertView.setTag(viewHolder);
        } else {

            // View is being recycled, retrieve the viewHolder object from tag.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object into the template view.
        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());

        // Check the orientation of the device and choose the correct image path for it.
        int orientation = getContext().getResources().getConfiguration().orientation;
        String image_path = "";
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            image_path = movie.getPosterPath();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image_path = movie.getBackdropPath();
        }

        // Load the image.
        viewHolder.image.setImageResource(0);
        Picasso.with(getContext()).load(image_path).into(viewHolder.image);

        return convertView;
    }
}
