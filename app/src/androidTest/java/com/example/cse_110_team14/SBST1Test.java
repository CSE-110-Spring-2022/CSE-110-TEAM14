package com.example.cse_110_team14;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SBST1Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sBST1Test() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Gorillas"), closeSoftKeyboard());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.search_item_checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_items),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.search_item_text), withText("Gorillas"),
                        withParent(withParent(withId(R.id.search_items))),
                        isDisplayed()));
        textView.check(matches(withText("Gorillas")));

        ViewInteraction button = onView(
                allOf(withId(R.id.plan_button), withText("PLAN(1)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.delete_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.search_text), withText(""),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(withText("")));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Java"), closeSoftKeyboard());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.no_search_results), withText("No Search Results :("),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("No Search Results :(")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.delete_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.search_item_checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_items),
                                        2),
                                1),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.plan_button), withText("PLAN(2)"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
        button2.check(matches(withText("Plan(2)")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
