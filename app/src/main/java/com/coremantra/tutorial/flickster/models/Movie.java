package com.coremantra.tutorial.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by radhikak on 3/4/17.
 */

public class Movie {

    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private int voteAverage;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public Movie (JSONObject jsonObject) throws JSONException
    {
        title = jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getInt("vote_average");
    }
    // Factory Method
    public static ArrayList<Movie> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Movie> movies = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
               movies.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return movies;
    }

}
