package com.example.popularmoviesapppaging.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.example.popularmoviesapppaging.Movies;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private LiveData<List<Movies>> mPopular;
    private LiveData<List<Movies>> mTopRated;
    private LiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private LiveData<Boolean> mIsMostPopularDataAvailable = new MutableLiveData<>();
    private LiveData<Boolean> mIsTopRatedDataAvailable = new MutableLiveData<>();

    public final LiveData<PagedList<Movies>> movies;
    MoviesRepository mRepository;


    public MoviesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MoviesRepository(application);
        movies = mRepository.allMovies();
    }

    public void init(){
        if (mPopular != null && mTopRated != null) {
            return;
        }
            mRepository = MoviesRepository.getInstance(getApplication());
            mPopular = mRepository.getMostPopularMovies();
            mTopRated = mRepository.getTopRatedMovies();
            mIsUpdating = mRepository.mIsFetching();
            mIsMostPopularDataAvailable = mRepository.mIsMostPopularDataAvailable();
            mIsTopRatedDataAvailable = mRepository.mIsTopRatedDataAvailable();

    }

    public LiveData<PagedList<Movies>> loadAllMovies() {
        return movies;
    }

    public void insert(Movies movies) {
        mRepository.insert(movies);
    }

    public void delete(Movies movies) {
        mRepository.delete(movies);
    }

    public int getAnyWord(){
       return mRepository.getAnyWord();
    }

    public LiveData<List<Movies>> getPopularMoviesList() {
        return mPopular;
    }

    public LiveData<List<Movies>> getTopRatedMoviesList() {
        return mTopRated;
    }

    public LiveData<Boolean> fetchingData() {
        return mIsUpdating;
    }

    public LiveData<Boolean> isMostPopularMovieDataAvailable() {
        return mIsMostPopularDataAvailable;
    }
    public LiveData<Boolean> isTopRatedMovieDataAvailable() {
        return mIsTopRatedDataAvailable;
    }

}
