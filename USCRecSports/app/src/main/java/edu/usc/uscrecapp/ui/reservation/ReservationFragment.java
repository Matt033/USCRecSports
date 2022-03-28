package edu.usc.uscrecapp.ui.reservation;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.sql.*;
import java.util.Map;
import java.util.Date;

import javax.xml.transform.Result;

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.databinding.FragmentReservationBinding;

public class ReservationFragment extends Fragment {

    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private FragmentReservationBinding binding;
    private View root;
    int buttonIDs[] = {R.id.button4, R.id.button5, R.id.button6, R.id.button7};
    HashMap<Integer, Availability> info = new HashMap<>();
    String activeDate;
    String dateName;
    String dateName2;
    String dateName3;
    Date selectedDate;
    Date firstDate, secondDate, thirdDate;

    // This flag represents the mode between reservation and wait-list
    boolean res = true;
    int location;
    int user_id;

    @Override
    public void onResume(){
        super.onResume();
        // set title of the reservation fragment page
        if (location == 2)
            ((MainActivity) getActivity()).setActionBarTitle("Reservation - Cromwell Track");
        else if (location == 1)
            ((MainActivity) getActivity()).setActionBarTitle("Reservation - Lyon Center");
        else
            ((MainActivity) getActivity()).setActionBarTitle("Reservation - UV Fitness Center");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel reservationViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);

        binding = FragmentReservationBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        final TextView textView = binding.textReservation;
        reservationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        location = ((MainActivity) getActivity()).getLocationId();
        user_id = ((MainActivity) getActivity()).getUserId();
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                location = bundle.getInt("location_id");
                user_id = bundle.getInt("user_id");
                Log.i(">>>>>", "location received from home fragment is "+location);
                Log.i(">>>>>", "user_id received from home fragment is "+user_id);
                new InfoAsyncTask().execute();
            }
        });
        Calendar c = Calendar.getInstance();
        String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        dateName = dayName;
        activeDate = dateName;
        int day = c.get(Calendar.DATE);
        firstDate = c.getTime();
        selectedDate = firstDate;
        Button b1 = root.findViewById(R.id.button1);
        Button b2 = root.findViewById(R.id.button2);
        Button b3 = root.findViewById(R.id.button3);
        b1.setBackgroundColor(Color.RED);
        b2.setBackgroundColor(Color.BLUE);
        b3.setBackgroundColor(Color.BLUE);

        b1.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        secondDate = c.getTime();
        day++;
        dateName2 = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b2.setText(day + "\n" + dateName2);
        c.add(Calendar.DATE, 1);
        thirdDate = c.getTime();
        day++;
        dateName3 = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b3.setText(day + "\n" + dateName3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Button b1 = (Button)view;
                b1.setBackgroundColor(Color.RED);
                b2.setBackgroundColor(Color.BLUE);
                b3.setBackgroundColor(Color.BLUE);
                new DateSelectAsyncTask(R.id.button1, dateName).execute();
                activeDate = dateName;
                selectedDate = firstDate;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                b1.setBackgroundColor(Color.BLUE);
                b2.setBackgroundColor(Color.RED);
                b3.setBackgroundColor(Color.BLUE);
                new DateSelectAsyncTask(R.id.button2, dateName2).execute();
                activeDate = dateName2;
                selectedDate = secondDate;
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                b1.setBackgroundColor(Color.BLUE);
                b2.setBackgroundColor(Color.BLUE);
                b3.setBackgroundColor(Color.RED);
                new DateSelectAsyncTask(R.id.button3, dateName3).execute();
                activeDate = dateName3;
                selectedDate = thirdDate;
            }
        });

        Button rb = root.findViewById(R.id.button);
        rb.setBackgroundColor(Color.BLUE);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                TextView t = root.findViewById(R.id.confirmation);
                if (res) {
                    b.setBackgroundColor(Color.RED);
                    b.setText("switch to Reservation");
                    res = false;
                    t.setText("Select time to be wait-listed.");
                } else {
                    b.setBackgroundColor(Color.BLUE);
                    res = true;
                    b.setText("remind me");
                    t.setText("Select time to make a reservation.");
                }
                for (int i = 0; i < buttonIDs.length; i++) {
                    Button btn = root.findViewById(buttonIDs[i]);
                    if (btn.isEnabled())
                        btn.setEnabled(false);
                    else
                        btn.setEnabled(true);
                }
            }
        });

        TextView t = root.findViewById(R.id.confirmation);
        t.setText("Select time to make a reservation.");
        new InfoAsyncTask().execute();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class Availability{
        String starttime;
        String endtime;
        int slotsAvailable;
        int button_id;
        int timeslot_id;
    };

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<Integer, Availability>> {
        @Override
        protected Map<Integer, Availability> doInBackground(Void... voids) {
            //View root = binding.getRoot();
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

                for(int i = 1; i < 5; i++) {
                    String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                            "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                    PreparedStatement statement = connection.prepareStatement(sql);
                    String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName +"'";
                    //Log.i(">>>>>>", sql2);
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    ResultSet resultSet = statement.executeQuery();
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet.next()) {
                        resultSet2.next();
                        int slots = resultSet2.getInt("slots_available");
                        Availability a = new Availability();
                        a.button_id = buttonIDs[i-1];
                        a.starttime = resultSet.getString("start_time");
                        a.endtime = resultSet.getString("end_time");
                        a.slotsAvailable = slots;
                        a.timeslot_id = i;
                        info.put(buttonIDs[i-1], a);
                        Log.i("map", ""+buttonIDs[i-1]+" id");
                    }
                }
            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return info;
        }

        @Override
        protected void onPostExecute(Map<Integer, Availability> result) {
            for (int i = 0; i < result.size(); i++) {
                Availability a = result.get(buttonIDs[i]);
                Button b = root.findViewById(buttonIDs[i]);
                //Log.i(">>>>> ", "hello "+ a.timeslot_id);
                String label =  a.starttime + " - " + a.endtime + "    " +
                                a.slotsAvailable + " slots available";
                b.setText(label);
                if(a.slotsAvailable == 0){
                    b.setEnabled(false);
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(">>> slots", ""+a.slotsAvailable);
                        if (res)
                            a.slotsAvailable--;
                        new TimeSelectAsyncTask(a.button_id, a.starttime, a.endtime, a.slotsAvailable, a.timeslot_id).execute();
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class DateSelectAsyncTask extends AsyncTask<Void, Void, Map<Integer, Availability>> {
        int buttonID;
        String dateName;

        public DateSelectAsyncTask(int id, String dt) {
            buttonID = id;
            dateName = dt;
        }

        @Override
        protected Map<Integer, Availability> doInBackground(Void... voids) {
            int count;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                info.clear();
                for(int i = 1; i < 5; i++) {
                    String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                            "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                    PreparedStatement statement = connection.prepareStatement(sql);
                    String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName +"'";
                    //Log.i(">>>>>>", sql2);
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    ResultSet resultSet = statement.executeQuery();
                    ResultSet resultSet2 = statement2.executeQuery();
                    while (resultSet.next()) {
                        resultSet2.next();
                        int slots = resultSet2.getInt("slots_available");
                        Availability a = new Availability();
                        a.starttime = resultSet.getString("start_time");
                        a.endtime = resultSet.getString("end_time");
                        a.slotsAvailable = slots;
                        a.timeslot_id = i;
                        a.button_id = buttonIDs[i-1];
                        info.put(buttonIDs[i-1], a);
                    }
                }

            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return info;
        }

        @Override
        protected void onPostExecute(Map<Integer, Availability> result) {
            for (int i = 0; i < result.size(); i++) {
                //Log.i(">>> timeslect "+i, ""+buttonIDs[i]);
                Availability a = result.get(buttonIDs[i]);
                Button b = root.findViewById(buttonIDs[i]);
                String label =  a.starttime + " - " + a.endtime + "    " +
                                a.slotsAvailable + " slots available";
                b.setText(label);
                if (res) {
                    if (a.slotsAvailable == 0)
                        b.setEnabled(false);
                    else
                        b.setEnabled(true);
                } else {
                    if (a.slotsAvailable == 0)
                        b.setEnabled(true);
                    else
                        b.setEnabled(false);
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Log.i(">>>> button ids "+info.size(), ""+info.keySet());
                        if (res)
                            a.slotsAvailable--;
                        new TimeSelectAsyncTask(a.button_id, a.starttime, a.endtime, a.slotsAvailable, a.timeslot_id).execute();
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TimeSelectAsyncTask extends AsyncTask<Void, Void, Availability> {
        int buttonID;
        String starttime;
        String endtime;
        int slots_available;  // available slots
        int timeslot_id;

        public TimeSelectAsyncTask(int id, String start, String end, int slots_avail, int tid) {
            buttonID = id;
            starttime = start;
            endtime = end;
            slots_available = slots_avail;
            timeslot_id = tid;
        }

        @Override
        protected Availability doInBackground(Void... voids) {
            Availability a = new Availability();
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                info.clear();
                String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                        "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + timeslot_id;
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                PreparedStatement statement2;
                sql = "SELECT availability_id FROM availability WHERE location_id= ? AND timeslot_id=? AND date=?";
                statement2 = connection.prepareStatement(sql);
                statement2.setInt(1, location);
                statement2.setInt(2, timeslot_id);
                statement2.setString(3, activeDate);
                ResultSet resultSet3 = statement2.executeQuery();
                resultSet3.next();
                int availability_id = resultSet3.getInt("availability_id");
                Log.i("availid", "" + availability_id);
                if(res) {
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ? AND timeslot_id=? AND date=?";
                    statement2 = connection.prepareStatement(sql);
                    statement2.setInt(1, slots_available);
                    statement2.setInt(2, location);
                    statement2.setInt(3, timeslot_id);
                    statement2.setString(4, activeDate);
                    statement2.executeUpdate();
                    sql = "INSERT INTO reservations (user_id, timeslot_id, location_id, date, availability_id) VALUES (?,?,?,?,?)";
                }
                else{
                    sql = "INSERT INTO waiting_lists (user_id, timeslot_id, location_id, date, availability_id) VALUES (?,?,?,?,?)";
                }
                java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
                statement2 = connection.prepareStatement(sql);
                statement2.setInt(1, user_id);
                statement2.setInt(2, timeslot_id);
                statement2.setInt(3, location);
                statement2.setDate(4, sqlDate);
                statement2.setInt(5, availability_id);
                statement2.executeUpdate();
                while (resultSet.next()) {
                    a.starttime = resultSet.getString("start_time");
                    a.endtime = resultSet.getString("end_time");
                    a.slotsAvailable = slots_available;
                    a.button_id = buttonID;
                    a.timeslot_id = timeslot_id;
                    //Log.i(">>>>bid ", ""+buttonID);
                }
            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return a;
        }

        @Override
        protected void onPostExecute(Availability a) {
            Log.i(">>>>> ", "hello world "+a.button_id);
            Button b = root.findViewById(a.button_id);
            String label =
                    a.starttime + " - " + a.endtime + "    " +
                            a.slotsAvailable + " slots available";
            b.setText(label);

            TextView t = root.findViewById(R.id.confirmation);
            if (res) {
                if (a.slotsAvailable == 0)
                    b.setEnabled(false);
                else
                    b.setEnabled(true);
                t.setText("Reserved " + a.starttime + "-" + a.endtime + " on " + activeDate);
            } else {
                t.setText("Wait-listed "+ a.starttime + "-" + a.endtime + " on " + activeDate);
            }
        }
    }

}