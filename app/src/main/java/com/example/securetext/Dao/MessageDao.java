package com.example.securetext.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.securetext.Model.Message;
import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = REPLACE)
    void saveItem(Message message);

    @Query("select * from message order by creationtime desc")
    List<Message> getAllMessages();

    @Delete
    void delete(Message message);

    @Query("delete from message")
    void deleteAllMessages();

}
