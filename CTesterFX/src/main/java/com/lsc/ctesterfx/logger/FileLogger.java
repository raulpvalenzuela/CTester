package com.lsc.ctesterfx.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that logs the results in a file.
 *
 * @author dma@logossmartcard.com
 */
public class FileLogger extends AbstractLogger
{
    private static final String LOG_EXTENSION = ".log";

    private static FileLogger mFileLogger;
    // Reference to the log file.
    private File mLogFile;
    private FileWriter mLogFileWriter;

    private FileLogger() {}

    public static synchronized FileLogger newInstance()
    {
        if (mFileLogger == null)
        {
            mFileLogger = new FileLogger();
        }

        return mFileLogger;
    }

    /**
     * Sets up the logger and creates the log file.
     *
     * @param fileName: name of the log file.
     * @param path: path where the log file should be located.
     */
    public void setup(String fileName, String path)
    {
        try
        {
            _createFile(fileName, path);

        } catch (IOException ex) {
            // TODO
        }
    }

    @Override
    public void log(String text)
    {

    }

    @Override
    public void logComment(String text)
    {

    }

    @Override
    public void logError(String text)
    {

    }

    @Override
    public void logWarning(String text)
    {

    }

    @Override
    public void logDebug(String text)
    {

    }

    @Override
    public void logSuccess(String text)
    {

    }

    /**
     * Creates the log file in the specified path.
     *
     * @param fileName: log file name.
     * @param path: path where the file should be located.
     * @return: reference to the file.
     */
    private void _createFile(String fileName, String path) throws IOException
    {
        mLogFile = new File(path + fileName + LOG_EXTENSION);
        mLogFile.createNewFile();

        mLogFileWriter = new FileWriter(mLogFile);
    }
}
