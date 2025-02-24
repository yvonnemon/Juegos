package com.example.juegos.repository;

import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.example.juegos.model.Game;
import com.example.juegos.model.User;
import com.example.juegos.model.UserGame;
import com.example.juegos.model.UserSettings;
import com.example.juegos.model.dao.GameDao;
import com.example.juegos.model.dao.GameSettingsDao;
import com.example.juegos.model.dao.UserDao;
import com.example.juegos.model.dao.UserGameDao;

@Database(entities = {User.class, Game.class, UserGame.class, UserSettings.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract GameDao gameDao();

    public abstract UserGameDao userGameDao();

    public abstract GameSettingsDao gameSettingsDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "game_db")
                            .fallbackToDestructiveMigration() // Allows database reset on schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
