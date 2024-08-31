package com.example.android_developer_assignment.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android_developer_assignment.MyApplication;

@Database(entities = {User.class}, version = 4)
abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
public class AppLocalDb {
    static public AppDatabase getDb(){
        return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppDatabase.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }

}
