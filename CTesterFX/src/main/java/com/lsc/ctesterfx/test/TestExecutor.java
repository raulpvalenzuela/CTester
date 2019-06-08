package com.lsc.ctesterfx.test;

import java.lang.reflect.Method;

/**
 * Class that hides how to run a method from a .java class.
 *
 * @author dma@logossmartcard.com
 */
public class TestExecutor
{
    private static TestExecutor testExecutor;

    private TestExecutor() {}

    public static synchronized TestExecutor newInstance()
    {
        if (testExecutor == null)
        {
            testExecutor = new TestExecutor();
        }

        return testExecutor;
    }

    /**
     * Method that runs the given method contained in the object.
     *
     * @param object: instance of the class containig the method.
     * @param method: method to be executed.
     * @return boolean with the result of the execution.
     * @exception Exception
     *
     *   IllegalAccessException: if this {@code Method} object
     *       is enforcing Java language access control and the underlying
     *       method is inaccessible.
     *   IllegalArgumentException: if the method is an
     *       instance method and the specified object argument
     *       is not an instance of the class or interface
     *       declaring the underlying method (or of a subclass
     *       or implementor thereof); if the number of actual
     *       and formal parameters differ; if an unwrapping
     *       conversion for primitive arguments fails; or if,
     *       after possible unwrapping, a parameter value
     *       cannot be converted to the corresponding formal
     *       parameter type by a method invocation conversion.
     *   InvocationTargetException: if the underlying method
     *       throws an exception.
     *   NullPointerException: if the specified object is null
     *       and the method is an instance method.
     *   ExceptionInInitializerError: if the initialization
     *       provoked by this method fails.
     */
    public boolean run(final Object object, final Method method) throws Exception
    {
        Object result = method.invoke(object);

        return (Boolean) result;
    }
}
