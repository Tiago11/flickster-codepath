package com.codepath.tiago.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tiago on 9/15/17.
 */

@Parcel
public class Movie {

    public enum DisplayValues implements Serializable {
        POPULAR,
        REGULAR;
    }

    public String posterPath;
    public String backdropPath;
    public String originalTitle;
    public String overview;
    public double voteAverage;
    public String releaseDate;

    public DisplayValues displayValue;

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", this.posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", this.backdropPath);
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public String getOverview() {
        return this.overview;
    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public String getReleaseDate() {
        return String.format("Release date: %s", this.releaseDate);
    }

    public DisplayValues getDisplayValue() {
        return this.displayValue;
    }

    public Movie() {

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.releaseDate = jsonObject.getString("release_date");
        this.voteAverage = jsonObject.getDouble("vote_average");

        if (this.voteAverage >= 7.0) {
            this.displayValue = DisplayValues.POPULAR;
        } else {
            this.displayValue = DisplayValues.REGULAR;
        }
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                results.add(new Movie(array.getJSONObject(i)));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}
