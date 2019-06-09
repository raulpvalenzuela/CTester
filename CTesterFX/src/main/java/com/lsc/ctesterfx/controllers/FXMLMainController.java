package com.lsc.ctesterfx.controllers;

import com.lsc.ctesterfx.background.MultithreadController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.lsc.ctesterfx.constants.Animations;
import com.lsc.ctesterfx.constants.Tooltips;
import com.lsc.ctesterfx.logger.Printer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class of the main window.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLMainController implements Initializable
{
    // List with all the test controllers.
    private List<FXMLTestItemController> testItemControllerList;

    // Reference to the window.
    private Stage stage;
    // Reference to the printer.
    private Printer printer;

    // Flag to indicate if the commands list is visible.
    private boolean commandsListVisible;
    // Variable to keep track of the tests in execution.
    private AtomicInteger numOfTestsInExecution;

    @FXML
    private VBox mTestListVBox;
    @FXML
    private VBox mSnackbarContainer;
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
    private JFXButton mReadersButton;
    @FXML
    private JFXTextField mCommandTextfield;
    @FXML
    private JFXCheckBox mSelectAllCheckbox;
    @FXML
    private ImageView mFABPlusIcon;
    @FXML
    private Label mReaderSelectedLabel;
    @FXML
    private Label mVersionLabel;

    // Add all the animations to be run when the FAB is clicked.
    private FadeTransition[] fadeInAnimationsList;
    private FadeTransition[] fadeOutAnimationsList;
    private RotateTransition rotateTransition;
    private FadeTransition fadeInAnimation;
    private FadeTransition fadeOutAnimation;

    /**
     * Initializes the controller class.
     *
     * @param url: unused.
     * @param rb: unused.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        _initialize();
        _setupPrinter();
        _setupTooltips();
        _setupAnimations();
    }

    /**
     * Sets up different variables.
     */
    private void _initialize()
    {
        commandsListVisible   = true;
        numOfTestsInExecution = new AtomicInteger(0);
        testItemControllerList = new ArrayList<>();

        MultithreadController.initialize();
    }

    /**
     * Sets up the printer.
     */
    private void _setupPrinter()
    {
        printer = Printer.newInstance();

        printer.setup(mOutputContainer);
    }

    /**
     * Sets up all the buttons' tooltips.
     */
    private void _setupTooltips()
    {
        mAddTestsButton.setTooltip(Tooltips.create(Tooltips.ADD_TESTS));
        mCompileTestsButton.setTooltip(Tooltips.create(Tooltips.COMPILE_TESTS));
        mRunTestsButton.setTooltip(Tooltips.create(Tooltips.RUN_TESTS));
        mSettingsButton.setTooltip(Tooltips.create(Tooltips.SETTINGS));
        mFABButton.setTooltip(Tooltips.create(Tooltips.SHOW_MORE));
        mSendButton.setTooltip(Tooltips.create(Tooltips.SEND));
        mResetButton.setTooltip(Tooltips.create(Tooltips.RESET));
        mVirginizeButton.setTooltip(Tooltips.create(Tooltips.VIRGINIZE));
        mBootloaderButton.setTooltip(Tooltips.create(Tooltips.BOOTLOADER));
        mSecurityHistoryButton.setTooltip(Tooltips.create(Tooltips.SEC_HISTORY));
        mGetProductCodeButton.setTooltip(Tooltips.create(Tooltips.GET_PROD_CODE));
        mReadersButton.setTooltip(Tooltips.create(Tooltips.READERS));
    }

    /**
     * Sets up all the animations in this window.
     */
    private void _setupAnimations()
    {
        fadeInAnimationsList = new FadeTransition[] {
              new FadeTransition(Duration.millis(Animations.FADE_IN_DURATION), mVirginizeButton)
            , new FadeTransition(Duration.millis(Animations.FADE_IN_DURATION), mSecurityHistoryButton)
            , new FadeTransition(Duration.millis(Animations.FADE_IN_DURATION), mGetProductCodeButton)
            , new FadeTransition(Duration.millis(Animations.FADE_IN_DURATION), mBootloaderButton)
        };

        fadeOutAnimationsList = new FadeTransition[] {
              new FadeTransition(Duration.millis(Animations.FADE_OUT_DURATION), mVirginizeButton)
            , new FadeTransition(Duration.millis(Animations.FADE_OUT_DURATION), mSecurityHistoryButton)
            , new FadeTransition(Duration.millis(Animations.FADE_OUT_DURATION), mGetProductCodeButton)
            , new FadeTransition(Duration.millis(Animations.FADE_OUT_DURATION), mBootloaderButton)
        };

        int delay = 0;
        for (int i = 0; i < fadeInAnimationsList.length; ++i)
        {
            fadeInAnimationsList[i].setFromValue(0.0f);
            fadeInAnimationsList[i].setToValue(1.0f);
            fadeInAnimationsList[i].setDelay(Duration.millis(delay));
            fadeInAnimationsList[i].setInterpolator(Interpolator.EASE_BOTH);

            delay += Animations.FAST_DELAY;
        }

        delay = 0;
        for (int i = fadeOutAnimationsList.length - 1; i >= 0; --i)
        {
            fadeOutAnimationsList[i].setFromValue(1.0f);
            fadeOutAnimationsList[i].setToValue(0.0f);
            fadeOutAnimationsList[i].setDelay(Duration.millis(delay));
            fadeOutAnimationsList[i].setInterpolator(Interpolator.EASE_BOTH);

            // Hide the node once it's finished so that it cannot get clicked.
            final int index = i;
            fadeOutAnimationsList[i].setOnFinished((e) -> {
                fadeOutAnimationsList[index].getNode().setVisible(false);
            });

            delay += Animations.FAST_DELAY;
        }

        rotateTransition = new RotateTransition(Duration.millis(Animations.SLOW_DELAY), mFABIconsContainer);
        rotateTransition.setByAngle(180);

        fadeInAnimation  = new FadeTransition(Duration.millis(Animations.SLOW_DELAY), mFABPlusIcon);
        fadeOutAnimation = new FadeTransition(Duration.millis(Animations.SLOW_DELAY), mFABPlusIcon);

        fadeInAnimation.setFromValue(0.0f);
        fadeInAnimation.setToValue(1.0f);
        fadeOutAnimation.setFromValue(1.0f);
        fadeOutAnimation.setToValue(0.0f);
    }

    /**
     * Disables all the buttons.
     */
    private void _disableButtons()
    {
        mAddTestsButton.setDisable(true);
        mCompileTestsButton.setDisable(true);
        mRunTestsButton.setDisable(true);
        mReadersButton.setDisable(true);
        mSettingsButton.setDisable(true);
        mSendButton.setDisable(true);
        mResetButton.setDisable(true);
        mVirginizeButton.setDisable(true);
        mSecurityHistoryButton.setDisable(true);
        mGetProductCodeButton.setDisable(true);
        mBootloaderButton.setDisable(true);
        mFABButton.setDisable(true);
        testItemControllerList.forEach((controller) ->
        {
            controller.disableButtons();
        });
    }

    /**
     * Enables all the buttons.
     */
    private void _enableButtons()
    {
        mAddTestsButton.setDisable(false);
        mCompileTestsButton.setDisable(false);
        mRunTestsButton.setDisable(false);
        mReadersButton.setDisable(false);
        mSettingsButton.setDisable(false);
        mSendButton.setDisable(false);
        mResetButton.setDisable(false);
        mVirginizeButton.setDisable(false);
        mSecurityHistoryButton.setDisable(false);
        mGetProductCodeButton.setDisable(false);
        mBootloaderButton.setDisable(false);
        mFABButton.setDisable(false);
        testItemControllerList.forEach((controller) ->
        {
            controller.enableButtons();
        });
    }

    @FXML
    private void onClickAddTests(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Java Files", "*.java"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if (selectedFiles != null)
        {
            int index = 0;
            for (File file : selectedFiles)
            {
                try
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TestItem.fxml"));
                    Parent testItem = (Parent) loader.load();
                    FXMLTestItemController controller = (FXMLTestItemController) loader.getController();
                    controller.setAttributes(file, this, index++);

                    mTestListVBox.getChildren().add(testItem);
                    testItemControllerList.add(controller);

                } catch (IOException ex) {
                    // TODO
                }
            }
        }
    }

    @FXML
    private void onClickCompileTests(ActionEvent event)
    {
        if (!testItemControllerList.isEmpty())
        {
            // Clear the output panel.
            printer.clear();

            // First initialize the state of each test.
            testItemControllerList.stream().filter((testItem) -> (testItem.isSelected())).forEachOrdered((testItem) ->
            {
                testItem.setState(FXMLTestItemController.TEST_STATE.QUEUED);
            });

            // Compile only the ones checked.
            testItemControllerList.stream().filter((testItem) -> (testItem.isSelected())).forEachOrdered((testItem) ->
            {
                testItem.compile();
            });
        }
    }

    @FXML
    private void onClickRunTests(ActionEvent event)
    {
        if (!testItemControllerList.isEmpty())
        {
            // Clear the output panel.
            printer.clear();

            // First initialize the state of each test.
            testItemControllerList.stream().filter((testItem) -> (testItem.isSelected())).forEachOrdered((testItem) ->
            {
                testItem.setState(FXMLTestItemController.TEST_STATE.QUEUED);
            });

            // Compile only the ones checked.
            testItemControllerList.stream().filter((testItem) -> (testItem.isSelected())).forEachOrdered((testItem) ->
            {
                testItem.run();
            });
        }
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
        rotateTransition.play();

        if (commandsListVisible)
        {
            for (int i = 0; i < fadeOutAnimationsList.length; ++i)
            {
                fadeOutAnimationsList[i].play();
            }

            // Minus to plus transition.
            fadeInAnimation.play();
            // Change the tooltip.
            mFABButton.setTooltip(Tooltips.create(Tooltips.SHOW_MORE));
        }
        else
        {
            for (int i = 0; i < fadeInAnimationsList.length; ++i)
            {
                // Make the node visible again.
                fadeInAnimationsList[i].getNode().setVisible(true);
                fadeInAnimationsList[i].play();
            }

            // Plus to minus transition.
            fadeOutAnimation.play();
            // Change the tooltip.
            mFABButton.setTooltip(Tooltips.create(Tooltips.SHOW_LESS));
        }

        commandsListVisible = !commandsListVisible;
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

    @FXML
    private void onStateChangedSelectAll(ActionEvent event)
    {
        testItemControllerList.forEach((controller) -> {
            controller.select(mSelectAllCheckbox.isSelected());
        });
    }

    /**
     * Stores the stage object. This method is called from MainApp so
     * that this controller can have access to different components.
     *
     * @param stage stage object passed from the main window.
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Removes a specific test at the given index. This method is called
     * from within a test controller to notify that the method has
     * been removed.
     *
     * @param index: index of the test.
     */
    public void removeTestAtIndex(int index)
    {
        // Remove the test and the controller from the lists.
        mTestListVBox.getChildren().remove(index);
        testItemControllerList.remove(index);

        index = 0;
        for (FXMLTestItemController controller : testItemControllerList)
        {
            controller.setAttributes(index++);
        }
    }

    /**
     * Increments the number of tests in execution. This method is called from a
     * test controller to notify that a task has started. If it's the first task, disable all the buttons.
     */
    public void notifyStartExecution()
    {
        if (numOfTestsInExecution.getAndIncrement() == 0)
        {
            _disableButtons();
        }
    }

    /**
     * Decrements the number of tests in execution. This method is called from a
     * test controller to notify that a task has finished. If it's the last task, enable all the buttons.
     */
    public void notifyFinishedExecution()
    {
        if (numOfTestsInExecution.decrementAndGet() == 0)
        {
            _enableButtons();
        }
    }

    /**
     * Clears the output panel. Called when a new test has started.
     */
    public void requestClear()
    {
        printer.clear();
    }
}
