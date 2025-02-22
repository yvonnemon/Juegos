package com.example.juegos.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.juegos.model.UserGame;

import java.util.List;

@Dao
public interface UserGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGame(UserGame userGame);

    @Query("SELECT MAX(score) FROM User_Game WHERE user_id = :userId")
    int getUserHighestScore(int userId);

    @Query("SELECT * FROM User_Game WHERE user_id = :playerId")
    List<UserGame> getGamesForPlayer(int playerId);

    @Query("SELECT MAX(score) FROM User_Game")
    int getHighestOverallScore();

}
