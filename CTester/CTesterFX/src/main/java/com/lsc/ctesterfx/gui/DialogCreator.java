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

    /**
     * Creates a new dialog.
     *
     * @param container: StackPane where the dialog is shown on.
     * @param title: title of the dialog.
     * @param message: body of the dialog.
     */
    public DialogCreator(StackPane container, String title, String message)
    {
        JFXDialogLayout layout = new JFXDialogLayout();
        dialog = new JFXDialog(container, layout, JFXDialog.DialogTransition.CENTER);

        layout.setStyle("-fx-background-color: #333333");

        Label heading = new Label(title);
        heading.setFont(Font.font("Microsoft JhengHei UI Light", FontWeight.BOLD, 18));
        heading.setTextFill(Color.web("#CCCCCC"));

        Label body = new Label(message);
        body.setFont(Font.font("Microsoft JhengHei UI Light", 14));
        body.setTextFill(Color.web("#CCCCCC"));

        JFXButton button = new JFXButton("Close");
        button.setButtonType(JFXButton.ButtonType.FLAT);
        button.setBackground(Background.EMPTY);
        button.setFont(Font.font("Microsoft JhengHei UI Light", 12));
        button.setTextFill(Color.web(Colors.createAsString(Colors.Color.ACCENT)));
        button.setOnAction((ActionEvent event) ->
        {
            dialog.close();
        });

        layout.setHeading(heading);
        layout.setBody(body);
        layout.setActions(button);
    }

    /**
     * Displays the dialog.
     */
    public void show()
    {
        dialog.show();
    }
}
