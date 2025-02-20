package com.example.juegos.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "User_Game", primaryKeys = {"user_id", "game_id"})
public class UserGame {
    @NonNull
    public int user_id;
    @NonNull
    public int game_id;
}
