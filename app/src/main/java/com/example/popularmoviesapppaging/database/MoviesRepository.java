package com.example.popularmoviesapppaging.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.popularmoviesapppaging.MainActivity;
import com.example.popularmoviesapppaging.Movies;
import com.example.popularmoviesapppaging.utils.AppExecutors;
import com.example.popularmoviesapppaging.utils.JsonMovieUtils;
import com.example.popularmoviesapppaging.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MoviesRepository {
    private static MoviesRepository instance;

    private MoviesDao moviesDao;

    public final LiveData<PagedList<Movies>> mAllMovies;
    PagedList.Config pagedList;
    private int databaseSize;

    public List<Movies> mostPopular;
    public List<Movies> topRated;
    private MutableLiveData<List<Movies>> dataPopular;
    private MutableLiveData<List<Movies>> dataTopRated;
    private MutableLiveData<Boolean> mIsFetchingData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsPopularMoviesDataAvailable = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsTopRatedMoviesDataAvailable = new MutableLiveData<>();


    public static MoviesRepository getInstance(Application application) {
        if (instance == null){
            instance = new MoviesRepository(application);
        }
        return instance;
    }

    MoviesRepository(Application application) {
        MoviesDatabase db = MoviesDatabase.getInstance(application);
        moviesDao = db.moviesDao();

        pagedList = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .setPageSize(20).build();

        mAllMovies = new LivePagedListBuilder<>(
                moviesDao.getAllPaged(), pagedList
        ).build();
    }

    public void insert(Movies movies) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                moviesDao.insertMovie(movies);
            }
        });
    }

    public void delete(Movies movies) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                moviesDao.deleteMovie(movies);
            }
        });
    }

    public LiveData<PagedList<Movies>> allMovies() {
        return mAllMovies;
    }

    public int getAnyWord() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            private int size;

            @Override
           public void run() {
                size = moviesDao.getAnyWord().length;
                databaseSize = size;
           }
       });

        return databaseSize;
    }

    public LiveData<List<Movies>> getMostPopularMovies() {
        setPopularMovies(MainActivity.MOST_POPULAR);
        return dataPopular;
    }

    public LiveData<List<Movies>> getTopRatedMovies() {
        setTopRatedMovies(MainActivity.TOP_RATED);
        return dataTopRated;
    }

    public void setPopularMovies(String sortOrder){
        dataPopular = new MutableLiveData<>();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mIsFetchingData.postValue(true);
                URL movieUrl = NetworkUtils.buildUrl(sortOrder);
                List<Movies> jsonResponse = null;
                try {
                    String movieJsonResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    jsonResponse = JsonMovieUtils.parsedMovieJson(movieJsonResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                mostPopular = jsonResponse;
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mIsFetchingData.setValue(false);

                        if (mostPopular != null) {
                            mIsPopularMoviesDataAvailable.setValue(true);

                            dataPopular.setValue(mostPopular);
                        } else {
                            mIsPopularMoviesDataAvailable.setValue(false);
                        }
                    }
                });

            }
        });
    }

    public void setTopRatedMovies(String sortOrder){
        dataTopRated = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mIsFetchingData.postValue(true);
                URL movieUrl = NetworkUtils.buildUrl(sortOrder);
                List<Movies> jsonResponse = null;
                try {
                    String movieJsonResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    jsonResponse = JsonMovieUtils.parsedMovieJson(movieJsonResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                topRated = jsonResponse;
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mIsFetchingData.setValue(false);

                        if (topRated != null){
                            mIsTopRatedMoviesDataAvailable.setValue(true);

                            dataTopRated.setValue(topRated);
                        } else {
                            mIsTopRatedMoviesDataAvailable.setValue(false);
                        }
                    }
                });

            }
        });
    }

    public LiveData<Boolean> mIsFetching() {
        return mIsFetchingData;
    }
    public LiveData<Boolean> mIsMostPopularDataAvailable() {
        return mIsPopularMoviesDataAvailable;
    }
    public LiveData<Boolean> mIsTopRatedDataAvailable() {
        return mIsTopRatedMoviesDataAvailable;
    }
}
