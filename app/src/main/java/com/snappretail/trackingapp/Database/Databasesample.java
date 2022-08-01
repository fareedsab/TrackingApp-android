package com.snappretail.trackingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"time"},
        unique = true)})
public class Databasesample {
    @PrimaryKey(autoGenerate = true)
    int id;

//    public Databasesample( String task_time) {
//        this.id = id;
//        this.task_time = task_time;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @ColumnInfo(name = "time")
    String task_time;
    @ColumnInfo(name = "latitude")
    String latitude;
    @ColumnInfo(name = "longitude")
    String longitude;

    public Databasesample( String task_time, String latitude, String longitude, Integer issync) {
        this.task_time = task_time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.issync = issync;
    }

    public Integer getIssync() {
        return issync;
    }

    public void setIssync(Integer issync) {
        this.issync = issync;
    }

    @ColumnInfo(name = "isync")
    Integer issync;


}
