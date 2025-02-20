package com.example.juegos.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "User", indices = {@Index(value = "userName", unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userName;
    public String password;
    public String name;
}
