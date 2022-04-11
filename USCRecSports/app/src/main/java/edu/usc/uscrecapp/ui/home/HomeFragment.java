
        package edu.usc.uscrecapp.ui.home;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.navigation.Navigation;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.Map;

        import edu.usc.uscrecapp.MainActivity;
        import edu.usc.uscrecapp.R;
        import edu.usc.uscrecapp.databinding.FragmentHomeBinding;
        import edu.usc.uscrecapp.databinding.FragmentReservationBinding;
        import edu.usc.uscrecapp.ui.reservation.ReservationFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FragmentReservationBinding rbinding;
    public TextView b_summary;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        int userID = ((MainActivity) getActivity()).getUserId();

        b_summary = root.findViewById(R.id.Summary);
        new HomeFragment.UpdateAsyncTask(0, "null").execute();


        Button b_lyon_center = root.findViewById(R.id.lyonCenter);
        b_lyon_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button
                int location_id = 1;
                ((MainActivity) getActivity()).setLocationId(location_id);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", userID);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });

        Button b_village = root.findViewById(R.id.Village);
        b_village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button
                int location_id = 3;
                ((MainActivity) getActivity()).setLocationId(location_id);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", userID);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });

        Button b_cromwell = root.findViewById(R.id.Cromwell);
        b_cromwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button
                int location_id = 2;
                ((MainActivity) getActivity()).setLocationId(location_id);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", userID);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });
        b_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigation_notifications);
            }
        });


        // temporary hack to set the default user id and location id
        // The following two lines should be removed.
        ((MainActivity) getActivity()).setLocationId(1);
        ((MainActivity) getActivity()).setUserId(userID);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public class UpdateAsyncTask extends AsyncTask{
        int userID;
        private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
        private static final String USER = "root";
        private static final String PASSWORD = "Barkley2001$";

        public UpdateAsyncTask(int id, String dt) {
            userID=((MainActivity) getActivity()).getUserId();
        }

        @Override
        protected Void doInBackground(Object[] objects) {
            System.out.println("Background");
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM reservations WHERE user_id=" + userID + " AND DATE(reservations.date) >= DATE(NOW());";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                int rowCount =0;
                if(resultSet.last()) {
                    rowCount = resultSet.getRow();
                    resultSet.beforeFirst();
                }
                System.out.println("RC:"+rowCount);
                String resDates = "";
                int count = 0;
                while(resultSet.next() && count < 3){
                    resDates += resultSet.getDate("date");
                    resDates += ",\n";
                    count++;
                }
                if(count == 3){
                    resDates += " etc";
                }
                System.out.println(resDates);

                if(rowCount > 0){
                    b_summary.setText("View your "+rowCount+" upcoming reservations on \n"+resDates);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }


}


