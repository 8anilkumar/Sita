package com.eminence.sitasrm.Utils;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.eminence.sitasrm.Interface.CartInterface;
import com.eminence.sitasrm.Models.CartResponse;

@Database(entities = CartResponse.class,exportSchema = false ,version = 1)


public abstract class DatabaseHandler extends RoomDatabase {

    private static final String DBNAME="cart";
    private static DatabaseHandler instanse;

/*

public static synchronized DatabaseHandler getInstance(Context context)
{

    if (instanse==null)
    {
        instanse= Room.databaseBuilder(context.getApplicationContext(),DatabaseHandler.class,DBNAME)
                .fallbackToDestructiveMigration()
                .build();

    }
    return instanse;

}
*/

    public  abstract CartInterface cartInterface();

}