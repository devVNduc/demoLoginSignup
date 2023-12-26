package com.example.demologinsignup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);
    @Query("SELECT * from users where username=(:username) and password=(:password)")
    User login(String username,String password);
}
