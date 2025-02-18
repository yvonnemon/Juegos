package com.example.juegos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        Button mainGame = view.findViewById(R.id.maingame);
        Button lineFour = view.findViewById(R.id.linefour);
        Button hanged = view.findViewById(R.id.hanged);

        //on click, cambia de actividad
        mainGame.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            Intent intent = new Intent(requireActivity(), MainGameActivity.class);
            startActivity(intent);
        });
        lineFour.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            Intent intent = new Intent(requireActivity(), LineFourActivity.class);
            startActivity(intent);
        });
        hanged.setOnClickListener(v -> {
            // Create an intent to navigate to the new activity
            //Intent intent = new Intent(requireActivity(), MainGameActivity.class);
            //startActivity(intent);
            System.out.println("todo");
        });
    }
}
