package com.lsc.ctesterfx.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.lsc.ctesterfx.constants.Colors;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class that manages the creation of dialogs in the screen.
 *
 * @author dma@logossmartcard.com
 */
public class DialogCreator
{
    private final JFXDialog dialog;
    // Runnable to be executed when the dialog gets closed.
    private Runnable onClose;

    /**
     * Creates a new dialog.
     *
     * @param container: StackPane where the dialog is shown on.
     * @param title: title of the dialog.
     * @param message: body of the dialog.
     * @param action: button message.
     * @param overlayClose: flag to indicate if the dialog should be closed when clicked outside.
     */
    public DialogCreator(
              StackPane container
            , String title
            , String message
            , String action
            , boolean overlayClose)
    {
        JFXDialogLayout layout = new JFXDialogLayout();
        dialog = new JFXDialog(
                        container
                      , layout
                      , JFXDialog.DialogTransition.CENTER
                      , overlayClose);

        layout.setStyle("-fx-background-color: #333333");

        Label heading = new Label(title);
        heading.setFont(Font.font("Microsoft JhengHei UI Light", FontWeight.BOLD, 18));
        heading.setTextFill(Color.web("#CCCCCC"));

        Label body = new Label(message);
        body.setFont(Font.font("Microsoft JhengHei UI Light", 14));
        body.setTextFill(Color.web("#CCCCCC"));

        JFXButton button = new JFXButton(action);
        button.setButtonType(JFXButton.ButtonType.FLAT);
        button.setBackground(Background.EMPTY);
        button.setFont(Font.font("Microsoft JhengHei UI Light", 12));
        button.setTextFill(Color.web(Colors.createAsString(Colors.Color.ACCENT)));
        button.setOnAction((ActionEvent event) ->
        {
            dialog.close();

            if (onClose != null)
            {
                onClose.run();
            }
        });

        layout.setHeading(heading);
        layout.setBody(body);
        layout.setActions(button);
    }

    /**
     * Executes the runnable when the dialog gets closed.
     *
     * @param onClose runnable to be executed when the dialog gets closed.
     */
    public void setOnCloseListener(final Runnable onClose)
    {
        this.onClose = onClose;
    }

    /**
     * Displays the dialog.
     */
    public void show()
    {
        dialog.show();
    }
}
