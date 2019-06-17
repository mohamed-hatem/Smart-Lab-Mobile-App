package com.example.android.smartlabapplication;

import com.example.android.smartlabapplication.Security.SecurityModule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by DELL on 6/4/2019.
 */

public class SecurityModuleTest {
 private  SecurityModule securityModule;
    @Before
   public void setUp()
    {
        securityModule= new SecurityModule();
    }
    @Test
    public void correctEncryption()
    {
        Assert.assertEquals("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4",securityModule.encrypt("1234"));
    }
    @Test
    public void wrongEncryption()
    {
        Assert.assertNotSame("wrong",securityModule.encrypt("text123"));
    }
    @Test
    public void isValid()
    {

        Assert.assertEquals(true,securityModule.validateString("hello"));
    }
    @Test
    public void isInvalid()
    {
        SecurityModule securityModule= new SecurityModule();
        Assert.assertEquals(false,securityModule.validateString("selEct  * from Sensor"));
    }
}
