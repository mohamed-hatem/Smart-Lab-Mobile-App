package com.example.android.smartlabapplication;

import android.support.test.InstrumentationRegistry;

import com.example.android.smartlabapplication.Util.TextAnalyzer;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by DELL on 6/10/2019.
 */

public class TextAnalyzerTest {
    private TextAnalyzer textAnalyzer;

    @Before
    public void setUp()
    {
        textAnalyzer = new TextAnalyzer(InstrumentationRegistry.getTargetContext());
    }
    //Unit Tests
    @Test
    public void isSuitable()
    {
        Assert.assertEquals(textAnalyzer.isSuitable("Light001","on"),true);
    }

    @Test
    public void isNotSuitable()
    {
        Assert.assertNotSame(textAnalyzer.isSuitable("Light001","get"),false);
    }
    @After
    public void finish()
    {
        textAnalyzer.finish();
    }

}
