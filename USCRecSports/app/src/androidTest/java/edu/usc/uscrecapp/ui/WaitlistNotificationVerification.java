package edu.usc.uscrecapp.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import edu.usc.uscrecapp.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WaitlistNotificationVerification {
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static final String USER = "root";
    private static final String PASSWORD = "Matthewwilson033!";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void waitlistNotificationVerification() {
        System.out.println("Starting test execution now");
        new NotificationAsync().execute();
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("mwilson"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("mwilson"), closeSoftKeyboard());

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

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_notifications), withContentDescription("Summary"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction button = onView(
                allOf(withText("Cancel"),
                        childAtPosition(
                                allOf(withId(R.id.upcoming_layout),
                                        childAtPosition(
                                                withId(R.id.vertical_layout),
                                                0)),
                                2),
                        isDisplayed()));
        button.perform(click());
        new DeleteAsync().execute();
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

    public class NotificationAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void...voids){
            try{
                System.out.println("inside of notification async method");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = connection.createStatement();
                String sql = "DELETE FROM reservations WHERE user_id=3";
                stmt.executeUpdate(sql);

                String sql2 = "INSERT INTO availability(availability_id,location_id,timeslot_id,slots_available,date)" +
                        "VALUES(1000,1,1,0,'MON');";
                stmt.executeUpdate(sql2);

                String sql3 = "INSERT INTO reservations(user_id,timeslot_id,location_id,date,availability_id) VALUES(3,1,1,'2024-04-12',1000);";
                stmt.executeUpdate(sql3);

                String sql4 = "INSERT INTO waiting_lists(user_id, timeslot_id,location_id,date,availability_id)\n" +
                        "VALUES(3,1,1,'2022-04-12',1000);";
                stmt.executeUpdate(sql4);

            }
            catch(Exception e){
                Log.e("USC Rec Sports", "Error during MySQL communication", e);

            }
            return null;
        }
    }

    public class DeleteAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void...voids){
            try{
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = connection.createStatement();
                String sql = "DELETE FROM reservations WHERE user_id=3";
                stmt.executeUpdate(sql);

                String wait = "DELETE FROM waiting_lists WHERE availability_id=1000";
                stmt.executeUpdate(wait);

                String sql2= "DELETE FROM availability WHERE availability_id=1000";
                stmt.executeUpdate(sql2);

                String sql3 = "DELETE FROM waiting_lists WHERE user_id=3";
                stmt.executeUpdate(sql3);

            }
            catch(Exception e){
                Log.e("USC Rec Sports", "Error during MySQL communication", e);

            }
            return null;
        }
    }

}
