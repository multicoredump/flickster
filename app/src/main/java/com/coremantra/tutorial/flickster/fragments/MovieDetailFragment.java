package com.coremantra.tutorial.flickster.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coremantra.tutorial.flickster.R;
import com.coremantra.tutorial.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by radhikak on 3/10/17.
 */

public class MovieDetailFragment extends DialogFragment {

    private static final String SHOW_MOVIE = "show_movie";

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;

    @BindView(R.id.rbRating)
    RatingBar rbRating;

    @BindView(R.id.tvOverview)
    TextView tvOverview;

    Movie movie;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            movie = (Movie) args.getSerializable("movie");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use placeholder & rounded corners transformation
        Picasso.with(getActivity().getApplicationContext()).load(movie.getBackdropPath())
                .placeholder(R.drawable.movie_placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(ivImage);

        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(tvReleaseDate.getText() + " " + movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());

        rbRating.setRating(movie.getVoteAverage() / 2);
        rbRating.setNumStars(5);
        LayerDrawable stars = (LayerDrawable) rbRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
