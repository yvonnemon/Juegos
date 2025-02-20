package com.example.juegos.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.juegos.model.UserGame;

import java.util.List;

@Dao
public interface UserGameDao {
    @Insert
    void insertUserGame(UserGame userGame);

    @Query("SELECT * FROM User_Game WHERE user_id = :playerId")
    List<UserGame> getGamesForPlayer(int playerId);
}
