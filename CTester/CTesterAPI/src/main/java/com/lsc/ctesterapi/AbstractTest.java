package com.lsc.ctesterapi;

import com.lsc.ctesterfx.Context;
import com.lsc.ctesterfx.gui.GUIController;
import com.lsc.ctesterlib.constants.Strings;

/**
 * Abstract class that every test should extend from.
 *
 * @author dma@logossmartcard.com
 */
public abstract class AbstractTest
{
    /**
     * This method will be executed before the 'run' method.
     *
     * @return true if the execution of the setup is succesful, false otherwise.
     */
    public abstract boolean setUp();

    /**
     * This is the main method of the test.
     *
     * @return true if the execution of the test is succesful, false otherwise.
     */
    public abstract boolean run();

    /**
     * This method will be executed after the 'run' method.
     *
     * @return true if the execution of the teardown is succesful, false otherwise.
     */
    public abstract boolean tearDown();

    /**
     * Pause the current test. It will get resumed once the dialog is closed.
     */
    protected void pause()
    {
        GUIController.newInstance()
                .showPauseDialog(Strings.PAUSE_TITLE, Strings.PAUSE_BODY, Strings.PAUSE_CONTINUE);

        Context context = Context.newInstance();
        context.pause();

        while (context.isPaused()) {}
    }
}
