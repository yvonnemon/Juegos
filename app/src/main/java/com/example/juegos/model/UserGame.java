package com.example.juegos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "User_Game", primaryKeys = {"user_id", "game_id"})
public class UserGame {
    @NonNull
    public int user_id;
    @NonNull
    public int game_id;

    public int score;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public String timestamp; // Auto-generated timestamp
}
