package com.example.juegos.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

@Entity(tableName = "User_Game")
public class UserGame {

    @PrimaryKey(autoGenerate = true)
    public int session_id; // Unique session identifier

    @NonNull
    public int user_id; // Player ID

    @NonNull
    public int game_id; // Game ID (1 = 2048, 2 = Hangman, etc.)

    @NonNull
    public String gameName; // Game Name

    public int score; // Score achieved

    public String timestamp; // Auto-generated timestamp
}

