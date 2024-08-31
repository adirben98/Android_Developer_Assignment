package com.example.android_developer_assignment.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> findById(int id);

    @Query("SELECT COUNT(*) FROM user WHERE id = :id")
    int checkIfIdExists(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User ...user);

    @Delete
    void delete(User user);
}
