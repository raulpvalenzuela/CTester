package com.lsc.ctesterapi;

import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.logger.ApplicationLogger;

/**
 * Class used to log information in the screen and in the log file.
 *
 * @author dma@logossmartcard.com
 */
public class Logger extends AbstractLogger
{
    private static Logger logger;

    private final ApplicationLogger applicationLogger;

    private Logger()
    {
        applicationLogger = ApplicationLogger.newInstance();
    }

    public static synchronized Logger newInstance()
    {
        if (logger == null)
        {
            logger = new Logger();
        }

        return logger;
    }

    /**
     * Logs general information.
     *
     * @param text: text to be printed.
     */
    @Override
    public void log(String text)
    {
        applicationLogger.log(text);
    }

    /**
     * Logs comments.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logComment(String text)
    {
        applicationLogger.logComment(text);
    }

    /**
     * Logs errors.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logError(String text)
    {
        applicationLogger.logError(text);
    }

    /**
     * Logs warnings.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logWarning(String text)
    {
        applicationLogger.logWarning(text);
    }

    /**
     * Logs debug information.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logDebug(String text)
    {
        applicationLogger.logDebug(text);
    }

    /**
     * Logs a succesful message.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logSuccess(String text)
    {
        applicationLogger.logSuccess(text);
    }
}
