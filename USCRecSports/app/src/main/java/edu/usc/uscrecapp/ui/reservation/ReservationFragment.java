package edu.usc.uscrecapp.ui.reservation;

import android.annotation.SuppressLint;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.sql.*;
import java.util.Map;

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
    Map<Integer, Availability> info = new HashMap<>();
    String activeDate;
    String dateName;
    String dateName2;
    String dateName3;
    boolean res = true;
    int location;
    int user_id;
    //@Override
    public void onClick(View v) {
        int count = 0;
        int c;
        switch(v.getId()) {
            case R.id.button1:
                info.clear();
                count = 0;
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    for(int i = 1; i < 5; i++) {
                        String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                                "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                        PreparedStatement statement = connection.prepareStatement(sql);
                        String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName +"'";
                        Log.i(">>>>>>", sql2);
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
                            info.put(buttonIDs[count], a);
                            count++;
                        }
                    }
                } catch (Exception e) {
                    Log.e("USC Rec Sports", "Error during MySQL communication", e);
                }
                for (int i = 0; i < info.size(); i++) {
                    Availability a = info.get(buttonIDs[i]);
                    View root = binding.getRoot();
                    Button b = root.findViewById(buttonIDs[i]);
                    String label = a.starttime + " - " + a.endtime + "    " + a.slotsAvailable + " slots available";
                    b.setText(label);
                    if(a.slotsAvailable == 0){
                        b.setEnabled(false);
                    }
                }

            case R.id.button2:
                info.clear();
                count = 0;
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    for(int i = 1; i < 5; i++) {
                        String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                                "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                        PreparedStatement statement = connection.prepareStatement(sql);
                        String sql2 = "FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName2 +"'";
                        Log.i(">>>>>>", sql2);
                        Log.i(">>>>>>>", "AHHHHHHHHHHHHHHHHHHHHHHH");
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
                            info.put(buttonIDs[count], a);
                            count++;
                        }
                    }
                } catch (Exception e) {
                    Log.e("USC Rec Sports", "Error during MySQL communication", e);
                }
                for (int i = 0; i < info.size(); i++) {
                    Availability a = info.get(buttonIDs[i]);
                    View root = binding.getRoot();
                    Button b = root.findViewById(buttonIDs[i]);
                    String label = a.starttime + " - " + a.endtime + "    " + a.slotsAvailable + " slots available";
                    b.setText(label);
                    if(a.slotsAvailable == 0){
                        b.setEnabled(false);
                    }
                }

            case R.id.button3:
                info.clear();
                count = 0;
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    for(int i = 1; i < 5; i++) {
                        String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                                "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                        PreparedStatement statement = connection.prepareStatement(sql);
                        String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName3 +"'";
                        Log.i(">>>>>>", sql2);
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
                            info.put(buttonIDs[count], a);
                            count++;
                        }
                    }
                } catch (Exception e) {
                    Log.e("USC Rec Sports", "Error during MySQL communication", e);
                }
                for (int i = 0; i < info.size(); i++) {
                    Availability a = info.get(buttonIDs[i]);
                    View root = binding.getRoot();
                    Button b = root.findViewById(buttonIDs[i]);
                    String label = a.starttime + " - " + a.endtime + "    " + a.slotsAvailable + " slots available";
                    b.setText(label);
                    if(a.slotsAvailable == 0){
                        b.setEnabled(false);
                    }
                }

            case R.id.button4:
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM availability WHERE location_id=" + location;
                    Log.i("s", sql);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    c = resultSet.getInt("slots_available");
                    c--;
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, c);
                    statement.setInt(2, location);
                    int row = statement.executeUpdate();
                    Button b4 = v.findViewById(buttonIDs[0]);
                    //String label = a.starttime + " - " + a.endtime + "    " + a.slotsAvailable + " slots available";
                    //b4.setText(label);
                } catch (Exception e) {
                    Log.e("InfoAsyncTask", "Error reading school information", e);
                }

            case R.id.button5:
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM availability WHERE location_id=" + location;
                    Log.i("s", sql);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    c = resultSet.getInt("slots_available");
                    c--;
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, c);
                    statement.setInt(2, location);
                    int row = statement.executeUpdate();
                    System.out.println(row);
                } catch (Exception e) {
                    Log.e("InfoAsyncTask", "Error reading school information", e);
                }

            case R.id.button6:
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM availability WHERE location_id=" + location;
                    Log.i("s", sql);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    c = resultSet.getInt("slots_available");
                    c--;
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, c);
                    statement.setInt(2, location);
                    int row = statement.executeUpdate();
                    System.out.println(row);
                } catch (Exception e) {
                    Log.e("InfoAsyncTask", "Error reading school information", e);
                }

            case R.id.button7:
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM availability WHERE location_id=" + location;
                    Log.i("s", sql);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    c = resultSet.getInt("slots_available");
                    c--;
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, c);
                    statement.setInt(2, location);
                    int row = statement.executeUpdate();
                    System.out.println(row);
                } catch (Exception e) {
                    Log.e("InfoAsyncTask", "Error reading school information", e);
                }
        }
    }

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
        Connection connection;
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                location = bundle.getInt("location_id");
                user_id = bundle.getInt("user_id");
                Log.i(">>>>>", "location received from home fragment is "+location);
                Log.i(">>>>>", "user_id received from home fragment is "+user_id);
                new InfoAsyncTask().execute();
                // function to initialize info
            }
        });
        Calendar c = Calendar.getInstance();
        String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        dateName = dayName;
        activeDate = dateName;
        int day = c.get(Calendar.DATE);
        Button b1 = root.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DateSelectAsyncTask(R.id.button1, dateName).execute();
                activeDate = dateName;
            }
        });

        Button b2 = root.findViewById(R.id.button2);
        Button b3 = root.findViewById(R.id.button3);
        b1.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        day++;
        dateName2 = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DateSelectAsyncTask(R.id.button2, dateName2).execute();
                activeDate = dateName2;
            }
        });

        b2.setText(day + "\n" + dateName2);
        c.add(Calendar.DATE, 1);
        day++;
        dateName3 = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DateSelectAsyncTask(R.id.button3, dateName3).execute();
                activeDate = dateName3;
            }
        });
        b3.setText(day + "\n" + dateName3);

        Log.i(">>>>>", "Joshua "+buttonIDs[0]);
        Button b4 = root.findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(">>>>timeid", ""+info.get(R.id.button4).timeslot_id);
                new TimeSelectAsyncTask(R.id.button4, info.get(R.id.button4).starttime, info.get(R.id.button4).endtime, info.get(R.id.button4).slotsAvailable, info.get(R.id.button4).timeslot_id).execute();
            }
        });
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
            int count = 0;
            //View root = binding.getRoot();
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

                for(int i = 1; i < 5; i++) {
                    String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                            "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                    PreparedStatement statement = connection.prepareStatement(sql);
                    String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + i + " AND date='" + dateName +"'";
                    Log.i(">>>>>>", sql2);
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
                        info.put(buttonIDs[count], a);
                        count++;
                    }
                }
            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return info;
        }

        @Override
        protected void onPostExecute(Map<Integer, Availability> result) {
            Log.i(">>>>> ", "hello");
            for (int i = 0; i < result.size(); i++) {
                Availability a = result.get(buttonIDs[i]);
                View root = binding.getRoot();
                Button b = root.findViewById(buttonIDs[i]);
                String label = //(String) b4.getText() +
                        a.starttime + " - " + a.endtime + "    " +
                                a.slotsAvailable + " slots available";
                b.setText(label);
                if(a.slotsAvailable == 0){
                    b.setEnabled(false);
                }
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
                    Log.i(">>>>>>", sql2);
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
            Log.i(">>>>> ", "hello world");
            for (int i = 0; i < result.size(); i++) {
                Availability a = result.get(buttonIDs[i]);
                //View root = binding.getRoot();
                Button b = root.findViewById(buttonIDs[i]);
                String label =
                        a.starttime + " - " + a.endtime + "    " +
                                a.slotsAvailable + " slots available";
                b.setText(label);
                if(a.slotsAvailable == 0){
                    b.setEnabled(false);
                }
                else{
                    b.setEnabled(true);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class TimeSelectAsyncTask extends AsyncTask<Void, Void, Availability> {
        int buttonID;
        String starttime;
        String endtime;
        int slot;  // available slots
        int timeslot_id;

        public TimeSelectAsyncTask(int id, String start, String end, int slots_avail, int tid) {
            buttonID = id;
            Log.i("timeslotid",""+tid);
            starttime = start;
            endtime = end;
            slot = slots_avail;
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
                Log.i(">>>>>>", sql);
                /*String sql2 = "SELECT * FROM availability WHERE location_id=" + location + " AND timeslot_id=" + timeslot_id + " AND date='" + dateName +"'";
                PreparedStatement statement2 = connection.prepareStatement(sql2);*/
                ResultSet resultSet = statement.executeQuery();
                //ResultSet resultSet2 = statement2.executeQuery();
                sql = "UPDATE availability SET slots_available= ? WHERE location_id= ? AND timeslot_id=? AND date=?";
                PreparedStatement statement2 = connection.prepareStatement(sql);
                statement2.setInt(1, slot--);
                statement2.setInt(2, location);
                statement2.setInt(3, timeslot_id);
                statement2.setString(4, activeDate);
                statement2.executeUpdate();
                while (resultSet.next()) {
                    //resultSet2.next();
                    //slot = resultSet2.getInt("slots_available");
                    a.starttime = resultSet.getString("start_time");
                    a.endtime = resultSet.getString("end_time");
                    a.slotsAvailable = slot;
                    a.button_id = buttonID;
                    a.timeslot_id = timeslot_id;
                    Log.i(">>>>bid ", ""+buttonID);
                }

            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return a;
        }

        @Override
        protected void onPostExecute(Availability a) {
            Log.i(">>>>> ", "hello world "+a.button_id);
                //View root = binding.getRoot();
                Button b = root.findViewById(a.button_id);
                String label =
                        a.starttime + " - " + a.endtime + "    " +
                                a.slotsAvailable + " slots available";
                b.setText(label);
                if(a.slotsAvailable == 0){
                    b.setEnabled(false);
                }
                else{
                    b.setEnabled(true);
                }
        }
    }

}