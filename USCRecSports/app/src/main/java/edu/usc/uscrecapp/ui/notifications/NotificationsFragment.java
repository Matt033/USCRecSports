package edu.usc.uscrecapp.ui.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import org.w3c.dom.Text;

import edu.usc.uscrecapp.MainActivity;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        TextView currentRes = root.findViewById(R.id.textView1);
        //currentRes.setText("Current Reservations");
        new InfoAsyncTask().execute();
        return root;
    }

    public class InfoAsyncTask extends AsyncTask<Void, Void, Vector<Reservation>> {
        @Override
        protected Vector<Reservation> doInBackground(Void... voids) {
            Vector<Reservation> current_reservations = new Vector<Reservation>();
            try{
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Vector<Integer> timeslots = new Vector<Integer>();
                Vector<Integer> locations = new Vector<Integer>();
                Vector<Integer> reservation_ids = new Vector<Integer>();
                Vector<Date> dates = new Vector<Date>();
                Vector<Integer> availabilities = new Vector<Integer>();
                //placeholder but replace with actual user id's
                int user_id = ((MainActivity) getActivity()).getUserId();

                //To get current and future reservations need to order by dates >= to today's date
                String sql = "SELECT * FROM reservations WHERE user_id=" + user_id + " AND DATE(reservations.date) >= DATE(NOW()) ORDER BY DATE(date) ASC LIMIT 5;";
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
                    int availability_id = resultSet.getInt("availability_id");
                    availabilities.add(availability_id);
                }

                //gets actual timeslots and physical locations
                //orders into reservation class
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
                    Reservation res = new Reservation(start_time, end_time, location, reservation_ids.get(i), dates.get(i), availabilities.get(i));
                    current_reservations.add(res);
                }
                Reservation blankRes = new Reservation("0", "0", "blanklocation", 0, null,0);
                current_reservations.add(blankRes);

                //get previous reservations
                //limits to only 10 reservations
                String prevSql = "SELECT * FROM reservations WHERE user_id = " + user_id + " AND DATE(reservations.date) < DATE(NOW()) LIMIT 5;";
                PreparedStatement prepPrev = connection.prepareStatement(prevSql);
                ResultSet prevResult = prepPrev.executeQuery();
                Vector<Integer> timeslotsPrev = new Vector<Integer>();
                Vector<Integer> locationsPrev = new Vector<Integer>();
                Vector<Integer> reservationsPrev = new Vector<Integer>();
                Vector<Date> datesPrev = new Vector<Date>();
                Vector<Integer> availPrev = new Vector<Integer>();
                while(prevResult.next()){
                    int timeslot = prevResult.getInt("timeslot_id");
                    timeslotsPrev.add(timeslot);
                    int location = prevResult.getInt("location_id");
                    locationsPrev.add(location);
                    int reservation_id = prevResult.getInt("reservation_id");
                    reservationsPrev.add(reservation_id);
                    Date date = prevResult.getDate("date");
                    datesPrev.add(date);
                    int availability_id = prevResult.getInt("availability_id");
                    availPrev.add(availability_id);
                }
                //gets the timeslots of the previous reservations
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
                    Reservation res = new Reservation(start_time, end_time, location, reservationsPrev.get(i), datesPrev.get(i),availPrev.get(i));
                    current_reservations.add(res);
                }
                //divides previous from the waitlist section
                Reservation waitRes = new Reservation("0", "0", "waitlocation", 0, null,0);
                current_reservations.add(waitRes);


                String sqlWait = "SELECT * FROM waiting_lists WHERE user_id=" + user_id + " AND DATE(date) >= DATE(NOW());";
                PreparedStatement waitStatement = connection.prepareStatement(sqlWait);
                ResultSet waitResult = waitStatement.executeQuery();
                Vector<Integer> waitTimeslots = new Vector<Integer>();
                Vector<Integer> waitLocations = new Vector<Integer>();
                Vector<Integer> waitIds = new Vector<Integer>();
                Vector<Date> waitDates = new Vector<Date>();
                Vector<Integer> waitAvailabilities = new Vector<Integer>();
                //need to get the location and timeslot
                while(waitResult.next()){
                    int timeslot = waitResult.getInt("timeslot_id");
                    waitTimeslots.add(timeslot);
                    int location = waitResult.getInt("location_id");
                    waitLocations.add(location);
                    int waitid = waitResult.getInt("waiting_list_id");
                    waitIds.add(waitid);
                    Date date = waitResult.getDate("date");
                    waitDates.add(date);
                    int availability = waitResult.getInt("availability_id");
                    waitAvailabilities.add(availability);
                }

                for(int i = 0; i < waitTimeslots.size(); i++){
                    String sql2 = "SELECT * FROM timeslots WHERE timeslot_id=" + waitTimeslots.get(i);
                    String sql3 = "SELECT location_name FROM recreation_facilities WHERE location_id=" + waitLocations.get(i);
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                    PreparedStatement statement3 = connection.prepareStatement(sql3);
                    ResultSet resultSet2 = statement2.executeQuery(); // timeslot
                    ResultSet resultSet3 = statement3.executeQuery(); // location

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

                    Reservation res = new Reservation(start_time, end_time, location, waitIds.get(i), waitDates.get(i), waitAvailabilities.get(i));
                    current_reservations.add(res);
                }

                connection.close();

            }catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }

            return current_reservations;
        }
        @Override
        protected void onPostExecute(Vector<Reservation> result){
            View root = binding.getRoot();
            //check upcoming and button to make sure they dont have to be zero
            int upcomingMargin = 90;
            int previousMargin = 475;
            int buttonMargin = 90;
            int prevIndex = 0;
            RelativeLayout upcomingRes = (RelativeLayout)root.findViewById(R.id.upcoming_layout);
            RelativeLayout previousRes = (RelativeLayout)root.findViewById(R.id.previous_layout);
            RelativeLayout waitRes = (RelativeLayout)root.findViewById(R.id.waiting_layout);
            //handles upcoming reservations - these should have cancel buttons
            for(int i = 0; i < result.size(); i++){
                String location = result.get(i).location;
                int reservation_id = result.get(i).reservation_id;
                String startTime = result.get(i).start_time;
                String endTime = result.get(i).end_time;
                Date date = result.get(i).date;
                int availability_id = result.get(i).availability_id;
                if(location == "blanklocation"){
                    prevIndex = i+1;
                    break;
                }
                TextView newReservation = new TextView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, upcomingMargin, 0, 0);
                newReservation.setLayoutParams(layoutParams);
                upcomingMargin += 90;
                String displayText = location + " from " + startTime + " to " + endTime + " on " + date;
                newReservation.setText(displayText);
                newReservation.setTextSize(12);
                upcomingRes.addView(newReservation);
                //cancellation button for each upcoming reservation

                RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(950, buttonMargin, 0, 0);
                buttonParams.width = 100;
                buttonParams.height = 80;
                Button cancelButton = new Button(root.getContext());
                cancelButton.setLayoutParams(buttonParams);
                buttonMargin += 90;
                cancelButton.setBackgroundColor(Color.RED);
                cancelButton.setText("Cancel");
                cancelButton.setTextSize(7);
                cancelButton.setTextColor(Color.WHITE);
                //this needs to remove res from user database
                //update current availability for the location (+1)
                //If current availability was zero, notify the first person on waiting list
                //Remove this reservation from the user screen
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        upcomingRes.removeView(view); //removes button
                        upcomingRes.removeView(newReservation); //removes reservation
                        System.out.println("calling timeselect function here");
                        new TimeSelectAsyncTask(reservation_id, availability_id).execute();
                    }
                });
                upcomingRes.addView(cancelButton);
            }
            int waitIndex = 0;
            for(int j = prevIndex; j < result.size(); j++){
                String location = result.get(j).location;
                if(location == "waitlocation"){
                    waitIndex = j+1;
                    break;
                }
                String startTime = result.get(j).start_time;
                String endTime = result.get(j).end_time;
                Date date = result.get(j).date;
                TextView newReservation = new TextView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, previousMargin, 0, 0);
                newReservation.setLayoutParams(layoutParams);
                previousMargin += 50;
                String displayText = location + " from " + startTime + " to " + endTime + " on " + date;
                newReservation.setText(displayText);
                newReservation.setTextSize(12);
                previousRes.addView(newReservation);
            }
            int waitMargin = 80;
            for(int k = waitIndex; k < result.size(); k++){
                String location = result.get(k).location;
                String startTime = result.get(k).start_time;
                String endTime = result.get(k).end_time;
                Date date = result.get(k).date;
                TextView newReservation = new TextView(root.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, waitMargin, 0, 0);
                newReservation.setLayoutParams(layoutParams);
                waitMargin += 50;
                String displayText = location + " from " + startTime + " to " + endTime + " on " + date;
                newReservation.setText(displayText);
                newReservation.setTextSize(12);
                waitRes.addView(newReservation);
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
        int availability_id;

        public Reservation(String start, String end, String place, int reservation, Date date, int availability){
            this.start_time = start;
            this.end_time = end;
            this.location = place;
            this.reservation_id = reservation;
            this.date = date;
            this.availability_id = availability;
        }
    }

    public class TimeSelectAsyncTask extends AsyncTask<Void, Void, Void>{
        int reservation_id;
        int availability_id;
        public TimeSelectAsyncTask(int reservation, int availability){
            this.reservation_id = reservation;
            this.availability_id = availability;
        }
        @Override
        protected Void doInBackground(Void...voids){
            System.out.println("Inside of timselectasync");
            try{
                //removes reservation for user
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = connection.createStatement();
                String sql = "DELETE FROM reservations WHERE reservation_id=" + reservation_id;
                //PreparedStatement statement = connection.prepareStatement(sql);
                stmt.executeUpdate(sql);
                //ResultSet result = statement.executeQuery();

                //each reservation is going to have to be associated with an availability id
                String sql2 = "SELECT * FROM availability WHERE availability_id=" + availability_id;
                PreparedStatement statement2 = connection.prepareStatement(sql2);
                ResultSet result2 = statement2.executeQuery();
                int currSpots = 0;
                while(result2.next()){
                    currSpots = result2.getInt("slots_available");
                    //if currspots is zero and a waiting list exists, notify first user on list
                    //user must be deleted from the waiting list after being notified
                    //waiting list should only be maintained for current and future dates
                    if(currSpots == 0){
                        String sqlWait = "SELECT * FROM waiting_lists WHERE availability_id=" + availability_id;
                        PreparedStatement statementWait = connection.prepareStatement(sqlWait);
                        ResultSet resultWait = statementWait.executeQuery();
                        Vector<Integer> userIds = new Vector<Integer>();
                        while(resultWait.next()){
                            int user_id = resultWait.getInt("user_id");
                            userIds.add(user_id);
                        }
                        Vector<String> emails = new Vector<String>();
                        for(int i = 0; i < userIds.size(); i++){
                            String sqlUser = "SELECT * FROM users WHERE user_id=" + userIds.get(i);
                            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                            ResultSet resultUser = statementUser.executeQuery();
                            while(resultUser.next()){
                                String email = resultUser.getString("email");
                                emails.add(email);
                            }
                        }
                        String[] emailList = new String[emails.size()];
                        for(int j = 0; j < emails.size(); j++){
                            emailList[j] = emails.get(j);
                        }

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailList);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "USC REC Sports Waiting List Update");
                        //location timeslot, date
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "A new spot has opened up for one of your waitlist reservations. Please visit the USCRecSportsApp now to book!");
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));





                    }
                }
                currSpots++;
                Statement stmt2 = connection.createStatement();
                String sql3 = "UPDATE availability SET slots_available="+ currSpots + " WHERE availability_id=" + availability_id;
                //PreparedStatement statement3 = connection.prepareStatement(sql3);
                //ResultSet result3 = statement3.executeQuery();
                stmt2.executeUpdate(sql3);

            } catch (Exception e) {
                Log.e("USC Rec Sports", "Error during MySQL communication", e);
            }
            return null;
        }
    }
}