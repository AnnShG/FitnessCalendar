package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Quote;

@Dao
public interface QuoteDao {
    @Insert
    void insert(Quote quote);

    @Update
    void update(Quote quote);

    @Delete
    void delete(Quote quote);

//    @Query("SELECT * FROM quotes WHERE id = :quoteId")
//    Quote getQuoteById(int quoteId);


}
