package edu.usc.uscrecapp.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.ui.reservation.ReservationFragment;

@RunWith(AndroidJUnit4.class)
public class ReservationInstrumentationTest {

    private ReservationFragment fragment = null;

    /**
     * Create a new ReservationFragment instance and check for null.
     */
    @Before
    public void setUp() {
        fragment = new ReservationFragment();
        assertNotNull(fragment);
    }

    /**
     * Test if the active date is null when the fragment is created.
     * Also, test if the active date changes to today's date once the fragment is loaded to MainActivity.
     */
    @Test
    public void testActiveDate() {
        assertNull(fragment.getActiveDate());
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment).setReorderingAllowed(true).commitNowAllowingStateLoss();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Date selectedDate = fragment.getSelectedDate();
                    Calendar c = Calendar.getInstance();
                    String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
                    assertEquals(dayName, fragment.getActiveDate());
                }
            });
        });
    }

    /**
     * Test if the 'res' flag is set true once the fragment is successfully loaded to the MainActivity
     */
    @Test
    public void testRes() {
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment).setReorderingAllowed(true).commitNowAllowingStateLoss();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean resValue = fragment.getRes();
                    assertEquals(true, resValue);
                }
            });
        });
    }

    /**
     * Test if the location id is set to 0 upon the fragment creation.
     * Then, test if the default location changes to 1 when the fragment is loaded.
     */
    @Test
    public void testLocation() {
        int location = fragment.getLocation();
        assertEquals(0, location);
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment).setReorderingAllowed(true).commitNowAllowingStateLoss();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int locationId = fragment.getLocation();
                    assertEquals(1, locationId);
                }
            });
        });
    }

    /**
     * Test if the default selected date is today's day of the week.
     */
    @Test
    public void testSelectedDate() {
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment).setReorderingAllowed(true).commitNowAllowingStateLoss();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DATE);
                    Date sDate = c.getTime();
                    Date selectedDate = fragment.getSelectedDate();
                    assertEquals(sDate.toString(), selectedDate.toString());
                }
            });
        });
    }

    /**
     * Test if the user id is zero upon the fragment creation.
     * Also test the default user id changes to 1 when the fragment is loaded.
     */
    @Test
    public void testUserId() {
        assertEquals(0, fragment.getUser_id());
        ActivityScenario<MainActivity> mainActivityScenario = ActivityScenario.launch(MainActivity.class);
        mainActivityScenario.onActivity(activity -> {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment).setReorderingAllowed(true).commitNowAllowingStateLoss();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int userId = fragment.getUser_id();
                    assertEquals(1, userId);
                }
            });
        });
    }
}
