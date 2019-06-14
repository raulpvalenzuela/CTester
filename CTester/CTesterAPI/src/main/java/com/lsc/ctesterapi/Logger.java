package com.lsc.ctesterapi;

import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.logger.ApplicationLogger;

/**
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

    @Override
    public void log(String text)
    {
        applicationLogger.log(text);
    }

    @Override
    public void logComment(String text)
    {
        applicationLogger.logComment(text);
    }

    @Override
    public void logError(String text)
    {
        applicationLogger.logError(text);
    }

    @Override
    public void logWarning(String text)
    {
        applicationLogger.logWarning(text);
    }

    @Override
    public void logDebug(String text)
    {
        applicationLogger.logDebug(text);
    }

    @Override
    public void logSuccess(String text)
    {
        applicationLogger.logSuccess(text);
    }
}
