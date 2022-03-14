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

        Button b_lyon_center = root.findViewById(R.id.lyon_center);
        b_lyon_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbinding = FragmentReservationBinding.inflate(inflater, container, false);
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}