package com.example.fitnesscalendar.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

@Data
@Entity(tableName = "ai_messages")
public class AiMessage {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String role; // "user" or "model"
    public String content;
    public Date timestamp;

    public AiMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = new Date();
    }
}
