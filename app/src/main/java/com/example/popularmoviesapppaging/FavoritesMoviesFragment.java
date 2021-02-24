package com.example.popularmoviesapppaging;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmoviesapppaging.adapters.MovieAdapter;
import com.example.popularmoviesapppaging.database.MoviesViewModel;

import java.util.List;

import static com.example.popularmoviesapppaging.MainActivity.ID;
import static com.example.popularmoviesapppaging.MainActivity.IMAGE;
import static com.example.popularmoviesapppaging.MainActivity.MARKED_FAV;
import static com.example.popularmoviesapppaging.MainActivity.OVERVIEW;
import static com.example.popularmoviesapppaging.MainActivity.POPULARITY;
import static com.example.popularmoviesapppaging.MainActivity.RELEASE_DATE;
import static com.example.popularmoviesapppaging.MainActivity.TITLE;
import static com.example.popularmoviesapppaging.MainActivity.USER_RATING;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesMoviesFragment extends Fragment implements MovieAdapter.ItemClickListener {

    public static RecyclerView recyclerView_favorites;
    public static MovieAdapter adapter_favorites;
    GridLayoutManager layoutManager_favorites;
    public static TextView errorDisplay_favorites;
    ProgressBar loadingIndicator_favorites;

    MoviesViewModel mViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesMoviesFragment newInstance() {
        FavoritesMoviesFragment fragment = new FavoritesMoviesFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites_movies, container, false);

        mViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_favorites= view.findViewById(R.id.movie_favorites_recycler_view);
        errorDisplay_favorites = view.findViewById(R.id.tv_error_favorites);
        loadingIndicator_favorites = view.findViewById(R.id.progress_bar_favorites);

        int spanCount = getResources().getInteger(R.integer.columns);

        String title = getResources().getString(R.string.favorites);
        getActivity().setTitle(title);

        adapter_favorites = new MovieAdapter(this);
        layoutManager_favorites = new GridLayoutManager(getActivity(), spanCount);

        recyclerView_favorites.setLayoutManager(layoutManager_favorites);
        setUpViewModel();
        recyclerView_favorites.setAdapter(adapter_favorites);

    }

    private void setUpViewModel() {
        if (errorDisplay_favorites.getVisibility() == View.VISIBLE) {
             errorDisplay_favorites.setVisibility(View.INVISIBLE);
         }

        mViewModel.movies.observe(getActivity(), new Observer<PagedList<Movies>>() {
            @Override
            public void onChanged(PagedList<Movies> movies) {
                if (movies.size() > 0) {
                    errorDisplay_favorites.setVisibility(View.INVISIBLE);
                    adapter_favorites.submitList(movies);
                } else {
                    adapter_favorites.setMovies(null);
                    errorDisplay_favorites.setVisibility(View.VISIBLE);
                    errorDisplay_favorites.setText(getString(R.string.fav_list_error));
                }

            }
        });

    }

    @Override
    public void onItemClick(Movies mData) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(IMAGE, mData.getImage());
        intent.putExtra(TITLE, mData.getTitle());
        intent.putExtra(ID, mData.getId());
        intent.putExtra(OVERVIEW, mData.getOverview());
        intent.putExtra(USER_RATING, mData.getVote_average() + "/10");
        intent.putExtra(RELEASE_DATE, mData.getRelease_date());
        intent.putExtra(POPULARITY, mData.getPopularity());
        intent.putExtra(MARKED_FAV, mData.getMarkedAsFavorite());

        startActivity(intent);
    }

}