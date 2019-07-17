package com.lsc.ctesterfx.test;

import java.io.File;

/**
 * Class that represents a test. It will contain basically a reference to the java file
 * and the state of the test.
 *
 * @author dma@logossmartcard.com
 */
public class Test
{
    public enum TEST_STATE
    {
        QUEUED,
        NOT_COMPILED,
        COMPILING,
        COMPILATION_OK,
        COMPILATION_FAILED,
        RUNNING,
        EXECUTION_OK,
        EXECUTION_FAILED,
        STOPPED
    }

    // File containing the .java file.
    private final File file;
    private TEST_STATE state;

    public Test(File file)
    {
        this.file = file;
    }

    /**
     * Returns the pathname string of the parent directory.
     *
     * @return The pathname string of the parent directory.
     */
    public String getPath()
    {
        return file.getParent();
    }

    /**
     * Returns the filename excluding the extension.
     *
     * @return the filename excluding the extension.
     */
    public String getName()
    {
        return file.getName().replace(".java", "");
    }

    /**
     * Returns the instance of the File object.
     *
     * @return the instance of the File object.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Sets a new state.
     *
     * @param newState new state of the test.
     */
    public void setState(TEST_STATE newState)
    {
        state = newState;
    }

    /**
     * Returns the state of the test.
     *
     * @return the state of the test.
     */
    public TEST_STATE getState()
    {
        return state;
    }
}
