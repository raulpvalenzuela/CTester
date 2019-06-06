package com.lsc.ctesterfx.interfaces;

/**
 * Interface to be implemented by the different loggers in the system.
 *
 * @author dma@logossmartcard.com
 */
public interface ILogger
{
    static final String COMMENT_HEADER = "// ";
    static final String ERROR_HEADER = "// [ERROR]: ";
    static final String WARNING_HEADER = "// [WARNING]: ";
    static final String DEBUG_HEADER = "// [DEBUG]: ";

    /**
     * Method to log general information.
     *
     * @param text: text to be printed.
     */
    public void log(String text);

    /**
     * Method to log comments.
     *
     * @param text: text to be printed.
     */
    public void logComment(String text);

    /**
     * Method to log errors.
     *
     * @param text: text to be printed.
     */
    public void logError(String text);

    /**
     * Method to log warnings.
     *
     * @param text: text to be printed.
     */
    public void logWarning(String text);

    /**
     * Method to log debug information.
     *
     * @param text: text to be printed.
     */
    public void logDebug(String text);
}
