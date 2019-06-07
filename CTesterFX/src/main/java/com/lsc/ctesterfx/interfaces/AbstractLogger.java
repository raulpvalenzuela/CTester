package com.lsc.ctesterfx.interfaces;

/**
 * Abstract class to be implemented by the different loggers in the system.
 *
 * @author dma@logossmartcard.com
 */
public abstract class AbstractLogger
{
    protected static final String COMMENT_HEADER = "// ";
    protected static final String ERROR_HEADER   = "// [ERROR]: ";
    protected static final String WARNING_HEADER = "// [WARNING]: ";
    protected static final String DEBUG_HEADER   = "// [DEBUG]: ";
    protected static final String SUCCESS_HEADER = "// [SUCCESS]: ";

    /**
     * Method to log general information.
     *
     * @param text: text to be printed.
     */
    public abstract void log(String text);

    /**
     * Method to log comments.
     *
     * @param text: text to be printed.
     */
    public abstract void logComment(String text);

    /**
     * Method to log errors.
     *
     * @param text: text to be printed.
     */
    public abstract void logError(String text);

    /**
     * Method to log warnings.
     *
     * @param text: text to be printed.
     */
    public abstract void logWarning(String text);

    /**
     * Method to log debug information.
     *
     * @param text: text to be printed.
     */
    public abstract void logDebug(String text);

    /**
     * Method to log a succesful message.
     *
     * @param text: text to be printed.
     */
    public abstract void logSuccess(String text);
}
