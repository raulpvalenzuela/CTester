package com.lsc.ctesterfx.logger;

/**
 * This layer will manage the different loggers in the system. It will store
 * one instance of Printer (there can only be one in the system) and one instance
 * of FileLogger.
 *
 * As every test needs one FileLogger (different files), the reference must be changed
 * everytime a new test starts.
 *
 * @author dma@logossmartcard.com
 */
public class ApplicationLogger extends AbstractLogger
{
    private static ApplicationLogger applicationLogger;
    // Internal references to the different loggers.
    private final Printer printer;
    private FileLogger fileLogger;

    private ApplicationLogger()
    {
        printer = Printer.newInstance();
    }

    public static synchronized ApplicationLogger newInstance()
    {
        if (applicationLogger == null)
        {
            applicationLogger = new ApplicationLogger();
        }

        return applicationLogger;
    }

    /**
     * Sets a new FileLogger reference to be used.
     *
     * @param fileLogger: reference to a FileLogger.
     */
    public void setFileLogger(FileLogger fileLogger)
    {
        this.fileLogger = fileLogger;
    }

    @Override
    public void log(String text)
    {
        text = text + "\n";

        printer.log(text);
        fileLogger.log(text);
    }

    @Override
    public void logComment(String text)
    {
        text = COMMENT_HEADER + text + "\n";

        printer.logComment(text);
        fileLogger.logComment(text);
    }

    @Override
    public void logError(String text)
    {
        text = ERROR_HEADER + text + "\n";

        printer.logError(text);
        fileLogger.logError(text);
    }

    @Override
    public void logWarning(String text)
    {
        text = WARNING_HEADER + text + "\n";

        printer.logWarning(text);
        fileLogger.logWarning(text);
    }

    @Override
    public void logDebug(String text)
    {
        text = DEBUG_HEADER + text + "\n";

        printer.logDebug(text);
        fileLogger.logDebug(text);
    }

    @Override
    public void logSuccess(String text)
    {
        text = SUCCESS_HEADER + text + "\n";

        printer.logSuccess(text);
        fileLogger.logSuccess(text);
    }
}
