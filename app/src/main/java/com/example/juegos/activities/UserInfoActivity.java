package com.example.juegos.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.juegos.R;
import com.example.juegos.fragment.RegisterFragment;
import com.example.juegos.model.User;
import com.example.juegos.model.UserGame;
import com.example.juegos.repository.AppDatabase;

import java.util.List;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.user_info_activity);

//        addTableRow("2048", "PlayerOne", "4096");
//        addTableRow("4 en raya", "PlayerThree", "1024");

        loadInfo(0);
        TextView dateSort = findViewById(R.id.dateSort);
        dateSort.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.9f) // Shrink horizontally
                    .scaleY(0.9f) // Shrink vertically
                    .setDuration(100) // Animation duration (fast)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100)) // Reset to normal size
                    .start();
            loadInfo(2);
        });
        TextView gameSort = findViewById(R.id.gameSort);
        gameSort.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.9f) // Shrink horizontally
                    .scaleY(0.9f) // Shrink vertically
                    .setDuration(100) // Animation duration (fast)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100)) // Reset to normal size
                    .start();
            loadInfo(1);
        });
    }

    private void loadInfo(int action) {
        resetTable();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_db").allowMainThreadQueries().build();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int id = sharedPreferences.getInt("user_id", -1); // Default to -1 if no user is found
        String nombre = sharedPreferences.getString("username","undefined");
        System.out.println("sout?");
        List<UserGame> users;

        if(action == 1) {
            users = db.userGameDao().getFromUserOrderByGamenameDesc(id);
            System.out.println("game");
        } else if (action == 2){
            users = db.userGameDao().getFromUserOrderByDateDesc(id);
            System.out.println("date");
        } else { //default sin filtro
            users = db.userGameDao().getGamesForPlayer(id);
            System.out.println("default");
        }

        for (UserGame user : users) {
            Log.d("Database", "User: " + user.score + ", time: " + user.timestamp + "asd " + user.game_id);
            boolean raya4 = user.gameName.equals("Line Four");
            if(raya4) {
                addTableRow(user.gameName, nombre, user.score == 1 ? "Win" : "Lose",user.timestamp);
            } else {
                addTableRow(user.gameName, nombre, String.valueOf(user.score), user.timestamp);
            }

        }
        Log.d("Database", "User inserted with ID: " + id);
    }

    private void addTableRow(String rank, String player, String score, String date) {
        LinearLayout tableLayout = findViewById(R.id.tableRowsContainer);

        // Create row container
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 8, 8, 8);
        row.setBackgroundColor(getResources().getColor(R.color.verdeclaro));

        // Create columns
        TextView rankView = createTextView(rank, 1);
        TextView playerView = createTextView(player, 1);
        TextView scoreView = createTextView(score, 1);
        TextView dateView = createTextView(date, 1);

        // Add views to row
        row.addView(rankView);
        row.addView(playerView);
        row.addView(scoreView);
        row.addView(dateView);

        // Add row to table
        tableLayout.addView(row);

        // Add divider
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(getResources().getColor(R.color.evoker));
        tableLayout.addView(divider);
    }


    // Helper function for creating TextViews
    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight));
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void resetTable() {
        LinearLayout tableLayout = findViewById(R.id.tableRowsContainer);
        tableLayout.removeAllViews(); // Clears previous rows
    }

}
