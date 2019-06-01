package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class of the main window.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLMainController implements Initializable 
{
    @FXML
    private VBox mTestListVBox;
    
    @FXML
    private JFXButton mAddTestsButton;
    @FXML
    private JFXButton mAddTestsButton1;
    @FXML
    private JFXButton mAddTestsButton2;
    @FXML
    private JFXButton mAddTestsButton21;
    @FXML
    private JFXButton mSendButton;
    @FXML
    private JFXButton mResetButton;
    @FXML
    private JFXButton mBootloaderButton;
    @FXML
    private JFXButton mGetProductCodeButton;
    @FXML
    private JFXButton mSecurityHistoryButton;
    @FXML
    private JFXButton mVirginizeButton;
    @FXML
    private JFXButton mSettingsTestsButton1;
    
    @FXML
    private JFXTextField mCommandTextfield;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        for (int i = 0; i < 7; ++i)
        {
            try 
            {
                Parent testItem = FXMLLoader.load(getClass().getResource("/fxml/TestItem.fxml"));
                
                mTestListVBox.getChildren().add(testItem);
                
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
