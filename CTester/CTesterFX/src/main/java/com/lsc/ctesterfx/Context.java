package com.lsc.ctesterfx;

import com.lsc.ctesterfx.controllers.FXMLMainController;

/**
 * This class contains all the relevant information of
 * what is happening in the application.
 *
 * @author dma@logossmartcard.com
 */
public class Context
{
    private static Context context;

    // Reference to the FXMLMainController
    private FXMLMainController mainController;

    private Context() {}

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
}
