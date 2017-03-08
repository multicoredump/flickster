package com.coremantra.tutorial.flickster.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.coremantra.tutorial.flickster.R;
import com.coremantra.tutorial.flickster.adapters.MovieAdapter;
import com.coremantra.tutorial.flickster.fragments.MovieDetailFragment;
import com.coremantra.tutorial.flickster.models.Movie;
import com.coremantra.tutorial.flickster.utils.ItemClickSupport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends Activity {

    private static final String TAG = MovieActivity.class.getName();

    final static String MOVIES_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    ItemClickSupport.OnItemClickListener itemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            Movie movie = movies.get(position);
            launchDetailsDialog(movie);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        readMoviesData();

        movieAdapter = new MovieAdapter(movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvMovies);

        ItemClickSupport.addTo(rvMovies).setOnItemClickListener(itemClickListener);
    }

    private void launchDetailsDialog(Movie movie) {
        android.app.FragmentManager fm = getFragmentManager();
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie);
        movieDetailFragment.show(fm, "show_movie_details");

    }

    private void readMoviesData() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(MOVIES_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                JSONArray movieJsonResults = null;
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    movieJsonResults = json.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (movieJsonResults != null) {
                    // Run view-related code back on the main thread
                    final JSONArray finalMovieJsonResults = movieJsonResults;

                    MovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movies.addAll(Movie.fromJSONArray(finalMovieJsonResults));
                            movieAdapter.update(movies);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
