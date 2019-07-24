package com.lsc.ctesterfx.gui;

import com.lsc.ctesterfx.Context;
import com.lsc.ctesterfx.background.PauseNotifier;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

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
    // Stack pane used to show dialogs
    private StackPane dialogContainer;

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
     * Sets the dialog container.
     *
     * @param container dialog container.
     */
    public void setDialogContainer(final StackPane container)
    {
        this.dialogContainer = container;
    }

    /**
     * Sets the reader label.
     *
     * @param label reader label.
     */
    public void setAttributes(final Label label)
    {
        this.readerLabel = label;
    }

    /**
     * Sets the new reader name.
     *
     * @param reader new reader name.
     */
    public void updateReader(final String reader)
    {
        this.readerLabel.setText(reader);
    }

    /**
     * Creates a new dialog.
     *
     * @param title: title of the dialog.
     * @param message: body of the dialog.
     * @param action: button message.
     */
    public void showPauseDialog(final String title, final String message, final String action)
    {
        Platform.runLater(() ->
        {
            DialogCreator dialog =
                    new DialogCreator(dialogContainer, title, message, action, false);

            dialog.setOnCloseListener((PauseNotifier) () ->
            {
                Context.newInstance().unpause();
            });

            dialog.show();
        });
    }
}
