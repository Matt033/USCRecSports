package com.example.uscrecsports;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /*Button lyon_center = findViewById(R.id.lyonCenter);
        lyon_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int location_id = 1;
                int user_id =1;//get_cookie
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("");
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);

            }
        });

        Button Cromwell = findViewById(R.id.Cromwell);
        Cromwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int location_id = 2;
                int user_id =1;//get_cookie
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("");
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);

            }
        });

        Button village = findViewById(R.id.Village);
        village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int location_id = 3;
                int user_id =1;//get_cookie
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("");
                Navigation.findNavController(view).navigate(R.id.navigation_reservation);

            }
        });

        Button summary = findViewById(R.id.Summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_id =1;//get_cookie
                Bundle result = new Bundle();
                result.putInt("location_id", location_id);
                result.putInt("user_id", user_id);
                getParentFragmentManager().setFragmentResult("");
                Navigation.findNavController(view).navigate(R.id.notifications_reservation); //to be changed

            }
        });*/
        
    }
}