package com.lsc.ctesterfx.interfaces;

/**
 *
 *
 * @author dma@logossmartcard.com
 */
public interface ILogger
{
    public void log(String message);

    public void logError(String message);

    public void logWarning(String message);

    public void logDebug(String message);
}
