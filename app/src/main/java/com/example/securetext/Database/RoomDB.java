package com.example.securetext.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.securetext.Dao.KeyDao;
import com.example.securetext.Dao.MessageDao;
import com.example.securetext.Model.Key;
import com.example.securetext.Model.Message;

@Database( entities = {Message.class, Key.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;
    private static String DATABASE_NAME = "SecureText";

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MessageDao mainDao();
    public abstract KeyDao keyDao();


}
