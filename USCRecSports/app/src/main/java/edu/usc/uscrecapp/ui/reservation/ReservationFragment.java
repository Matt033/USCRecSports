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
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.sql.*;
import java.util.Map;

import javax.xml.transform.Result;

import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.databinding.FragmentReservationBinding;

public class ReservationFragment extends Fragment /*implements View.OnClickListener*/{

    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private FragmentReservationBinding binding;
    int buttonIDs[] = {R.id.button4, R.id.button5, R.id.button6, R.id.button7};
    Map<Integer, Availability> info = new HashMap<>();
    String dateName;
    int location = 1;
    //@Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button4:
                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String sql = "SELECT * FROM availability WHERE location_id=2";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery();
                    int count = resultSet.getInt("slots_available");
                    count--;
                    sql = "UPDATE availability SET slots_available= ? WHERE location_id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, count);
                    statement.setInt(2, 2);
                    int row = statement.executeUpdate();
                    System.out.println(row);
                } catch (Exception e) {
                    Log.e("InfoAsyncTask", "Error reading school information", e);
                }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel reservationViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);

        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReservation;
        reservationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Calendar c = Calendar.getInstance();
        String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        dateName = dayName;
        int day = c.get(Calendar.DATE);
        Button b1 = root.findViewById(R.id.button1);
        //b1.setOnClickListener(this);
        Button b2 = root.findViewById(R.id.button2);
        //b2.setOnClickListener(this);
        Button b3 = root.findViewById(R.id.button3);
        //b3.setOnClickListener(this);
        b1.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        day++;
        dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b2.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        day++;
        dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b3.setText(day + "\n" + dayName);
        Button b4 = root.findViewById(R.id.button4);
        Log.i(">>>>>", "Joshua");
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
    };

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<Integer, Availability>> {
        @Override
        protected Map<Integer, Availability> doInBackground(Void... voids) {
            int count = 0;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                for(int i = 1; i < 5; i++) {
                    String sql = "SELECT TIME_FORMAT(start_time, '%h:%i %p') AS start_time," +
                            "TIME_FORMAT(end_time, '%h:%i %p') AS end_time FROM timeslots WHERE timeslot_id=" + i;
                    PreparedStatement statement = connection.prepareStatement(sql);
                    String sql2 = "SELECT * FROM availability WHERE location_id=1 AND timeslot_id=" + i + " AND date='" + dateName +"'";
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
}