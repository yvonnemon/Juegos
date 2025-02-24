package com.example.juegos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity(tableName = "user_settings")
public class UserSettings {
    @PrimaryKey
    public int id = 1; // Only one settings entry

    @ColumnInfo(name = "tile_colors")
    public boolean originalTiles  = false; // Stores tile value-color pairs
    @ColumnInfo(name = "colorblind_mode")
    public boolean colorBlind  = false; // Stores tile value-color pairs
    @ColumnInfo(name = "single_player")
    public boolean soloPlayer  = false;  // Stores tile value-color pairs


    @ColumnInfo(name = "text_color")
    public String textColor;

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", originalTiles=" + originalTiles +
                ", colorBlind=" + colorBlind +
                ", soloPlayer=" + soloPlayer +
                ", textColor='" + textColor + '\'' +
                '}';
    }

//    @ColumnInfo(name = "sound_enabled")
//    public boolean soundEnabled;
}

