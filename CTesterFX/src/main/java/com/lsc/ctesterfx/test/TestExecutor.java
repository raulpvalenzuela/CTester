package com.lsc.ctesterfx.test;

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

    /**
     * Method that runs the given method contained in the object.
     *
     * @param object: instance of the class containig the method.
     * @param method: method to be executed.
     * @return boolean with the result of the execution.
     * @throws Exception
     */
    public boolean run(Object object, Method method) throws Exception
    {
        Object result = method.invoke(object);

        return (Boolean) result;
    }
}
