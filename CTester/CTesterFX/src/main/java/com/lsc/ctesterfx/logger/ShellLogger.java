/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lsc.ctesterfx.logger;

/**
 * Logger used to print text when the application is launched
 * from the command line.
 *
 * @author dma@logossmartard.com
 */
public class ShellLogger extends AbstractLogger
{
    private static ShellLogger logger;

    private ShellLogger() {}

    public static synchronized ShellLogger newInstance()
    {
        if (logger == null)
        {
            logger = new ShellLogger();
        }

        return logger;
    }

    @Override
    public void log(String text)
    {
        print(text);
    }

    @Override
    public void logComment(String text)
    {
        text = COMMENT_HEADER + text;

        print(text);
    }

    @Override
    public void logError(String text)
    {
        text = ERROR_HEADER + text;

        print(text);
    }

    @Override
    public void logWarning(String text)
    {
        text = WARNING_HEADER + text;

        print(text);
    }

    @Override
    public void logDebug(String text)
    {
        text = DEBUG_HEADER + text;

        print(text);
    }

    @Override
    public void logSuccess(String text)
    {
        text = SUCCESS_HEADER + text;

        print(text);
    }

    /**
     * Logs the message.
     *
     * @param text text to be printed.
     */
    private void print(String text)
    {
        System.out.println(text);
    }
}
