package edu.usc.uscrecapp.ui.reservation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Locale;
import java.sql.*;

import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.databinding.FragmentReservationBinding;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;

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
        int day = c.get(Calendar.DATE);
        Button b1 = root.findViewById(R.id.button1);
        Button b2 = root.findViewById(R.id.button2);
        Button b3 = root.findViewById(R.id.button3);
        b1.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        day++;
        dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b2.setText(day + "\n" + dayName);
        c.add(Calendar.DATE, 1);
        day++;
        dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        b3.setText(day + "\n" + dayName);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/uscrecapp?user=root&password=root");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM availability");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // extract each column
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}