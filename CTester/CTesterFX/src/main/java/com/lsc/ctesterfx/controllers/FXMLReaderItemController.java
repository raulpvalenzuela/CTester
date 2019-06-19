package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author danim
 */
public class FXMLReaderItemController implements Initializable {

    @FXML
    private JFXButton mReaderButton;

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

    public void setAttributes(String name)
    {
        mReaderButton.setText(name);
    }
}
