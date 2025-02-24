package com.example.juegos.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.room.Room;

import com.example.juegos.R;
import com.example.juegos.fragment.RegisterFragment;
import com.example.juegos.model.UserSettings;
import com.example.juegos.repository.AppDatabase;

public class SettingsActivity extends AppCompatActivity {

    private AppDatabase db;
    SwitchCompat tiles;
    SwitchCompat soloPlayer;
    SwitchCompat colorBlind;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.settings_activity);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_db").allowMainThreadQueries().build();
        UserSettings settings = db.gameSettingsDao().getSettings();

        tiles = findViewById(R.id.tilesOption);
        soloPlayer = findViewById(R.id.soloQ);
        colorBlind = findViewById(R.id.colorBlind);

        tiles.setChecked(settings.originalTiles);
        soloPlayer.setChecked(settings.soloPlayer);
        colorBlind.setChecked(settings.colorBlind);


        Button save = findViewById(R.id.saveSettings);
        save.setOnClickListener(v -> {
            saveGameSettings(this);
            Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
        });

    }

    public void saveGameSettings(Context context) {
        new Thread(() -> {
            UserSettings settings = new UserSettings();

            settings.originalTiles = tiles.isChecked();
            settings.soloPlayer = soloPlayer.isChecked();
            settings.colorBlind = colorBlind.isChecked();

           // settings.textColor = textColor;


            AppDatabase.getInstance(context).gameSettingsDao().insertSettings(settings);

            Log.d("DB", "gameName" + " Settings Saved!");
        }).start();
    }
}
