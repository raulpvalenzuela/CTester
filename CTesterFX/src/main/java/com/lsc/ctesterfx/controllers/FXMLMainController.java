package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

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
    private BorderPane mOutputContainer;
    
    @FXML
    private JFXButton mAddTestsButton;
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
    private JFXButton mFABButton;
    @FXML
    private JFXButton mCompileTestsButton;
    @FXML
    private JFXButton mRunTestsButton;
    @FXML
    private JFXButton mSettingsButton;
    
    @FXML
    private JFXTextField mCommandTextfield;
    
    // RichTextArea that will contain the output of the test.
    private final InlineCssTextArea mOutputTextArea = new InlineCssTextArea();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // Set the common style for output. Monospace and font size.
        mOutputTextArea.setStyle("-fx-font-family: monospace; -fx-font-size: 10pt;");
        // Not editable.
        mOutputTextArea.setEditable(false);
        // Transparent background
        mOutputTextArea.setBackground(Background.EMPTY);
        // No wrapping.
        mOutputTextArea.setWrapText(false);
        
        // Container of the output text area. The virtualized container will only render the text visible.
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane<>(mOutputTextArea);
        VBox.setVgrow(vsPane, Priority.ALWAYS);
        
        // Force to fill the parent size.
        vsPane.prefWidthProperty().bind(mOutputContainer.prefWidthProperty());
        vsPane.prefHeightProperty().bind(mOutputContainer.prefHeightProperty());
        
        mOutputContainer.setCenter(vsPane);
    }
}
