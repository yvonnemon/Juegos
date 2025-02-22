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

        addTableRow("2048", "PlayerOne", "4096");
        addTableRow("Ahorcado", "PlayerTwo", "2048");
        addTableRow("4 en raya", "PlayerThree", "1024");
        loadInfo();
    }
    private void loadInfo() {

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_db").allowMainThreadQueries().build();
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int id = sharedPreferences.getInt("user_id", -1); // Default to -1 if no user is found
        String nombre = sharedPreferences.getString("username","undefined");
        System.out.println("sout?");
        List<UserGame> users = db.userGameDao().getGamesForPlayer(id);
        for (UserGame user : users) {
            Log.d("Database", "User: " + user.score + ", Score: " + user.timestamp + "asd " + user.game_id);
            boolean raya4 = user.gameName.equals("Line Four");
            if(raya4) {
                addTableRow(user.gameName, nombre, user.score == 1 ? "Win" : "Lose");
            } else {
                addTableRow(user.gameName, nombre, String.valueOf(user.score));
            }

        }
        Log.d("Database", "User inserted with ID: " + id);
    }

    private void addTableRow(String rank, String player, String score) {
        LinearLayout tableLayout = findViewById(R.id.tableRowsContainer);

        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 8, 8, 8);
        row.setBackgroundColor(getResources().getColor(R.color.verdeclaro));

        TextView rankView = createTextView(rank, 1);
        TextView playerView = createTextView(player, 2);
        TextView scoreView = createTextView(score, 1);

        row.addView(rankView);
        row.addView(playerView);
        row.addView(scoreView);

        tableLayout.addView(row);

        // Add divider
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(getResources().getColor(R.color.evoker));
        tableLayout.addView(divider);

    }

    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight));
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}
