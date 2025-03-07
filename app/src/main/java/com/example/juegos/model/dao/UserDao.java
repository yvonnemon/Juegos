package com.example.juegos.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.juegos.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM User")
    List<User> getAllPlayers();

    @Query("SELECT * FROM User WHERE userName = :userId LIMIT 1")
    User getUserByUsername(String userId);

    @Query("DELETE FROM User")
    void clearUsers();
}
