package com.coremantra.tutorial.flickster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private static List<Movie> movies;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvOverview)
        TextView tvOverview;

        @BindView(R.id.ivPoster)
        ImageView ivPoster;

        public ViewHolder(View itemView) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View todoView = inflater.inflate(R.layout.item_movie, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(todoView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // If a context is needed, it can be retrieved
        // from the ViewHolder's root view.
        final Context context = holder.itemView.getContext();

        // Get the data model based on position - from in-memory data
        Movie movie = movies.get(position);

        // Set item views based on your views and data model
        holder.tvOverview.setText(movie.getOverview());
        holder.tvTitle.setText(movie.getTitle());

        // use placeholder & rounded corners transformation

        Picasso.with(context).load(movie.getPosterPath())
                .placeholder(R.drawable.movie_placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(holder.ivPoster);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
