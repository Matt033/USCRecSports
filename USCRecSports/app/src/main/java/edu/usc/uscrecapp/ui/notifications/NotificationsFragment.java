package edu.usc.uscrecapp.ui.notifications;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private static final String PASSWORD = "root";
    //private static final String PASSWORD = "Matthewwilson033!";


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
        return root;
    }

    public class InfoAsyncTask extends AsyncTask<Void, Void, Vector<Reservation>> {
        @Override
        protected Vector<Reservation> doInBackground(Void... voids) {
            Log.d("ASYNCTASK", "ASYNCTASK is now being run");
            Vector<Reservation> current_reservations = new Vector<Reservation>();
            try{
                //Class.forName("com.mysql.cj.jdbc.Driver");
                Log.d("TRY", "Inside of TRY method!");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Log.d("AFTER", "Inside of AFTER method! YOU HAVE CONQUERED!!!");
                Vector<Integer> timeslots = new Vector<Integer>();
                Vector<Integer> locations = new Vector<Integer>();
                Vector<Integer> reservation_ids = new Vector<Integer>();
                Vector<Date> dates = new Vector<Date>();
                String user_id = "3";
                //To get current and future reservations need to order by dates >= to today's date
                String sql = "SELECT * FROM reservations WHERE user_id=" + user_id + " AND DATE(reservations.date) >= DATE(NOW());";
               // String sql = "SELECT * FROM reservations WHERE user_id = " + user_id;
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    int timeslot = resultSet.getInt("timeslot_id");
                    timeslots.add(timeslot);
                    int location = resultSet.getInt("location_id");
                    locations.add(location);
                    int reservation_id = resultSet.getInt("reservation_id");
                    reservation_ids.add(reservation_id);
                    Date date = resultSet.getDate("date");
                    dates.add(date);
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
                    Reservation res = new Reservation(start_time, end_time, location, reservation_ids.get(i), dates.get(i));
                    current_reservations.add(res);
                }
                Reservation blankRes = new Reservation("0", "0", "blanklocation", 0, null);
                current_reservations.add(blankRes);

                //get previous reservations
                //limits to only 10 reservations
                String prevSql = "SELECT * FROM reservations WHERE user_id = " + user_id + " AND DATE(reservations.date) <= DATE(NOW());";
                PreparedStatement prepPrev = connection.prepareStatement(prevSql);
                ResultSet prevResult = prepPrev.executeQuery();
                Vector<Integer> timeslotsPrev = new Vector<Integer>();
                Vector<Integer> locationsPrev = new Vector<Integer>();
                Vector<Integer> reservationsPrev = new Vector<Integer>();
                Vector<Date> datesPrev = new Vector<Date>();
                while(prevResult.next()){
                    int timeslot = prevResult.getInt("timeslot_id");
                    System.out.println(timeslot);
                    timeslotsPrev.add(timeslot);
                    int location = prevResult.getInt("location_id");
                    locationsPrev.add(location);
                    int reservation_id = prevResult.getInt("reservation_id");
                    reservationsPrev.add(reservation_id);
                    Date date = prevResult.getDate("date");
                    datesPrev.add(date);
                }


                for(int i = 0; i < timeslotsPrev.size(); i++){
                    String sql4 = "SELECT * FROM timeslots WHERE timeslot_id=" + timeslotsPrev.get(i);
                    String sql5 = "SELECT location_name FROM recreation_facilities WHERE location_id=" + locationsPrev.get(i);
                    PreparedStatement statement2 = connection.prepareStatement(sql4);
                    PreparedStatement statement3 = connection.prepareStatement(sql5);
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
                    Reservation res = new Reservation(start_time, end_time, location, reservationsPrev.get(i), datesPrev.get(i));
                    current_reservations.add(res);
                }

            }catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }

            return current_reservations;
        }
        @Override
        protected void onPostExecute(Vector<Reservation> result){
            Log.d("POSTEXECUTE", "iN POST EXECUTE FUNCTION");
            View root = binding.getRoot();
            int upcomingMargin = 10;
            int previousMargin = 400;
            int buttonMargin = 0;
            int prevIndex = 0;
            RelativeLayout upcomingRes = (RelativeLayout)root.findViewById(R.id.upcoming_layout);
            RelativeLayout previousRes = (RelativeLayout)root.findViewById(R.id.previous_layout);
            //handles upcoming reservations - these should have cancel buttons
            for(int i = 0; i < result.size(); i++){
                String location = result.get(i).location;
                int reservation_id = result.get(i).reservation_id;
                if(location == "blanklocation"){
                    prevIndex = i+1;
                    break;
                }
                TextView newReservation = new TextView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, upcomingMargin, 0, 0);
                newReservation.setLayoutParams(layoutParams);
                upcomingMargin += 50;
                newReservation.setText("Reservation at " + location);
                upcomingRes.addView(newReservation);
                //cancellation button for each upcoming reservation

                RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(300, buttonMargin, 0, 0);
                buttonParams.width = 95;
                Button cancelButton = new Button(root.getContext());
                cancelButton.setLayoutParams(buttonParams);
                buttonMargin += 50;
                cancelButton.setBackgroundColor(Color.RED);
                cancelButton.setText("Cancel");
                //this needs to remove res from user database
                //update current availability for the location (+1)
                //If current availability was zero, notify the first person on waiting list
                //Remove this reservation from the user screen
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        upcomingRes.removeView(view); //removes button
                        upcomingRes.removeView(newReservation); //removes reservation

                        try{
                            //removes reservation for user
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                            String sql = "DELETE FROM reservations WHERE reservation_id=" + reservation_id;
                            PreparedStatement statement = connection.prepareStatement(sql);
                            ResultSet result = statement.executeQuery();

                            //each reservation is going to have to be associated with an availability id

                        } catch (Exception e) {
                            Log.e("USC Rec Sports", "Error during MySQL communication", e);
                        }


                    }
                });
                upcomingRes.addView(cancelButton);



                // you can probably add location id, user id etc. to data params in Reservation object
            }

            for(int j = prevIndex; j < result.size(); j++){
                String location = result.get(j).location;
                TextView newReservation = new TextView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, previousMargin, 0, 0);
                newReservation.setLayoutParams(layoutParams);
                previousMargin += 50;
                newReservation.setText("Reservation at " + location);
                previousRes.addView(newReservation);
            }
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
        int reservation_id;
        Date date;

        public Reservation(String start, String end, String place, int reservation, Date date){
            this.start_time = start;
            this.end_time = end;
            this.location = place;
            this.reservation_id = reservation;
            this.date = date;
        }
    }
}