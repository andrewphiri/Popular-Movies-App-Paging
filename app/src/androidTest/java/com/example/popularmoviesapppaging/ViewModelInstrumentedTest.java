package com.example.popularmoviesapppaging;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.example.popularmoviesapppaging.database.MoviesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ViewModelInstrumentedTest {
    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    MoviesViewModel moviesViewModel;


    @Mock
    LiveData<List<Movies>> loadingLiveData;

    @Mock
    LiveData<Boolean> isLoadingData;

    @Mock
    Observer<Boolean> observer;

    @Before
    public void init() throws Exception {

        MockitoAnnotations.initMocks(this);
        Application context = ApplicationProvider.getApplicationContext();
        moviesViewModel = spy(new MoviesViewModel(context));
        loadingLiveData = moviesViewModel.getPopularMoviesList();
        isLoadingData = moviesViewModel.fetchingData();
    }
    @Test
    public void verifyLiveDataChanges() {
        assertNotNull(moviesViewModel.getPopularMoviesList());
        moviesViewModel.fetchingData().observeForever(observer);
        verify(observer).onChanged(false);
        moviesViewModel.getPopularMoviesList();
        verify(observer).onChanged(true);
    }

}
