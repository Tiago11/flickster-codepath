package com.codepath.tiago.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.tiago.flickster.R;
import com.codepath.tiago.flickster.adapters.MovieArrayAdapter;
import com.codepath.tiago.flickster.helpers.YoutubeVideoHandlerHelper;
import com.codepath.tiago.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    YouTubePlayerFragment youtubeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Youtube fragment.
        youtubeFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);
        getFragmentManager().beginTransaction().hide(youtubeFragment).commit();

        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        // Initialize the youtube fragment where trailers will be played.
        setupYoutubeFragment();

        // Hit the API to get the movies and create our movie collection.
        getNowPlayingMoviesFromApi();

        // Attach event listeners to the ListView.
        setupListViewListener();

    }

    private void setupListViewListener() {
        // Event listener on click will launch detailActivity for that element.
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                View item, int pos, long id) {

                        Movie movie = movies.get(pos);

                        Intent i = new Intent(MovieActivity.this, DetailActivity.class);
                        i.putExtra("movie", Parcels.wrap(movie));

                        startActivity(i);

                    }
                }
        );
    }

    private void getNowPlayingMoviesFromApi() {

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();

                    getTrailerKeyForPopularMoviesFromApi();

                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    private void getTrailerKeyForPopularMoviesFromApi() {

        for (int i = 0; i < movies.size(); i++) {

            if (movies.get(i).isPopular()) {

                String url = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", String.valueOf(movies.get(i).getId()));
                AsyncHttpClient client = new AsyncHttpClient();

                final int k = i;

                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray trailerJsonResults = null;

                        try {
                            trailerJsonResults = response.getJSONArray("results");
                            if (trailerJsonResults.length() > 0) {
                                JSONObject trailerInfo = trailerJsonResults.getJSONObject(0);
                                if ("YouTube".equals(trailerInfo.getString("site"))) {
                                    String trailerKey = trailerInfo.getString("key");
                                    movies.get(k).setTrailerKey(trailerKey);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        }
    }

    private void setupYoutubeFragment() {
        // Receive key for the video and play it in the youtube fragment.
        movieAdapter.ytVideoHandler = new YoutubeVideoHandlerHelper() {
            @Override
            public void initYoutubePlayerFragment(String key) {
                getFragmentManager().beginTransaction().show(youtubeFragment).commit();

                final String k = key;
                youtubeFragment.initialize("AIzaSyDYDQOTuBA7dsv67K6qVlv6Gcb6uWuozW4",
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                                if (b) {
                                    youTubePlayer.play();
                                } else {
                                    youTubePlayer.loadVideo(k);
                                    lvItems.setVisibility(View.INVISIBLE);
                                }

                                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                    @Override
                                    public void onLoading() {
                                        // To implement.
                                    }

                                    @Override
                                    public void onLoaded(String s) {
                                        // To implement.
                                    }

                                    @Override
                                    public void onAdStarted() {
                                        // To implement.
                                    }

                                    @Override
                                    public void onVideoStarted() {
                                        // To implement.
                                    }

                                    @Override
                                    public void onVideoEnded() {
                                        lvItems.setVisibility(View.VISIBLE);
                                        getFragmentManager().beginTransaction().hide(youtubeFragment).commit();
                                    }

                                    @Override
                                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                                        // To implement.
                                    }
                                });

                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
            }
        };

    }
}
