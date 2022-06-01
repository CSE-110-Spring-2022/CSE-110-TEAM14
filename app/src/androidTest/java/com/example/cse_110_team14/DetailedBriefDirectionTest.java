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
public class DetailedBriefDirectionTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void detailedBriefDirectionTest() {
        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.search_item_checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_items),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.search_item_checkbox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_items),
                                        1),
                                1),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.plan_button), withText("Plan(2)"),
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
                allOf(withId(R.id.nextButton), withText("Next Koi Fish (16700 ft)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button2), withText("Detailed directions"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        materialButton6.perform(scrollTo(), click());

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
                allOf(withId(R.id.direction), withText(" 8. Proceed on Front Street 3200 ft towards Front Street / Terrace Lagoon Loop (South)."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView8.check(matches(withText(" 8. Proceed on Front Street 3200 ft towards Front Street / Terrace Lagoon Loop (South).")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.imageButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Brief directions"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.direction), withText(" 1. Proceed on Monkey Trail 1200 ft towards Scripps Aviary."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView9.check(matches(withText(" 1. Proceed on Monkey Trail 1200 ft towards Scripps Aviary.")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.direction), withText(" 2. Proceed on Hippo Trail 4500 ft towards Monkey Trail / Hippo Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView10.check(matches(withText(" 2. Proceed on Hippo Trail 4500 ft towards Monkey Trail / Hippo Trail.")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.direction), withText(" 3. Proceed on Treetops Way 4400 ft towards Treetops Way / Hippo Trail."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView11.check(matches(withText(" 3. Proceed on Treetops Way 4400 ft towards Treetops Way / Hippo Trail.")));

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.direction), withText(" 4. Proceed on Front Street 3200 ft towards Front Street / Terrace Lagoon Loop (South)."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView12.check(matches(withText(" 4. Proceed on Front Street 3200 ft towards Front Street / Terrace Lagoon Loop (South).")));

        ViewInteraction textView13 = onView(
                allOf(withId(R.id.direction), withText(" 5. Proceed on Terrace Lagoon Loop 2200 ft towards Koi Fish."),
                        withParent(withParent(withId(R.id.directionsRecyclerView))),
                        isDisplayed()));
        textView13.check(matches(withText(" 5. Proceed on Terrace Lagoon Loop 2200 ft towards Koi Fish.")));

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.nextButton), withText("Next Entrance and Exit Gate (11200 ft)"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        try{ViewInteraction materialButton9 = onView(
                    allOf(withId(R.id.nextButton), withText("Finish"),
                            childAtPosition(
                                    childAtPosition(
                                            withId(android.R.id.content),
                                            0),
                                    1),
                            isDisplayed()));
            materialButton9.perform(click());
        }catch (Exception e) {
            System.out.println(" ");
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
