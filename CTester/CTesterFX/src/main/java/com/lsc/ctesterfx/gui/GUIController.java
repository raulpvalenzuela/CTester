package com.lsc.ctesterfx.gui;

import javafx.scene.control.Label;

/**
 * Class to modify the GUI from the outside.
 *
 * @author dma@logossmartcard.com
 */
public class GUIController
{
    private static GUIController guiController;

    // Label version
    private Label readerLabel;

    private GUIController() {}

    public static GUIController newInstance()
    {
        if (guiController == null)
        {
            guiController = new GUIController();
        }

        return guiController;
    }

    /**
     * Sets the reader label.
     *
     * @param label reader label.
     */
    public void setAttributes(Label label)
    {
        this.readerLabel = label;
    }

    /**
     * Sets the new reader name.
     *
     * @param reader new reader name.
     */
    public void updateReader(String reader)
    {
        this.readerLabel.setText(reader);
    }
}
