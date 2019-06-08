package com.lsc.ctesterfx.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class that logs the results in a file.
 *
 * @author dma@logossmartcard.com
 */
public class FileLogger extends AbstractLogger
{
    private static final String LOG_EXTENSION = ".log";

    private String fileName;
    private String path;

    private File logFile;
    private FileWriter logFileWriter;

    public static class Builder
    {
        private String fileName;
        private String path;

        public Builder() {}

        /**
         * Sets the file name.
         * @param fileName: name of the test file.
         * @return a Builder instance
         */
        public Builder withName(String fileName)
        {
            this.fileName = fileName;

            return this;
        }

        /**
         * Sets the path to the test file.
         * @param path: path to the test file.
         * @return a Builder instance
         */
        public Builder in(String path)
        {
            this.path = path;

            return this;
        }

        public FileLogger build()
        {
            FileLogger fileLogger = new FileLogger();

            fileLogger.fileName = fileName;
            fileLogger.path     = path;

            return fileLogger;
        }
    }

    private FileLogger() {}

    /**
     * Sets up the logger and creates the log file.
     */
    public void initialize()
    {
        try
        {
            _createFile();

        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Closes the log file.
     */
    public void close()
    {
        try
        {
            logFileWriter.close();

        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void log(String text)
    {
        _append(text);
    }

    @Override
    public void logComment(String text)
    {
        _append(text);
    }

    @Override
    public void logError(String text)
    {
        _append(text);
    }

    @Override
    public void logWarning(String text)
    {
        _append(text);
    }

    @Override
    public void logDebug(String text)
    {
        _append(text);
    }

    @Override
    public void logSuccess(String text)
    {
        _append(text);
    }

    /**
     * Appends the text.
     *
     * @param text: text to be appended.
     */
    private void _append(String text)
    {
        try
        {
            logFileWriter.append(text);
            logFileWriter.flush();

        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Creates the log file in the specified path.
     *
     * @return: reference to the file.
     */
    private void _createFile() throws IOException
    {
        logFile = new File(path + System.getProperty("file.separator") + fileName + LOG_EXTENSION);
        logFile.createNewFile();

        // Delete the content
        new PrintWriter(logFile).close();

        logFileWriter = new FileWriter(logFile);
    }
}
