package com.teamdrt.teamdrtdownloader.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.teamdrt.teamdrtdownloader.App;


@Database ( entities = Download.class,version = 1)
public abstract class AppDatabse extends RoomDatabase {

    private static AppDatabse instance;

    public abstract DownloadsDao downloadsDao();


    public static synchronized AppDatabse getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder (
                    context, AppDatabse.class,"FYI_database" )
                    .fallbackToDestructiveMigration ()
                    .build ();
        }
        return instance;

    }
}
