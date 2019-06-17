package com.example.android.smartlabapplication.Util;

import android.content.Context;
import android.util.Log;

import com.example.android.smartlabapplication.Controller.DatabaseController;

import java.util.ArrayList;

/**
 * Created by DELL on 2/1/2019.
 */

public class TextAnalyzer {
    private DatabaseController databaseController ;

    public TextAnalyzer(Context context)
    {
        databaseController = DatabaseController.getInstance(context);
    }
    public String processStringForSensor(String stringToBeProcesses)
    {
        databaseController.open();
        return databaseController.findSensor(stringToBeProcesses);

    }
    //Returns an arraylist containing device & command if they are valid
    public ArrayList<String> processStringForActuatorAndCommand(String stringToBeProcessed)
    {
        databaseController.open();
      final String device = processStringForActuator(stringToBeProcessed);
        if(device.equals(""))
            return  null;
        Log.e( "ProcessString: ",device );
        final String command = processStringForCommand(stringToBeProcessed);
        Log.e( "ProcessString: ",command);
        if(command.equals(""))
            return null;

        //Check if the device can use this command
        if(!isSuitable(device,command))
          return null;

        return new ArrayList<String>() {
            {
                add(command);
                add(device);
            }


        };
    }

    public boolean isSuitable(String device, String command) {
        return databaseController.isSuitable(device,command);
    }

    public String processStringForActuator(String stringToBeProcessed) {
        return databaseController.findActuator(stringToBeProcessed);
    }

    public String processStringForCommand(String stringToBeProcessed) {
      return databaseController.findCommand(stringToBeProcessed);
    }

    //Close the db connection
     public void finish()
{
    databaseController.close();
}

}
