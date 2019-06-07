package com.lsc.ctesterfx.logger;

import javafx.scene.layout.BorderPane;

/**
 * This layer will manage the different loggers.
 *
 * @author dma@logossmartcard.com
 */
public class Logger extends AbstractLogger
{
    // Internal references to the different loggers.
    private static Logger mLogger;
    private static Printer mPrinter;

    private Logger() {}

    public static synchronized Logger newInstance()
    {
        if (mLogger == null)
        {
            mLogger = new Logger();
        }

        return mLogger;
    }

    /**
     * FOR INTERNAL USE ONLY
     * Method to initialize the logger.
     *
     * @param container: layout containing the output pane.
     */
    public void setup(BorderPane container)
    {
        mPrinter = Printer.newInstance();
        mPrinter.setup(container);
    }

    /**
     * Clears the output area.
     */
    public void clear()
    {
        mPrinter.clear();
    }

    @Override
    public void log(String text)
    {
        text = text + "\n";

        mPrinter.log(text);
    }

    @Override
    public void logComment(String text)
    {
        text = COMMENT_HEADER + text + "\n";

        mPrinter.logComment(text);
    }

    @Override
    public void logError(String text)
    {
        text = ERROR_HEADER + text + "\n";

        mPrinter.logError(text);
    }

    @Override
    public void logWarning(String text)
    {
        text = WARNING_HEADER + text + "\n";

        mPrinter.logWarning(text);
    }

    @Override
    public void logDebug(String text)
    {
        text = DEBUG_HEADER + text + "\n";

        mPrinter.logDebug(text);
    }

    @Override
    public void logSuccess(String text)
    {
        text = SUCCESS_HEADER + text + "\n";

        mPrinter.logSuccess(text);
    }
}
