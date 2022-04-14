package edu.usc.uscrecapp.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.uscrecapp.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WaitlistModeTimeslotTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void pressTimeslotTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("jjso"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("jjso"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login), withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.navigation_reservation)));
        materialButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Reservation - Lyon Center"),
                        withParent(allOf(withId(androidx.appcompat.R.id.action_bar),
                                withParent(withId(androidx.appcompat.R.id.action_bar_container)))),
                        isDisplayed()));
        textView.check(matches(withText("Reservation - Lyon Center")));

        ViewInteraction button = onView(withId(R.id.button));
        button.perform(click());
        ViewInteraction textConfirm = onView(withId(R.id.confirmation));
        textConfirm.check(matches(withText(containsString("Select time to be wait-listed."))));

        String timeslot1 = getText(withId(R.id.button4));
        String timeslot2 = getText(withId(R.id.button5));
        String timeslot3 = getText(withId(R.id.button6));
        String timeslot4 = getText(withId(R.id.button7));
        Log.i(">>>> t1: ",timeslot1);
        Log.i(">>>> t2: ", timeslot2);
        Log.i(">>>> t3: ",timeslot3);
        Log.i(">>>> t4: ", timeslot4);
        if(timeslot1.contains(" 0 slots available")){
            button = onView(withId(R.id.button4));
            button.perform(click());
        }
        else if(timeslot2.contains(" 0 slots available")){
            button = onView(withId(R.id.button5));
            button.perform(click());
        }
        else if(timeslot3.contains(" 0 slots available")){
            button = onView(withId(R.id.button6));
            button.perform(click());
        }
        else if(timeslot4.contains(" 0 slots available")){
            button = onView(withId(R.id.button7));
            button.perform(click());
        }

        textConfirm = onView(withId(R.id.confirmation));
        textConfirm.check(matches(withText(containsString("Wait-listed"))));
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
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
