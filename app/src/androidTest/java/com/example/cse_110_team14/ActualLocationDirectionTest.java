package com.example.cse_110_team14;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActualLocationDirectionTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    // Test directions from actual location. Plan contains crocodiles (and entrance and exit gate).
    // First mock location is at gorilla exhibit location. Second mock location is at capuchin
    // monkeys exhibit location. The test confirms whether the two directions displayed by the two
    // different locations differ.
    @Test
    public void actualLocationDirectionTest() {
        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.search_item_checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_items),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_button), withText("Plan(1)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.directions_button), withText("Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.mockButton), withText("Mock"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        editText.perform(replaceText("32.74812588554637"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        editText2.perform(replaceText("-117.17565073656901"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.nextButton), withText("Next Entrance and Exit Gate (12400 ft)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.direction), withText(" 1. Proceed on Monkey Trail 1200 ft towards Scripps Aviary."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView.check(matches(withText(" 1. Proceed on Monkey Trail 1200 ft towards Scripps Aviary.")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.direction), withText(" 2. Proceed on Hippo Trail 1500 ft towards Monkey Trail / Hippo Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView2.check(matches(withText(" 2. Proceed on Hippo Trail 1500 ft towards Monkey Trail / Hippo Trail.")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.direction), withText(" 3. Continue on Hippo Trail 1100 ft towards Crocodiles."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView3.check(matches(withText(" 3. Continue on Hippo Trail 1100 ft towards Crocodiles.")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.direction), withText(" 4. Continue on Hippo Trail 1900 ft towards Hippos."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView4.check(matches(withText(" 4. Continue on Hippo Trail 1900 ft towards Hippos.")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.direction), withText(" 5. Proceed on Treetops Way 1900 ft towards Treetops Way / Hippo Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView5.check(matches(withText(" 5. Proceed on Treetops Way 1900 ft towards Treetops Way / Hippo Trail.")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.direction), withText(" 6. Continue on Treetops Way 1400 ft towards Treetops Way / Orangutan Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView6.check(matches(withText(" 6. Continue on Treetops Way 1400 ft towards Treetops Way / Orangutan Trail.")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.direction), withText(" 7. Continue on Treetops Way 1100 ft towards Treetops Way / Fern Canyon Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView7.check(matches(withText(" 7. Continue on Treetops Way 1100 ft towards Treetops Way / Fern Canyon Trail.")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.direction), withText(" 8. Proceed on Gate Path 1100 ft towards Front Street / Treetops Way."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView8.check(matches(withText(" 8. Proceed on Gate Path 1100 ft towards Front Street / Treetops Way.")));

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.previousButton), withText("Previous Crocodiles (2700 ft)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.mockButton), withText("Mock"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction editText3 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        editText3.perform(replaceText("32.751128871469874"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        editText4.perform(replaceText("-117.16364410510093"), closeSoftKeyboard());

        ViewInteraction materialButton8 = onView(
                allOf(withId(android.R.id.button1), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.nextButton), withText("Next Entrance and Exit Gate (11200 ft)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.direction), withText(" 1. Proceed on Monkey Trail 3100 ft towards Flamingos."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView9.check(matches(withText(" 1. Proceed on Monkey Trail 3100 ft towards Flamingos.")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.direction), withText(" 2. Continue on Monkey Trail 1500 ft towards Front Street / Monkey Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView10.check(matches(withText(" 2. Continue on Monkey Trail 1500 ft towards Front Street / Monkey Trail.")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.direction), withText(" 3. Proceed on Front Street 2700 ft towards Front Street / Treetops Way."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView11.check(matches(withText(" 3. Proceed on Front Street 2700 ft towards Front Street / Treetops Way.")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.direction), withText(" 4. Proceed on Gate Path 1100 ft towards Entrance and Exit Gate."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView12.check(matches(withText(" 4. Proceed on Gate Path 1100 ft towards Entrance and Exit Gate.")));

        try {ViewInteraction materialButton10 = onView(
                    allOf(withId(R.id.nextButton), withText("Finish"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    1),
                            isDisplayed()));
            materialButton10.perform(click());
        }catch(Exception e) {
            System.out.print("");
        }

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
