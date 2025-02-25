package com.example.juegos.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.juegos.R;
import com.example.juegos.activities.LineFourActivity;
import com.example.juegos.activities.MainGameActivity;
import com.example.juegos.activities.SettingsActivity;
import com.example.juegos.activities.UserInfoActivity;

//Menu para elegir el juego
public class GameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_fragment, container, false);




        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            logoutUser();
        });

        CardView maingame = view.findViewById(R.id.maingame);

        maingame.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainGameActivity.class);
            startActivity(intent);
        });

        CardView linefour = view.findViewById(R.id.linefour);
        linefour.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LineFourActivity.class);
            startActivity(intent);
        });

        //TODO
        ImageButton userInfo = view.findViewById(R.id.userInfo);

        userInfo.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            Intent intent = new Intent(requireActivity(), UserInfoActivity.class);
            startActivity(intent);
            System.out.println("todo");
        });

        ImageButton settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(intent);
            System.out.println("todo");
        });
    }

    private void logoutUser() {
        // Clear SharedPreferences (if storing user info)
        SharedPreferences preferences = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Remove saved user data
        editor.apply();


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.login_fragment, new LoginFragment()) // Ensure you're replacing the correct fragment container
                .addToBackStack(null) // Allows going back if needed
                .commit();
    }

}
