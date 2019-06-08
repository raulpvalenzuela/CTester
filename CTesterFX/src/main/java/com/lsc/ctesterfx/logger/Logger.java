package com.lsc.ctesterfx.logger;

/**
 * This layer will manage the different loggers in the system.
 *
 * @author dma@logossmartcard.com
 */
public class Logger extends AbstractLogger
{
    private static Logger logger;
    // Internal references to the different loggers.
    private final Printer printer;
    private FileLogger fileLogger;

    private Logger()
    {
        printer = Printer.newInstance();
    }

    public static synchronized Logger newInstance()
    {
        if (logger == null)
        {
            logger = new Logger();
        }

        return logger;
    }

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
