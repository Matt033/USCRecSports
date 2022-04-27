package edu.usc.uscrecapp.ui.userprofile;

import androidx.lifecycle.ViewModelProvider;

import android.media.AsyncPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import edu.usc.uscrecapp.MainActivity;
import edu.usc.uscrecapp.R;
import edu.usc.uscrecapp.databinding.FragmentNotificationsBinding;
import edu.usc.uscrecapp.databinding.UserProfileFragmentBinding;
import edu.usc.uscrecapp.ui.notifications.NotificationsViewModel;

public class UserProfileFragment extends Fragment {
    private UserProfileFragmentBinding binding;
    private UserProfileViewModel mViewModel;
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/uscrecsports";
    private static String USER;
    private static String PASSWORD;
    TextView username;
    TextView email;
    EditText newPassword;
    String passwordString;
    TextView response;



    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        UserProfileViewModel userProfileViewModel =
                new ViewModelProvider(this).get(UserProfileViewModel.class);

        binding = UserProfileFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        super.onCreate(savedInstanceState);
        USER = getString(R.string.db_username);
        PASSWORD = getString(R.string.db_password);

        username = (TextView) root.findViewById(R.id.usernamePlace);
        email = (TextView) root.findViewById(R.id.emailPlace);
        response = (TextView) root.findViewById(R.id.Feedback);



        Button updatePassword = (Button) root.findViewById(R.id.passwordButton);
        updatePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newPassword = (EditText) root.findViewById(R.id.passwordPlace);
                passwordString = newPassword.getText().toString();
                if(!passwordString.isEmpty()){
                    new PasswordAsync().execute();
                    response.setText("Password updated successfully");
                }
                else{
                    response.setText("Change password field must not be empty");
                }
            }
        });


        new InfoAsyncTask().execute();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    public class InfoAsyncTask extends AsyncTask<Void, Void, Vector<String>>{
        @Override
        protected Vector<String> doInBackground(Void...voids){
            Vector<String> userInfo = new Vector<>();
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                int user_id = ((MainActivity) getActivity()).getUserId();
                String sql = "SELECT * FROM users WHERE user_id=" + user_id;
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    String name = resultSet.getString("name");
                    userInfo.add(name);
                    String email = resultSet.getString("email");
                    userInfo.add(email);
                    Blob photo = resultSet.getBlob("photo");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            return userInfo;
        }

        @Override
        protected void onPostExecute(Vector<String> result){

            username.setText(result.get(0));
            email.setText(result.get(1));
        }
    }

    public class PasswordAsync extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void...voids){
            try {
                int user_id = ((MainActivity) getActivity()).getUserId();
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(passwordString.getBytes());
                byte[] digest = md.digest();
                BigInteger no = new BigInteger(1, digest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }

                String updateStatement = "UPDATE users SET password=? WHERE user_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,hashtext);
                preparedStatement.setInt(2,user_id);

                preparedStatement.executeUpdate();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
    }

}