package com.example.juegos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Game")
public class Game {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
}
