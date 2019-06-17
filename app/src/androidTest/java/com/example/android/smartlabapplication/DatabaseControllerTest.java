
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.smartlabapplication.Controller.DatabaseController;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by DELL on 6/9/2019.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseControllerTest {
    Context appContext;
    DatabaseController databaseController;
    @Before 
    public  void setup() {
     appContext = InstrumentationRegistry.getTargetContext();
     databaseController = DatabaseController.getInstance(appContext);
     databaseController.open();
    }
    
    @Test
    public void validSensor()
    {
        Assert.assertEquals("Sensor001",databaseController.findSensor("temperature"));
    }
    @Test
    public void invalidSensor()
    {
        Assert.assertNotSame("Sensor001",databaseController.findSensor("Light"));
    }
    @Test
    public void validCommand()
    {
        Assert.assertEquals("Open",databaseController.findSensor("open device"));
    }
    @Test
    public void invalidCommand()
    {
        Assert.assertNotSame("Open",databaseController.findSensor("Hello"));
    }
    @Test
    public void validActuator()
    {
        Assert.assertEquals("Light001",databaseController.findActuator("open the light"));
    }
    @Test
    public void invalidActuator()
    {
        Assert.assertNotSame("Light",databaseController.findActuator("Open the Temperature"));
    }


    @After
    public void end()
    {
        databaseController.close();
    }
}
