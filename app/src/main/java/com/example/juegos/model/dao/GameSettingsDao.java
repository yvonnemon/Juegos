package com.example.juegos.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.juegos.model.UserSettings;

@Dao
public interface GameSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(UserSettings settings);

    @Query("SELECT * FROM user_settings LIMIT 1")
    UserSettings getSettings();
}
