package com.example.juegos.fragment;

import android.content.Intent;
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

import com.example.juegos.R;
import com.example.juegos.activities.LineFourActivity;
import com.example.juegos.activities.MainGameActivity;
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
    }
}
