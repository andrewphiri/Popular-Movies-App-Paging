package com.example.popularmoviesapppaging;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmoviesapppaging.adapters.ReviewsAdapter;
import com.example.popularmoviesapppaging.utils.AppExecutors;
import com.example.popularmoviesapppaging.utils.JsonMovieUtils;
import com.example.popularmoviesapppaging.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MOVIE_ID = "param1";
    //private static final String MOVIE_ID = "com.example.popularmoviesapp.MOVIE_ID";

    ReviewsAdapter adapter;
    RecyclerView reviewsRecyclerView;
    LinearLayoutManager linearLayout;

    // TODO: Rename and change types of parameters
    private String ID;
    private String reviews = "reviews";
    List<Reviews> movieReviews = new ArrayList<>();
    TextView textView;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ID = getArguments().getString(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        int configuration = getActivity().getResources().getConfiguration().orientation;

        if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        textView = view.findViewById(R.id.textViewRevs);

        linearLayout = new LinearLayoutManager(getActivity());
        adapter = new ReviewsAdapter(getActivity());

        reviewsRecyclerView.setLayoutManager(linearLayout);
        reviewsRecyclerView.setAdapter(adapter);

        loadReviews();
    }

    public void loadReviews() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                URL url = NetworkUtils.trailerReviewsUrl(ID, reviews);
                Log.i("URLSSSSS", url.toString());

                List<Reviews> jsonResponse = null;

                try {
                    String responseFromNet = NetworkUtils.getResponseFromHttpUrl(url);
                    jsonResponse = JsonMovieUtils.reviewJson(responseFromNet);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                movieReviews = jsonResponse;

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movieReviews != null ) {
                            adapter.setReviews(movieReviews);

                        } else {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(getString(R.string.no_reviews_message));
                            reviewsRecyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}