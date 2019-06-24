package com.lsc.ctesterapi.logger;

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
     * Logs general information. The text will be printed as it is received
     * plus a new line.
     *
     * @param text: text to be printed.
     */
    @Override
    public void log(String text)
    {
        applicationLogger.log(text);
    }

    /**
     * Logs comments. The text will be appended to the string "// " and
     * a new line will be added.
     *
     * @param text: text to be printed as a comment.
     */
    @Override
    public void logComment(String text)
    {
        applicationLogger.logComment(text);
    }

    /**
     * Logs errors. Special type of comment that gets printed in RED and
     * with the label "[ERROR]" at the beginning.
     *
     * @param text: text to be printed as an error.
     */
    @Override
    public void logError(String text)
    {
        applicationLogger.logError(text);
    }

    /**
     * Logs warnings. Special type of comment that gets printed in YELLOW and
     * with the label "[ERROR]" at the beginning.
     *
     * @param text: text to be printed as a warning.
     */
    @Override
    public void logWarning(String text)
    {
        applicationLogger.logWarning(text);
    }

    /**
     * Logs debug information. Meaning that it will only be printed
     * in the screen and not in the file.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logDebug(String text)
    {
        applicationLogger.logDebug(text);
    }

    /**
     * Logs a succesful message. Special type of comment that gets printed in GREEN
     * and with the label "[SUCCESS]" at the beginning.
     *
     * @param text: text to be printed.
     */
    @Override
    public void logSuccess(String text)
    {
        applicationLogger.logSuccess(text);
    }
}
