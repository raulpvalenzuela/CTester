package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class that manages a specific reader item in the list.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLReaderItemController implements Initializable
{
    // Reference to the Main controller.
    private FXMLMainController mainController;
    // Reader name.
    private String readerName;
    // Index of the reader in the list.
    private int index;

    @FXML
    private JFXButton mReaderButton;
    @FXML
    private Pane mReaderBarSelected;

    /**
     * Initializes the controller class.
     * @param url unused.
     * @param rb unused.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    public void setAttributes(FXMLMainController mainController, String name, int index, boolean selected)
    {
        selectReader(selected);

        this.mainController = mainController;
        this.readerName     = name;
        this.index          = index;

        mReaderButton.setText(readerName);
    }

    @FXML
    private void onClickButton(ActionEvent event)
    {
        selectReader(true);
        mainController.notifyReaderSelected(readerName, index);
    }

    /**
     * Updates the GUI to indicate that this reader has been selected.
     *
     * @param selected: true to select it, false to deselect it.
     */
    public void selectReader(boolean selected)
    {
        if (selected)
        {
            mReaderBarSelected.setStyle("-fx-background-color: #eeeeee");
        }
        else
        {
            mReaderBarSelected.setStyle("-fx-background-color: #333333");
        }
    }

    /**
     * Returns the reader name.
     *
     * @return the reader name.
     */
    public String getName()
    {
        return readerName;
    }
}
