package com.lsc.ctesterfx.tests;

import java.io.File;

/**
 * The purpose of this class is to run Java tests.
 * 
 * @author dma@logossmartcard.com
 */
public class TestExecutor
{
    private static TestExecutor mTestExecutor;
    
    private TestExecutor() {}
    
    public static synchronized TestExecutor newInstance()
    {
        if (mTestExecutor == null)
        {
            mTestExecutor = new TestExecutor();
        }
        
        return mTestExecutor;
    }
    
    public void run(File test)
    {
        
    }
}
