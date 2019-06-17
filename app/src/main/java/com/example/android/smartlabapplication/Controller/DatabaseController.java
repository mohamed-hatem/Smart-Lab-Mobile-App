package com.example.android.smartlabapplication.Controller;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.smartlabapplication.Util.DatabaseOpenHelper;


/**
 * Created by DELL on 1/31/2019.
 */

public class DatabaseController  {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseController instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseController(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DatabaseAccess
     */
    public static DatabaseController getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseController(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }
    public String findSensor(String stringToBeProcessed)
    {
        Cursor cursor = database.rawQuery("select * from Text_to_Sensor,Sensor where Sensor.id=Text_to_Sensor.tts_id",null);
        String[] list = stringToBeProcessed.split(" ");
        if (cursor.moveToFirst()) {
            do {
                for(int i=0;i<list.length;i++)
                {
                    if(list[i].equals(cursor.getString(1)))
                    {
                        return cursor.getString(4)+","+cursor.getString(5);

                    }
                }
            } while (cursor.moveToNext());
        }


        return "";
    }

    public String findActuator(String stringToBeProcessed) {

        Cursor cursor = database.rawQuery("select * from Text_to_Actuator",null);
        String[] list = stringToBeProcessed.split(" ");
        int device_id=-1;
        boolean found = false;
        if (cursor.moveToFirst()) {
            do {
                for(int i=0;i<list.length;i++)
                {
                    if(list[i].equals(cursor.getString(1)))
                    {
                        device_id = cursor.getInt(2);
                        Log.e("findDevice: ", String.valueOf(device_id)+"   "+ cursor.getString(1) );
                        found= true;
                        break;


                    }
                }
             if(found)
                 break;


            } while (cursor.moveToNext());
        }
        if(device_id==-1)
            return "";
        Cursor cursor2 =
         database.rawQuery("select device_name from Actuator where device_id=?",new String[]{String.valueOf(device_id)});
        if(cursor2.moveToFirst())
        return cursor2.getString(0);
        return"";

    }

    public String findCommand(String stringToBeProcessed){

        Cursor cursor = database.rawQuery("select * from Text_to_Command",null);
        String[] list = stringToBeProcessed.split(" ");
        int command_id=-1;
        boolean found=false;
        if (cursor.moveToFirst()) {
            do {
                for(int i=0;i<list.length;i++)
                {
                    if(list[i].equals(cursor.getString(2)))
                    {
                        command_id = cursor.getInt(1);
                        Log.e("findCommand: ", String.valueOf(command_id)+"   "+ cursor.getString(1) );
                        found= true;
                        break;


                    }
                }
                if(found)
                    break;


            } while (cursor.moveToNext());
        }
        if(command_id==-1)
            return "";
        Cursor cursor2 = database.rawQuery("select command_name from Command where command_id = ? ",new String[]{String.valueOf(command_id)});
        if(cursor2.moveToFirst())
        Log.e("findCommand: ",cursor2.getString(0));
        return cursor2.getString(0);
    }

    public boolean isSuitable(String device,String command)
    {
       int device_id = getDeviceId(device);
       int command_id = getCommandId(command);
       Cursor cursor = database.rawQuery("select * from Authorized_Commands_to_Actuator",null);
        if (cursor.moveToFirst()) {
            do {
               if(cursor.getInt(1)==command_id &&cursor.getInt(2)==device_id)
                   return true;

            } while (cursor.moveToNext());
        }



        return  false;
    }

    private int getDeviceId(String device_name)
    {
        Cursor cursor = database.rawQuery("select device_id from Actuator where device_name=?",new String[]{device_name});
        cursor.moveToFirst();
        return cursor.getInt(0);

    }
    private int getCommandId(String command_name)
    {
        Cursor cursor = database.rawQuery("select command_id from Command where command_name=?",new String[]{command_name});
        cursor.moveToFirst();
        return cursor.getInt(0);

    }

}
