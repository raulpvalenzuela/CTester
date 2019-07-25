package com.lsc.ctesterapi;

import com.lsc.ctesterfx.Context;
import com.lsc.ctesterfx.gui.GUIController;
import com.lsc.ctesterfx.shell.ShellController;
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
     * Pause the current test. It will get resumed once the dialog is closed if in GUI mode
     * or once the ENTER key is pressed if in COMMAND_LINE_ONLY mode.
     */
    protected void pause()
    {
        Context context = Context.newInstance();

        if (context.getMode() == Context.MODE.GUI)
        {
            // Creating the dialog is not blocking, as all GUI changes have to be made
            // in the background. The way to know when the user closed the dialog is by
            // setting a flag.
            GUIController.newInstance()
                    .showPauseDialog(Strings.PAUSE_TITLE, Strings.PAUSE_BODY, Strings.PAUSE_CONTINUE);

            context.pause();

            while (context.isPaused()) {}
        }
        else
        {
            // When run through the command line, it will prompt for enter
            // and it will block the execution. So there's no need to notify anyone.
            ShellController.pause();
        }
    }
}
