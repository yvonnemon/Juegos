package com.example.juegos.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.juegos.fragment.LoginFragment;
import com.example.juegos.R;
import com.example.juegos.model.Game;
import com.example.juegos.repository.AppDatabase;

import java.util.List;

//
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, new LoginFragment())  // Load default fragment
                    .commit();
        }

        insertDefaultGames(this);

    }

    public void insertDefaultGames(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        new Thread(() -> {
            // Check if the database already contains these games
            List<Game> existingGames = db.gameDao().getAllSessions();
            if (existingGames.isEmpty()) {  // Only insert if the table is empty
                Game game1 = new Game();
                game1.name = "2048";
                game1.description = "The objective of the game is to slide numbered tiles on a grid to combine them to create a tile with the number 2048; however, one can continue to" +
                        " play the game after reaching the goal, creating tiles with larger numbers.";

                Game game2 = new Game();
                game2.name = "Line Four";
                game2.description = "Is a game in which the players choose a color and then take turns dropping colored tokens into a six-row, seven-column vertically suspended grid. " +
                        "The pieces fall straight down, occupying the lowest available space within the column. The objective of the game is to be the first to form a horizontal, vertical," +
                        " or diagonal line of four of one's own tokens.";

                db.gameDao().inserGame(game1);
                db.gameDao().inserGame(game2);

                Log.d("Database", "Default games inserted.");
            } else {
                Log.d("Database", "Games already exist. No need to insert.");
            }
        }).start();
    }


}