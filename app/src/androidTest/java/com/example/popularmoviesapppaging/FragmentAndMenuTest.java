package com.example.popularmoviesapppaging;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class FragmentAndMenuTest {
    @Rule
    public ActivityScenarioRule activityScenarioRule = new ActivityScenarioRule(MainActivity.class);

    private Context appContext;

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void fragmentMostPopular(){
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(MostPopularMoviesFragment.class);

        try {
            Thread.sleep(2000);
            onView(withId(R.id.movie_popular_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(3, click()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fragmentTopRated(){
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(TopRatedMoviesFragment.class);
        try {
            Thread.sleep(2000);
            onView(withId(R.id.movie_top_rated_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(0, click()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fragmentFavorites(){
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(FavoritesMoviesFragment.class);

        try {
            Thread.sleep(2000);
            onView(withId(R.id.movie_favorites_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(0, click()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
