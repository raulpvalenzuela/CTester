package com.lsc.ctesterfx;

import com.lsc.ctesterfx.controllers.FXMLMainController;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class contains all the relevant information of
 * what is happening in the application.
 *
 * @author dma@logossmartcard.com
 */
public class Context
{
    private static Context context;

    // Atomic flag to know if the test has been paused.
    private final AtomicBoolean paused;
    // Generic object used to make the methods thread-safe.
    private final Object object;

    // Reference to the FXMLMainController
    private FXMLMainController mainController;

    private Context()
    {
        paused = new AtomicBoolean(false);
        object = new Object();
    }

    public static Context newInstance()
    {
        if (context == null)
        {
            context = new Context();
        }

        return context;
    }

    /**
     * Sets the reference to the main controller
     * so that other components can call it.
     *
     * @param mainController reference to the main controller.
     */
    public void setMainController(FXMLMainController mainController)
    {
        this.mainController = mainController;
    }

    /**
     * Returns the main controller.
     *
     * @return the main controller.
     */
    public FXMLMainController getMainController()
    {
        return mainController;
    }

    /**
     * Sets the pause flag to true thread safely.
     */
    public void pause()
    {
        synchronized (object)
        {
            paused.set(true);
        }
    }

    /**
     * Sets the pause flag to false thread safely.
     */
    public void unpause()
    {
        synchronized (object)
        {
            paused.set(false);
        }
    }

    /**
     * Returns the value of the flag thread safely.
     *
     * @return the value of the flag thread safely.
     */
    public boolean isPaused()
    {
        boolean result;

        synchronized (object)
        {
            result = paused.get();
        }

        return result;
    }
}
