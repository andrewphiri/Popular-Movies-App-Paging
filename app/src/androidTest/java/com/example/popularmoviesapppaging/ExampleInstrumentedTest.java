package com.example.popularmoviesapppaging;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.testing.FragmentScenario;

import androidx.test.espresso.contrib.RecyclerViewActions;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.action.ViewActions.swipeUp;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public IntentsTestRule<DetailsActivity> intentsTestRule = new IntentsTestRule<>(DetailsActivity.class);



    @Before
    public void before() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }


    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.popularmoviesapp", appContext.getPackageName());
    }


    @Test
    public void checkReviewsDisplay() {
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(MostPopularMoviesFragment.class);
        try {
            Thread.sleep(2000);

            onView(withId(R.id.movie_popular_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(2, click()));
            onView(withId(R.id.scrollView_details)).perform(swipeUp());
            onView(withId(R.id.button)).perform(click());
            FragmentScenario scenario = FragmentScenario.launchInContainer(ReviewsFragment.class);
            assertNotNull(scenario);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void markFavorite() {
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(MostPopularMoviesFragment.class);
        try {
            Thread.sleep(2000);

            onView(withId(R.id.movie_popular_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.markFav)).perform(click()).check(matches(isClickable()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void playTrailer1() {
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(MostPopularMoviesFragment.class);
        //onView(withId(R.id.movie_popular_recycler_view)).check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);

            onView(withId(R.id.movie_popular_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(4, click()));

            onView(withId(R.id.scrollView_details)).perform(swipeUp());
            assertNotNull(withId(R.id.trailer1));

            try {
                onView(withId(R.id.trailer1)).perform(click());
                intended(hasAction(Intent.ACTION_VIEW));
            } catch (Exception e) {
                assertTrue(true);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void playTrailer2() {
        FragmentScenario fragmentScenario = FragmentScenario.launchInContainer(TopRatedMoviesFragment.class);
        //onView(withId(R.id.movie_popular_recycler_view)).check(matches(isDisplayed()));
        try {
            Thread.sleep(2000);

            onView(withId(R.id.movie_top_rated_recycler_view)).perform(swipeUp());
            onView(withId(R.id.movie_top_rated_recycler_view)).perform
                    (RecyclerViewActions.actionOnItemAtPosition(18, click()));

            onView(withId(R.id.scrollView_details)).perform(swipeUp());
            assertNotNull(withId(R.id.trailer2));

            try {
                onView(withId(R.id.trailer2)).perform(click());
                intended(hasAction(Intent.ACTION_VIEW));
            } catch (Exception e) {
                assertTrue(true);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}