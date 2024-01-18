package com.example.demologinsignup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);
    @Query("SELECT * from users where username=(:username) and password=(:password)")
    User login(String username,String password);
    @Query("SELECT COUNT(*) FROM users WHERE username=(:username)")
    int checkUsernameExists(String username);
    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone")
    int checkPhoneExists(String phone);
    @Query("SELECT * FROM users WHERE username = :username AND phone = :phone")
    User getUserByUsernameAndPhoneNumber(String username, String phone);
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Update
    void updateUser(User user);

}
