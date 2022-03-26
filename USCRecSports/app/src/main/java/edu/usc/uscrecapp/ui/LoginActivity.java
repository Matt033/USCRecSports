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

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;

import edu.usc.uscrecapp.databinding.FragmentNotificationsBinding;

import androidx.navigation.Navigation;

public class LoginActivity extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static final String USER = "root";
    private static final String PASSWORD = "root";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("Created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.login);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked!");
                usernameEditText.setText("Clicked!");
                passwordEditText.setText("World");
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                try {

                    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

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
                       // ((MainActivity) getActivity()).setUserId(userID);
                     getSupportFragmentManager().setFragmentResult("", result);
                        Navigation.findNavController(v).navigate(R.id.navigation_reservation);
                    } else {
                        System.out.println("Invalid Creds!");
                        TextView response = (TextView) findViewById(R.id.Feedback);
                        response.setText("Invalid Login. Please enter in a new login");
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }

            }

        });

    }
}
