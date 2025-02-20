package com.example.juegos.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.juegos.model.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    long inserGame(Game game);

    @Query("SELECT * FROM Game")
    List<Game> getAllSessions();
}
