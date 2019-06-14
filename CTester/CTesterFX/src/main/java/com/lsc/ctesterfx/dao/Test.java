package com.lsc.ctesterfx.dao;

import java.io.File;

/**
 * Class that represents a test. It will contain basically a reference to the java file.
 *
 * @author dma@logossmartcard.com
 */
public class Test
{
    // File containing the .java file.
    private final File file;

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
}
