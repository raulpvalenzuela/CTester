package com.lsc.ctesterfx.controllers;

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

public class FXMLMainController implements Initializable 
{
    @FXML
    private VBox mTestListVBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        for (int i = 0; i < 15; ++i)
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
