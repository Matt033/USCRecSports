package edu.usc.uscrecapp.ui;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.ui.home.HomeFragment;
//import edu.usc.uscrecapp.databinding.FragmentHomeBinding;
//import edu.usc.uscrecapp.databinding.FragmentReservationBinding;
//
//import edu.usc.uscrecapp.databinding.FragmentNotificationsBinding;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    TextView response;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        response = (TextView) findViewById(R.id.Feedback);
        final Button loginButton = (Button) findViewById(R.id.login);
        final Button registerButton = (Button) findViewById(R.id.register);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(password.getBytes());
                byte[] digest = md.digest();
                BigInteger no = new BigInteger(1, digest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                System.out.println(hashtext);


                new LoginAsyncTask(username, hashtext).execute();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response.setText("Invalid Login. Please enter in a new login");
            }

        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(password.getBytes());
                byte[] digest = md.digest();
                BigInteger no = new BigInteger(1, digest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                System.out.println(hashtext);


                new RegisterAsyncTask(username, hashtext).execute();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //response.setText("Invalid Login. Please enter in a new login");
            }

        });

    }

    public class LoginAsyncTask extends AsyncTask {
        private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
        private static final String USER = "root";
        private static final String PASSWORD = "Barkley2001$";
        String username;
        String password;

        public LoginAsyncTask(String user, String pass) {
            username = user;
            password = pass;
        }

        @Override
        protected Void doInBackground(Object[] objects) {
            System.out.println("Background");
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Valid Creds!");
                    int userID = rs.getInt("user_id");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    System.out.println("Invalid Creds!");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    //response.setText("Invalid Login. Please enter in a new login");
                    View root = findViewById(R.id.container);
                    Snackbar mySnackbar = Snackbar.make(root, "Invalid Login. Please enter in a new login", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }

    public class RegisterAsyncTask extends AsyncTask {
        private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
        private static final String USER = "root";
        private static final String PASSWORD = "Barkley2001$";
        String username;
        String password;

        public RegisterAsyncTask(String user, String pass) {
            username = user;
            password = pass;
        }

        @Override
        protected Void doInBackground(Object[] objects) {
            System.out.println("Background");
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Existing Creds!");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }

                String sql2 = "INSERT INTO users (username, password) VALUES ('"+username+"', '"+password+"')";
                PreparedStatement ps2 = connection.prepareStatement(sql);
                ResultSet rs2 = ps.executeQuery();

                ResultSet rs3 = ps.executeQuery();
                if (rs.next()) {
                    int userID = rs.getInt("user_id");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }

}
