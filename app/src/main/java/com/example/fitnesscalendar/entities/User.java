package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "birth_date")
    public Date birthDate;

    @ColumnInfo(name = "gender")
    public String gender;

    public Date createdAt = new Date();
}
