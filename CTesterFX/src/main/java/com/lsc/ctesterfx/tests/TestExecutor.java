package com.lsc.ctesterfx.tests;

import java.lang.reflect.Method;

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

    public boolean run(Object object, Method method) throws Exception
    {
        Object result = method.invoke(object);

        return (Boolean) result;
    }
}
