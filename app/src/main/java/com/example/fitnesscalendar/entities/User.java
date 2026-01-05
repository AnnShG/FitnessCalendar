package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

@Data
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "birth_date")
    public Date birthDate;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "goal")
    public String goal;

}
