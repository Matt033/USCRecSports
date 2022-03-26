package edu.usc.uscrecapp.ui;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.Navigation;

import edu.usc.uscrecapp.R;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.databinding.FragmentHomeBinding;
import edu.usc.uscrecapp.databinding.FragmentReservationBinding;

import edu.usc.uscrecapp.databinding.FragmentNotificationsBinding;

public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("Created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = (EditText) findViewById(R.id.username);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final Button loginButton = (Button) findViewById(R.id.login);
        usernameEditText.setText("Hello");
        passwordEditText.setText("World");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked!");
                usernameEditText.setText("Clicked!");
                passwordEditText.setText("World");
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Connection connection = null;
                try {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/uscrecsports?user=root&password=Barkley2001$");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        System.out.println("Valid Creds!");
                        int userID = rs.getInt("user_id");
                        Bundle result = new Bundle();
                        result.putInt("user_id", userID);
                        usernameEditText.setText("");
                        passwordEditText.setText("");

                      getSupportFragmentManager().setFragmentResult("requestKey", result);
//                      // now switch to reservation fragment page
                      Navigation.findNavController(v).navigate(R.id.navigation_home);
                    } else {
                        System.out.println("Invalid Creds!");
                        TextView response = (TextView) findViewById(R.id.Feedback);
                        response.setText("Invalid Login. Please enter in a new login");
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }

        });

    }
}
