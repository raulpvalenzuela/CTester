package com.lsc.ctesterfx.dao;

import java.io.File;

/**
 *
 * @author dma@logossmartcard.com
 */
public class Test
{
    // File containing the .java file.
    private File mFile;

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
