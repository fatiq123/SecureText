package com.example.securetext.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "message")
public class Message implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "ptext")
    String ptext;

    @ColumnInfo(name = "etext")
    String etext;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "creationtime")
    Date creationTime;

    public Message() {

    }

    public Message(String ptext, String etext, Date creationTime) {
        this.ptext = ptext;
        this.etext = etext;
        this.creationTime = creationTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    public void setPtext(String ptext) {
        this.ptext = ptext;
    }
    public String getPtext() {
        return ptext;
    }

    public void setEtext(String etext) {
        this.etext = etext;
    }
    public String getEtext() {
        return etext;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    public Date getCreationTime() {
        return creationTime;
    }
}
