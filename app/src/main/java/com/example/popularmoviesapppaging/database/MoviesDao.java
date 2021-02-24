package com.example.popularmoviesapppaging.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmoviesapppaging.Movies;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM popular_movies")
    LiveData<List<Movies>> loadAllMovies();

    @Query("SELECT * FROM popular_movies")
    DataSource.Factory<Integer, Movies> getAllPaged();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movies movies);

    @Delete
    void deleteMovie(Movies movies);

    @Query("SELECT * FROM popular_movies LIMIT 1")
    Movies [] getAnyWord();
}
