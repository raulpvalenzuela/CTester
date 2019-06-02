package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.lsc.ctesterfx.constants.Constants;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

/**
 * FXML Controller class of the main window.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLMainController implements Initializable 
{    
    // RichTextArea that will contain the output of the test.
    private final InlineCssTextArea mOutputTextArea = new InlineCssTextArea();
    
    // Flag to indicate if the commands list is visible.
    private boolean mCommandsListVisible;
    
    @FXML
    private VBox mTestListVBox;
    
    @FXML
    private BorderPane mOutputContainer;
    
    @FXML
    private StackPane mFABIconsContainer;
    
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
    
    @FXML
    private JFXCheckBox mSelectAllCheckbox;
    
    @FXML
    private ImageView mFABPlusIcon;
    
    private Stage mStage;
    
    // Add all the animations to be run when the FAB is clicked.
    private FadeTransition[] mFadeInAnimationsList;    
    private FadeTransition[] mFadeOutAnimationsList;
    private RotateTransition mRotateTransition;
    private FadeTransition mFadeInAnimation;
    private FadeTransition mFadeOutAnimation;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        _initialize();
        _setupOutputArea();
        _setupTooltips();
        _setupAnimations();
    }
    
    /**
     * Sets up different variables.
     */
    private void _initialize()
    {
        mCommandsListVisible = true;
    }
    
    /**
     * Sets up the output area.
     */
    private void _setupOutputArea()
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
    
    /**
     * Sets up all the buttons' tooltips.
     */
    private void _setupTooltips()
    {
        mAddTestsButton.setTooltip(new Tooltip(Constants.TOOLTIP_ADD_TESTS));
        mFABButton.setTooltip(new Tooltip(Constants.TOOLTIP_SHOW_MORE));
    }
    
    /**
     * Sets up all the animations in this window.
     */
    private void _setupAnimations()
    {
        mFadeInAnimationsList = new FadeTransition[] { 
              new FadeTransition(Duration.millis(Constants.FADE_IN_ANIMATION_DURATION), mVirginizeButton)
            , new FadeTransition(Duration.millis(Constants.FADE_IN_ANIMATION_DURATION), mSecurityHistoryButton)
            , new FadeTransition(Duration.millis(Constants.FADE_IN_ANIMATION_DURATION), mGetProductCodeButton)
            , new FadeTransition(Duration.millis(Constants.FADE_IN_ANIMATION_DURATION), mBootloaderButton) 
        };
        
        mFadeOutAnimationsList = new FadeTransition[] {        
              new FadeTransition(Duration.millis(Constants.FADE_OUT_ANIMATION_DURATION), mVirginizeButton)
            , new FadeTransition(Duration.millis(Constants.FADE_OUT_ANIMATION_DURATION), mSecurityHistoryButton)
            , new FadeTransition(Duration.millis(Constants.FADE_OUT_ANIMATION_DURATION), mGetProductCodeButton)
            , new FadeTransition(Duration.millis(Constants.FADE_OUT_ANIMATION_DURATION), mBootloaderButton) 
        };
        
        int delay = 0;        
        for (int i = 0; i < mFadeInAnimationsList.length; ++i)
        {
            mFadeInAnimationsList[i].setFromValue(0.0f);
            mFadeInAnimationsList[i].setToValue(1.0f);
            mFadeInAnimationsList[i].setDelay(Duration.millis(delay));
            mFadeInAnimationsList[i].setInterpolator(Interpolator.EASE_BOTH);

            delay += Constants.FAST_DELAY;
        }
        
        delay = 0;
        for (int i = mFadeOutAnimationsList.length - 1; i >= 0; --i)
        {
            mFadeOutAnimationsList[i].setFromValue(1.0f);
            mFadeOutAnimationsList[i].setToValue(0.0f);
            mFadeOutAnimationsList[i].setDelay(Duration.millis(delay));
            mFadeOutAnimationsList[i].setInterpolator(Interpolator.EASE_BOTH);

            // Hide the node once it's finished so that it cannot get clicked.
            final int index = i;
            mFadeOutAnimationsList[i].setOnFinished((e) -> {
                mFadeOutAnimationsList[index].getNode().setVisible(false);
            });
            
            delay += Constants.FAST_DELAY;
        }
        
        mRotateTransition = new RotateTransition(Duration.millis(Constants.SLOW_DELAY), mFABIconsContainer);
        mRotateTransition.setByAngle(180);
        
        mFadeInAnimation  = new FadeTransition(Duration.millis(Constants.SLOW_DELAY), mFABPlusIcon);
        mFadeOutAnimation = new FadeTransition(Duration.millis(Constants.SLOW_DELAY), mFABPlusIcon);
        
        mFadeInAnimation.setFromValue(0.0f);
        mFadeInAnimation.setToValue(1.0f);
        mFadeOutAnimation.setFromValue(1.0f);
        mFadeOutAnimation.setToValue(0.0f);
    }

    @FXML
    private void onClickAddTests(ActionEvent event) 
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Java Files", "*.java"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(mStage);        
        
        if (selectedFiles != null) 
        {
            selectedFiles.forEach((file) -> {
                try 
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TestItem.fxml"));
                    Parent testItem = (Parent) loader.load();
                    FXMLTestItemController controller = (FXMLTestItemController) loader.getController();
                    controller.setAttributes(file);
                    
                    System.out.println(file.getName());
                    
                    mTestListVBox.getChildren().add(testItem);

                } catch (IOException ex) {
                    Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @FXML
    private void onClickCompileTests(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickRunTests(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickSettings(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickFAB(ActionEvent event) 
    {
        // Rotation of the FAB 180 degrees.
        mRotateTransition.play();
        
        if (mCommandsListVisible)
        {            
            for (int i = 0; i < mFadeOutAnimationsList.length; ++i)
            {                
                mFadeOutAnimationsList[i].play();
            }
            
            // Minus to plus transition.
            mFadeInAnimation.play(); 
            // Change the tooltip.
            mFABButton.setTooltip(new Tooltip(Constants.TOOLTIP_SHOW_MORE));
        }
        else
        {
            for (int i = 0; i < mFadeInAnimationsList.length; ++i)
            {
                // Make the node visible again.
                mFadeInAnimationsList[i].getNode().setVisible(true);
                mFadeInAnimationsList[i].play();
            }
            
            // Plus to minus transition.
            mFadeOutAnimation.play();
            // Change the tooltip.
            mFABButton.setTooltip(new Tooltip(Constants.TOOLTIP_SHOW_LESS));
        }
        
        mCommandsListVisible = !mCommandsListVisible;
    }

    @FXML
    private void onClickBootloaderButton(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickGetProductCodeButton(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickSecurityHistoryButton(ActionEvent event) 
    {
        // TODO
    }

    @FXML
    private void onClickVirginizeButton(ActionEvent event) 
    {
        // TODO
    }
    
    public void setStage(Stage stage)
    {
        mStage = stage;
    }
}
