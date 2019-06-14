package com.lsc.ctesterfx.logger;

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
     * Logs general information.
     *
     * @param text: text to be printed.
     */
    public abstract void log(String text);

    /**
     * Logs comments.
     *
     * @param text: text to be printed.
     */
    public abstract void logComment(String text);

    /**
     * Logs errors.
     *
     * @param text: text to be printed.
     */
    public abstract void logError(String text);

    /**
     * Logs warnings.
     *
     * @param text: text to be printed.
     */
    public abstract void logWarning(String text);

    /**
     * Logs debug information.
     *
     * @param text: text to be printed.
     */
    public abstract void logDebug(String text);

    /**
     * Logs a succesful message.
     *
     * @param text: text to be printed.
     */
    public abstract void logSuccess(String text);
}
