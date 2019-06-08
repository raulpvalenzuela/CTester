package com.lsc.ctesterfx.dao;

import java.io.File;

/**
 * Class that represents a test. It will contain basically a reference to the file.
 *
 * @author dma@logossmartcard.com
 */
public class Test
{
    // File containing the .java file.
    private final File mFile;

    public Test(File file)
    {
        mFile = file;
    }

    /**
     * Returns the pathname string of the parent directory.
     *
     * @return The pathname string of the parent directory.
     */
    public String getPath()
    {
        return mFile.getParent();
    }

    /**
     * Returns the filename excluding the extension.
     *
     * @return the filename excluding the extension.
     */
    public String getName()
    {
        return mFile.getName().replace(".java", "");
    }

    /**
     * Returns the instance of the File object.
     *
     * @return the instance of the File object.
     */
    public File getFile()
    {
        return mFile;
    }
}
