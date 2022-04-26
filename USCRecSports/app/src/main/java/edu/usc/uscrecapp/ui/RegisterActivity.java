package edu.usc.uscrecapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    TextView response;
    EditText emailEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("Register activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        passwordConfirmEditText = (EditText) findViewById(R.id.passwordConfirm);
        response = (TextView) findViewById(R.id.Feedback);
        emailEditText = (EditText) findViewById(R.id.email);
        final Button loginButton = (Button) findViewById(R.id.login);
        final Button registerButton = (Button) findViewById(R.id.register);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();
                String emailGiven = emailEditText.getText().toString();

                if(!password.equals(passwordConfirm)){
                    response.setText("Invalid Registration. Please make sure your password entries match");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    passwordConfirmEditText.setText("");
                    emailEditText.setText("");
                    return;
                }

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


                new RegisterActivity.RegisterAsyncTask(username, hashtext, emailGiven).execute();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response.setText("Invalid Registration, this account already exists. Please login.");
            }

        });

    }

    public class RegisterAsyncTask extends AsyncTask {
        private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
        private String USER;
        private String PASSWORD;
        String username;
        String password;
        String email;

        public RegisterAsyncTask(String user, String pass, String emailParam) {
            username = user;
            password = pass;
            email = emailParam;
            USER = getString(R.string.db_username);
            PASSWORD = getString(R.string.db_password);
        }

        @Override
        protected Void doInBackground(Object[] objects) {
            System.out.println("Background");
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                String sql = "SELECT * FROM users WHERE username = '" + username + "'";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next() || username == "" || password == "") {
                    System.out.println("Existing Creds!");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    return null;
                }

                String sql2 = "INSERT INTO users (username, password, email) VALUES ('"+username+"', '"+password+"', '"+email+"')";
                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ps2.executeUpdate();

                ResultSet rs3 = ps.executeQuery();
                if (rs3.next()) {
                    int userID = rs3.getInt("user_id");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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