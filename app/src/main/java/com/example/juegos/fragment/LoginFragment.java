package com.example.juegos.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.juegos.R;
import com.example.juegos.activities.MainActivity;
import com.example.juegos.model.Game;
import com.example.juegos.model.User;
import com.example.juegos.repository.AppDatabase;
import com.example.juegos.session.SessionManager;
import com.example.juegos.session.UserSession;

import java.util.List;

public class LoginFragment extends Fragment {
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = rootView.findViewById(R.id.loginButton);
        Button registerButton = rootView.findViewById(R.id.registerButton);

        EditText username = rootView.findViewById(R.id.usernameInput);
        EditText password = rootView.findViewById(R.id.passwordInput);
        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        login.setOnClickListener(v -> {
            //TODO login
            attemptLogin(username.getText().toString(), password.getText().toString());

            //logAllUsers();
            getgames();
        });
        registerButton.setOnClickListener(v -> {
            //TODO login
            // Use the activity's FragmentManager to replace the fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return rootView;
    }

    private void attemptLogin(String usuario, String pass) {

        new Thread(() -> {
            User user = database.userDao().getUserByUsername(usuario); // Assuming email is used as userId

            if (user == null) {
                // Register new user in Room Database
//                user = new User();
//                user.userId = email;
//                user.username = username;
//                database.userDao().insertUser(user);
                System.out.println("no existe");

            } else {
                // Save session & store user globally
//                sessionManager.saveUserSession(String.valueOf(user.id));
//                UserSession.getInstance().setUser(user);
                //TODO
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", user.id); // Save user ID
                editor.putString("username", user.userName); // Save username
                editor.apply();

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Welcome " + user.userName, Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.login_fragment, new GameFragment())
                            .addToBackStack(null)
                            .commit();
                });

            }

        }).start();
    }

    private void logAllUsers() {
        new Thread(() -> {
            List<User> users = database.userDao().getAllPlayers();
            for (User user : users) {
                Log.d("Database", "User ID: " + user.id + ", Username: " + user.userName);
            }
        }).start();
    }
    private void getgames() {
        new Thread(() -> {
            List<Game> users = database.gameDao().getAllSessions();
            for (Game user : users) {
                Log.d("Database", "ID: " + user.id + ", name: " + user.name);
            }
        }).start();
    }
    //TODO implementar en todas las pantallas y redirigir al login
    private void logout() {
        new Thread(() -> {
            database.userDao().clearUsers(); // Clear user data in Room DB
            sessionManager.clearSession(); // Clear SharedPreferences

            /*runOnUiThread(() -> {
                Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });*/
        }).start();
    }
}
