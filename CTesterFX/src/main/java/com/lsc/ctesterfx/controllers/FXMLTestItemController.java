package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class of every test item in the VBox.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLTestItemController implements Initializable 
{
    @FXML
    private JFXCheckBox mTestNameCheckbox;    
    @FXML
    private JFXButton mRunTestButton;    
    @FXML
    private JFXButton mRemoveTestButton;
    
    private String mTestname;
    private String mTestPath;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }
    
    public void setAttributes(File file)
    {
        mTestname = file.getName();
        mTestPath = file.getAbsolutePath();
        
        mTestNameCheckbox.setText(mTestname);
    }
}
