package com.coremantra.tutorial.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremantra.tutorial.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by radhikak on 3/4/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = MovieAdapter.class.getName();

    private static int POPULAR_VOTE_AVERAGE = 6;

    private static List<Movie> movies;

    // View types
    private final int BACKDROP_IMAGE = 0;
    private final int POSTER_IMAGE_WITH_OVERVIEW = 1;

    public static class PosterImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvOverview)
        TextView tvOverview;

        @BindView(R.id.ivPoster)
        ImageView ivPoster;

        public PosterImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class BackdropImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivBackdrop)
        ImageView ivBackdrop;

        public BackdropImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MovieAdapter(List<Movie> movieList) {
        movies = movieList;
    }

    public void update(List<Movie> movieList) {
        movies = movieList;
        notifyDataSetChanged();
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View movieView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case BACKDROP_IMAGE:
                movieView = inflater.inflate(R.layout.item_movie_backdrop, parent, false);
                viewHolder = new BackdropImageViewHolder(movieView);
                break;

            case POSTER_IMAGE_WITH_OVERVIEW:
                movieView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new PosterImageViewHolder(movieView);
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // If a context is needed, it can be retrieved
        // from the ViewHolder's root view.
        final Context context = viewHolder.itemView.getContext();

        switch (viewHolder.getItemViewType()) {
            case BACKDROP_IMAGE:
                configureBackdropImageView(context, (BackdropImageViewHolder)viewHolder, position);
                break;

            case POSTER_IMAGE_WITH_OVERVIEW:
                configurePosterImageView(context, (PosterImageViewHolder) viewHolder, position);
                break;

            default:
                break;
        }

    }

    private void configureBackdropImageView(Context context, BackdropImageViewHolder viewHolder, int position) {
        Movie movie = movies.get(position);

        // Show backdrop image for landscape orientation.
        String imageURL;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
        } else {
            imageURL = movie.getPosterPath();
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Picasso.with(context).load(imageURL)
                .placeholder(R.drawable.movie_placeholder)
                .resize(displayMetrics.widthPixels, 0)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivBackdrop);

    }


    private void configurePosterImageView(Context context, PosterImageViewHolder viewHolder, int position) {
        Movie movie = movies.get(position);

        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.tvTitle.setText(movie.getTitle());

        // Show backdrop image for landscape orientation.
        String imageURL;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
        } else {
            imageURL = movie.getPosterPath();
        }

        // use placeholder & rounded corners transformation
        Picasso.with(context).load(imageURL)
                .placeholder(R.drawable.movie_placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivPoster);
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = movies.get(position);
        Log.d(TAG, "getItemViewType: Movie: "+ movie.getTitle() + " vote: "+ movie.getVoteAverage());

        if (movie.getVoteAverage() > POPULAR_VOTE_AVERAGE) {
            Log.d(TAG, "Movie: "+ movie.getTitle() + " vote: "+ movie.getVoteAverage());
            return BACKDROP_IMAGE;
        }

        return POSTER_IMAGE_WITH_OVERVIEW;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
