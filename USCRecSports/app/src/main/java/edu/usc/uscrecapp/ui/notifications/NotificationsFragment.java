package edu.usc.uscrecapp.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.usc.uscrecapp.R;

import java.sql.*;
import java.util.Map;
import java.util.Vector;


import edu.usc.uscrecapp.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static final String USER = "root";
    private static final String PASSWORD = "Matthewwilson033!";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Log.d("CREATION", "CREATE is now being run");
        TextView currentRes = root.findViewById(R.id.textView1);
        currentRes.setText("Current Reservations");
        new InfoAsyncTask().execute();
//
        return root;
    }

    public class InfoAsyncTask extends AsyncTask<Void, Void, Vector<Reservation>> {
        protected Vector<Reservation> doInBackground(Void... voids) {
            Log.d("ASYNCTASK", "ASYNCTASK is now being run");
            Vector<Reservation> total_reservations = new Vector<Reservation>();
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Log.d("TRY", "Inside of TRY method!");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Log.d("AFTER", "Inside of AFTER method! YOU HAVE CONQUERED!!!");
                Vector<Integer> timeslots = new Vector<Integer>();
                Vector<Integer> locations = new Vector<Integer>();
                String user_id = "mtwilson";
                //Use username here instead of user_id because user_id is a primary key
                String sql = "SELECT * FROM reservations WHERE username='" + user_id + "'";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    int timeslot = resultSet.getInt("timeslot_id");
                    timeslots.add(timeslot);
                    int location = resultSet.getInt("location_id");
                    locations.add(location);
                }

                for(int i = 0; i < timeslots.size(); i++){
                    String sql2 = "SELECT * FROM timeslots WHERE timeslot_id=" + timeslots.get(i);
                    String sql3 = "SELECT location_name FROM recreation_facilities WHERE location_id=" + locations.get(i);
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    PreparedStatement statement3 = connection.prepareStatement(sql3);
                    ResultSet resultSet2 = statement2.executeQuery();
                    ResultSet resultSet3 = statement3.executeQuery();
                    String location = "";
                    while(resultSet3.next()){
                        location = resultSet3.getString("location_name");
                    }
                    String start_time = "";
                    String end_time = "";
                    while(resultSet2.next()){
                        start_time = resultSet2.getString("start_time");
                        end_time = resultSet2.getString("end_time");
                    }
                    Reservation res = new Reservation(location, start_time, end_time);
                    total_reservations.add(res);
                }
            }catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }

            return total_reservations;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public class Reservation{
        String start_time;
        String end_time;
        String location;

        public Reservation(String start, String end, String place){
            this.start_time = start;
            this.end_time = end;
            this.location = place;
        }
    }
}