package com.snappretail.trackingapp.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface data_interface {
    @Insert
    void insert(com.snappretail.trackingapp.Database.Databasesample tasks);
    @Query("DELETE FROM Databasesample")
    void deletealltasks();


    @Query("SELECT * FROM Databasesample")
    List<Databasesample> getalltasks();

    @Query("SELECT * FROM Databasesample WHERE isync = 0")
    List<Databasesample> getallUnSynctasks();

    @Query("DELETE FROM Databasesample WHERE id = :id")
    void deletebyid(int id);
    @Query("UPDATE Databasesample Set isync = :num WHERE id=:id")
    void setsync(int id,int num);


}
