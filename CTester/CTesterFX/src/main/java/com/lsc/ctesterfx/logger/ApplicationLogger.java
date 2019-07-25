package com.lsc.ctesterfx.logger;

import com.lsc.ctesterfx.Context;
import com.lsc.ctesterfx.Context.MODE;

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
    // Reference to the context.
    private final Context context;
    // Internal references to the different loggers.
    private Printer printer;
    private FileLogger fileLogger;
    private ShellLogger shellLogger;

    private ApplicationLogger()
    {
        context = Context.newInstance();

        if (context.getMode() == MODE.COMMAND_LINE_ONLY)
        {
            shellLogger = ShellLogger.newInstance();
        }
        else
        {
            printer = Printer.newInstance();
        }
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
        if (context.getMode() == MODE.GUI) printer.log(text);
        else shellLogger.log(text);

        fileLogger.log(text);
    }

    @Override
    public void logComment(String text)
    {
        if (context.getMode() == MODE.GUI) printer.logComment(text);
        else shellLogger.logComment(text);

        fileLogger.logComment(text);
    }

    @Override
    public void logError(String text)
    {
        if (context.getMode() == MODE.GUI) printer.logError(text);
        else shellLogger.logError(text);

        fileLogger.logError(text);
    }

    @Override
    public void logWarning(String text)
    {
        if (context.getMode() == MODE.GUI) printer.logWarning(text);
        else shellLogger.logWarning(text);

        fileLogger.logWarning(text);
    }

    @Override
    public void logDebug(String text)
    {
        if (context.getMode() == MODE.GUI) printer.logDebug(text);
        else shellLogger.logDebug(text);

        fileLogger.logDebug(text);
    }

    @Override
    public void logSuccess(String text)
    {
        if (context.getMode() == MODE.GUI) printer.logSuccess(text);
        else shellLogger.logSuccess(text);

        fileLogger.logSuccess(text);
    }
}
