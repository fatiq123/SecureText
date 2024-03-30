package com.example.securetext.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "keydb")
public class Key implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "key")
    String key;

    @ColumnInfo(name = "messagebackup")
    Boolean messagebackup;

    @ColumnInfo(name = "security")
    Boolean security;

    public Key(String key) {
        this.key = key;
    }

    public Key() {

    }



    public int getID() {
        return ID;
    }
    public String getKey() {
        return key;
    }
    public Boolean getMessagebackup() {
        return messagebackup;
    }
    public Boolean getSecurity() {
        return security;
    }


    public void setID(int ID) {
        this.ID = ID;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setMessagebackup(Boolean messagebackup) {
        this.messagebackup = messagebackup;
    }
    public void setSecurity(Boolean security) {
        this.security = security;
    }


}
