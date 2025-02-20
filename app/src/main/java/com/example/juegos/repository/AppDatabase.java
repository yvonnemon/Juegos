package com.example.juegos.repository;

import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.juegos.model.Game;
import com.example.juegos.model.User;
import com.example.juegos.model.UserGame;
import com.example.juegos.model.dao.GameDao;
import com.example.juegos.model.dao.UserDao;
import com.example.juegos.model.dao.UserGameDao;

@Database(entities = {User.class, Game.class, UserGame.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract GameDao gameDao();
    public abstract UserGameDao userGameDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "game_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
