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

    public String getPath()
    {
        return mFile.getParent();
    }

    public String getName()
    {
        return mFile.getName().replace(".java", "");
    }

    public File getFile()
    {
        return mFile;
    }
}
