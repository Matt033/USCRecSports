
        package edu.usc.uscrecapp.ui.home;

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

        import edu.usc.uscrecapp.MainActivity;
        import edu.usc.uscrecapp.R;
        import edu.usc.uscrecapp.databinding.FragmentHomeBinding;
        import edu.usc.uscrecapp.databinding.FragmentReservationBinding;
        import edu.usc.uscrecapp.ui.reservation.ReservationFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FragmentReservationBinding rbinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button b_lyon_center = root.findViewById(R.id.lyonCenter);
        b_lyon_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button
                int location_id = 1;
                int user_id = 3;
                ((MainActivity) getActivity()).setLocationId(1);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
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
                int location_id =2;
                int user_id = 3;
                ((MainActivity) getActivity()).setUserId(3);
                ((MainActivity) getActivity()).setLocationId(2);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });

        Button b_cromwell = root.findViewById(R.id.Cromwell);
        b_cromwell
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button
                int location_id = 2;
                int user_id = 3;
                ((MainActivity) getActivity()).setUserId(3);
                ((MainActivity) getActivity()).setLocationId(1);
                // pass the location id to the reservation fragment
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });

        Button b_summary = root.findViewById(R.id.Summary);
        b_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: get location_id of the clicked button

                int user_id = 3;
                ((MainActivity) getActivity()).setUserId(3);

                // pass the location id to the reservation fragment
                Bundle result = new Bundle();

                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                // now switch to reservation fragment page
                Navigation.findNavController(view).navigate(R.id.navigation_notifications);
            }
        });
        // temporary hack to set the default user id and location id
        // The following two lines should be removed.
        ((MainActivity) getActivity()).setUserId(3);
        ((MainActivity) getActivity()).setLocationId(2);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
