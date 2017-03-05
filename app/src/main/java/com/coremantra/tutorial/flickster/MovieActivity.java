package com.coremantra.tutorial.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.coremantra.tutorial.flickster.models.Movie;

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

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getName();

    final static String MOVIES_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;

    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

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
                            movieAdapter.update(Movie.fromJSONArray(finalMovieJsonResults));
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
