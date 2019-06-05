package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.tests.TestLoader;
import com.lsc.ctesterfx.constants.Constants;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;

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
    
    private File mTestFile;
    private String mTestname;
    private String mTestPath;
    
    private int mIndex;
    
    private FXMLMainController mMainController;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        _setupTooltips();
    }
    
    /**
     * Sets up all the buttons' tooltips.
     */
    private void _setupTooltips()
    {
        mRunTestButton.setTooltip(new Tooltip(Constants.TOOLTIP_RUN_TEST));
        mRemoveTestButton.setTooltip(new Tooltip(Constants.TOOLTIP_REMOVE_TEST));
    }

    @FXML
    private void onClickRunTestButton(ActionEvent event) 
    {
        TestLoader testLoader = TestLoader.newInstance();
        
        try 
        {
            testLoader.compile(Paths.get(mTestFile.getParent()), mTestFile);
            testLoader.load(mTestFile);
            
        } catch (Exception ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event) 
    {
        mMainController.removeTestAtIndex(mIndex);
    }
    
    /**
     * Sets only the index of the test in the list.
     * 
     * @param index: index of the test in the list.
     */
    public void setAttributes(int index)
    {
        mIndex = index;
    }
    
    /**
     * Sets the different attributes needed to initialize the test item.
     * 
     * @param file: name of the test file.
     * @param controller: controller that has to be notified if something happens.
     * @param index: index in the list.
     */
    public void setAttributes(File file, FXMLMainController controller, int index)
    {
        mTestFile = file;
        mTestname = file.getName().replace(".java", "");
        mTestPath = file.getAbsolutePath();
        
        mMainController = controller;
        
        mIndex = index;
        
        mTestNameCheckbox.setMnemonicParsing(false);
        mTestNameCheckbox.setText(mTestname);
    }
    
    /**
     * Selects or deselects the checkbox.
     * 
     * @param selected: the checkbox has to be selected if true. Deselected otherwise.
     */
    public void select(boolean selected)
    {
        mTestNameCheckbox.setSelected(selected);
    }
}
