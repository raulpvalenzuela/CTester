package com.lsc.ctesterfx.controllers;

import com.lsc.ctesterfx.background.MultithreadController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.lsc.ctesterlib.iso7816.ApduCommand;
import com.lsc.ctesterlib.iso7816.ApduResponse;
import com.lsc.ctesterlib.utils.ApduValidator;
import com.lsc.ctesterlib.utils.Formatter;
import com.lsc.ctesterfx.constants.Animations;
import com.lsc.ctesterlib.constants.Strings;
import com.lsc.ctesterfx.constants.Tooltips;
import com.lsc.ctesterfx.gui.DialogCreator;
import com.lsc.ctesterfx.gui.GUIController;
import com.lsc.ctesterfx.logger.Printer;
import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.reader.ReaderController;
import com.lsc.ctesterlib.virginize.Virginize;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;

/**
 * FXML Controller class of the main window.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLMainController implements Initializable
{
    private static final Logger LOGGER = Logger.getLogger(FXMLMainController.class);

    // List with all the test controllers.
    private List<FXMLTestItemController> testItemControllerList;
    // List with all the reader controllers.
    private List<FXMLReaderItemController> readerItemControllerList;

    // Reference to the window.
    private Stage stage;
    // Reference to the printer.
    private Printer printer;
    // Reference to the Reader controller.
    private ReaderController readerController;
    // Reference to the configuration file.
    private Configuration configuration;

    // Flag to indicate if the commands list is visible.
    private boolean commandsListVisible;
    // Variable to keep track of the tests in execution.
    private AtomicInteger numOfTestsInExecution;
    // Variable to keep track of the test being currently executed.
    private int currentTest;

    @FXML
    private VBox mTestListVBox;
    @FXML
    private VBox mReadersContainer;
    @FXML
    private ScrollPane mTestListScrollPane;
    @FXML
    private BorderPane mOutputContainer;
    @FXML
    private StackPane mFABIconsContainer;
    @FXML
    private StackPane mDialogContainer;
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
    private JFXButton mStopTestsButton;
    @FXML
    private JFXButton mSettingsButton;
    @FXML
    private JFXButton mReadersButton;
    @FXML
    private JFXTextField mCommandTextfield;
    @FXML
    private JFXCheckBox mSelectAllCheckbox;
    @FXML
    private JFXToggleButton mAutoScrollToggleButton;
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
        initialize();
        setupViews();
        setupAnimations();
        setVersion();
    }

    /**
     * Sets up different variables and components.
     */
    private void initialize()
    {
        currentTest              = -1;
        commandsListVisible      = true;
        numOfTestsInExecution    = new AtomicInteger(0);
        testItemControllerList   = new ArrayList<>();
        readerItemControllerList = new ArrayList<>();

        // Initialize the executors.
        MultithreadController.initialize();

        // Printer setup
        printer = Printer.newInstance();
        printer.setup(mOutputContainer);

        // Reader controller
        readerController = ReaderController.newInstance();

        // Set up the GUI controller.
        GUIController.newInstance()
                .setAttributes(mReaderSelectedLabel);

        // It's needed to set the Java Home to the one inside the JDK (~/../Java/jdk1.8.xxx/jre)
        // to be able to compile the tests. When running the .jar by default
        // it will run against the JRE located in ~/../Java/jre1.8.xxx.
        configuration = new Configuration();
        String javaHome = configuration.getValueAsString(Configuration.JAVA_HOME);
        if (javaHome != null && !javaHome.isEmpty())
        {
            LOGGER.info("Setting JAVA_HOME read from config.xml: " + javaHome);
            System.setProperty("java.home", javaHome);
        }
        else
        {
            LOGGER.warn("JAVA_HOME is not properly configured");
        }

        String lastReader = configuration.getValueAsString(Configuration.LAST_READER);
        if ((lastReader != null) && !(lastReader.isEmpty()))
        {
            LOGGER.debug("Loading last session's reader '" + lastReader + "'");

            try
            {
                if (readerController.select(lastReader))
                {
                    // Update the reader label.
                    mReaderSelectedLabel.setText(lastReader);
                }

            } catch (Exception ex) {
                LOGGER.error("Error selecting last session's reader");
                LOGGER.error(ex);
            }
        }
    }

    /**
     * Sets up the different views.
     */
    private void setupViews()
    {
        // Tooltips
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
        mStopTestsButton.setTooltip(Tooltips.create(Tooltips.STOP_TEST));

        mTestListScrollPane.setFitToHeight(true);
        mTestListScrollPane.setFitToWidth(true);

        // Disable the send button.
        mSendButton.setDisable(true);

        mCommandTextfield.textProperty().addListener((observable, oldValue, newValue) ->
        {
            mSendButton.setDisable(!ApduValidator.isValid(newValue));
        });
    }

    /**
     * Sets up all the animations in this window.
     */
    private void setupAnimations()
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
     * Updates the version label reading the pom.xml.
     */
    private void setVersion()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(
                    this.getClass().getClassLoader().getResourceAsStream("application/application.properties"));

            mVersionLabel.setText("v" + properties.getProperty("version"));

        } catch (IOException | NullPointerException ex) {
            LOGGER.error("Exception reading version from application.properties");
            LOGGER.error(ex);
        }
    }

    /**
     * Disables all the buttons.
     */
    private void disableButtons()
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
        mCommandTextfield.setDisable(true);
        testItemControllerList.forEach((controller) ->
        {
            controller.disableButtons();
        });
    }

    /**
     * Enables all the buttons.
     */
    private void enableButtons()
    {
        mAddTestsButton.setDisable(false);
        mCompileTestsButton.setDisable(false);
        mRunTestsButton.setDisable(false);
        mReadersButton.setDisable(false);
        mSettingsButton.setDisable(false);
        mResetButton.setDisable(false);
        mVirginizeButton.setDisable(false);
        mSecurityHistoryButton.setDisable(false);
        mGetProductCodeButton.setDisable(false);
        mBootloaderButton.setDisable(false);
        mFABButton.setDisable(false);
        mCommandTextfield.setDisable(false);
        testItemControllerList.forEach((controller) ->
        {
            controller.enableButtons();
        });

        // Check if the command is valid to enable the send button.
        if (ApduValidator.isValid(mCommandTextfield.getText()))
        {
            mSendButton.setDisable(false);
        }
    }

    @FXML
    private void onClickAddTests(ActionEvent event)
    {
        LOGGER.info("Adding new tests");

        // Read the last path.
        String path = configuration.getValueAsString(Configuration.LAST_PATH);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add Tests");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Java Files", "*.java"));
        if ((path != null) && !(path.isEmpty()))
        {
            File initialDir = new File(path);
            if (initialDir.exists() && initialDir.isDirectory())
            {
                fileChooser.setInitialDirectory(initialDir);
            }
        }

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles != null)
        {
            LOGGER.info("Files added:");

            int index = 0;
            for (File file : selectedFiles)
            {
                LOGGER.info(" - " + file.getAbsolutePath());

                try
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TestItem.fxml"));
                    Parent testItem = (Parent) loader.load();
                    FXMLTestItemController controller = (FXMLTestItemController) loader.getController();
                    controller.setAttributes(file, this, index++);

                    mTestListVBox.getChildren().add(testItem);
                    testItemControllerList.add(controller);

                } catch (IOException ex) {
                    LOGGER.error("Exception loading TestItem.fxml");
                    LOGGER.error(ex);
                }
            }

            // Save the path for the next time.
            Configuration.Editor editor = configuration.getEditor();
            editor.edit(Configuration.LAST_PATH, selectedFiles.get(0).getParent());
            editor.commit();
        }
    }

    @FXML
    private void onClickCompileTests(ActionEvent event)
    {
        LOGGER.info("Compiling tests");

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
        else
        {
            LOGGER.info("List is empty");
        }
    }

    @FXML
    private void onClickRunTests(ActionEvent event)
    {
        LOGGER.info("Running tests");

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
        else
        {
            LOGGER.info("List is empty");
        }
    }

    @FXML
    private void onClickStopTest(ActionEvent event)
    {
        LOGGER.info("Stopping tests");

        // Shutdown the executors.
        MultithreadController.shutdown();
        // Restart them.
        MultithreadController.initialize();

        // Update only the tests that haven't started.
        for (int i = 0; i < testItemControllerList.size(); ++i)
        {
            if (testItemControllerList.get(i).isSelected() && i >= currentTest)
            {
                testItemControllerList.get(i).setState(FXMLTestItemController.TEST_STATE.STOPPED);
            }
        }

        // In case the test has been started individually.
        if (currentTest > -1)
        {
            testItemControllerList.get(currentTest).setState(FXMLTestItemController.TEST_STATE.STOPPED);
        }

        LOGGER.info("Tests stopped");
    }

    @FXML
    private void onClickReaders(ActionEvent event)
    {
        if (mReadersContainer.isVisible())
        {
            LOGGER.debug("Hiding readers list");
            mReadersContainer.setVisible(false);
        }
        else
        {
            LOGGER.info("Loading readers list:");
            // Clear the list
            mReadersContainer.getChildren().clear();

            try
            {
                // Retrieve the readers.
                List<String> readers = readerController.list();

                for (int i = 0; i < readers.size(); ++i)
                {
                    LOGGER.info(" - " + readers.get(i));

                    try
                    {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReaderItem.fxml"));
                        Parent readerItem = (Parent) loader.load();
                        FXMLReaderItemController controller = (FXMLReaderItemController) loader.getController();

                        controller.setAttributes(this
                                , readers.get(i)
                                , i
                                , (readerController.getSelected() != null) && readerController.getSelected().getName().equals(readers.get(i)));

                        mReadersContainer.getChildren().add(readerItem);
                        readerItemControllerList.add(controller);

                        mReadersContainer.setVisible(true);

                    } catch (IOException ex) {
                        LOGGER.error("Exception loading ReaderItem.fxml");
                        LOGGER.error(ex);
                    }
                }

            } catch (Exception ex) {
                LOGGER.error("Exception retrieving readers");
                LOGGER.error(ex);
            }
        }
    }

    @FXML
    private void onMouseClickedReadersContainer(MouseEvent event)
    {
        // If clicked outside, hide the readers.
        mReadersContainer.setVisible(false);
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
    private void onClickSend(ActionEvent event)
    {
        sendCommand();
    }

    @FXML
    private void onClickReset(ActionEvent event)
    {
        reset();
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
        Virginize virginize = new Virginize.Builder().buildFromConfig(Virginize.MODE.ERASE_AND_CONFIGURE);

        if ((virginize != null) & (virginize.getCommand() != null))
        {
            mCommandTextfield.setText(Formatter.fromByteArrayToString(virginize.getCommand()));
        }
    }

    @FXML
    private void onStateChangedSelectAll(ActionEvent event)
    {
        testItemControllerList.forEach((controller) ->
        {
            controller.select(mSelectAllCheckbox.isSelected());
        });
    }

    @FXML
    private void onAutoScrollChange(ActionEvent event)
    {
        printer.setAutoscroll(mAutoScrollToggleButton.isSelected());
    }

    @FXML
    private void onEnterCommand(ActionEvent event)
    {
        if (!mSendButton.isDisable())
        {
            sendCommand();
        }
    }

    /**
     * Send the command entered in the textfield.
     */
    private void sendCommand()
    {
        if (readerController.getSelected() != null)
        {
            String commandEntered = Formatter.separate(mCommandTextfield.getText(), 2);

            LOGGER.info("Command to be sent: " + commandEntered);

            try
            {
                ApduCommand apdu = new ApduCommand(
                        Formatter.fromStringToByteArray(commandEntered));

                printer.log(Strings.COMMAND_HEADER + apdu.toString());

                Pair<Float, ApduResponse> response = readerController.getSelected().transmit(apdu);
                float executionTime = response.getKey();
                ApduResponse apduResponse = response.getValue();

                printer.log(Strings.RESPONSE_HEADER + apduResponse.toString());
                printer.logComment("Time: " + String.format("%.2f", executionTime) + "ms\n");

            } catch (DecoderException ex) {
                LOGGER.error("Invalid command");

                printer.logError("Invalid command\n");

            } catch (Exception ex) {
                LOGGER.error("Exception transmitting command");
                LOGGER.error(ex);

                printer.logError("Exception transmitting command: " + ex.getMessage() + "\n");

                DialogCreator dialog = new DialogCreator(
                          mDialogContainer
                        , Strings.ERROR_TRANSMITTING
                        , "Exception transmitting command: " + ex.getMessage());

                dialog.show();
            }
        }
        else
        {
            printer.logWarning(Strings.NO_READER_SELECTED + "\n");
        }
    }

    /**
     * Resets the card.
     */
    private void reset()
    {
        byte[] atr;
        IReader reader = readerController.getSelected();

        if (reader != null)
        {
            try
            {
                atr = reader.reset();
                String atrStr = Formatter.fromByteArrayToString(atr);

                printer.log(Strings.RESET_CARD);
                printer.logComment(Strings.ATR_HEADER + atrStr + "\n");

            } catch (Exception ex) {
                LOGGER.error("Exception resetting the card");
                LOGGER.error(" - Ex: " + ex);

                printer.logError("Exception resetting the card\n");

                DialogCreator dialog = new DialogCreator(
                              mDialogContainer
                            , Strings.ERROR_RESETTING
                            , "Exception resetting the card: " + ex.getMessage());

                dialog.show();
            }
        }
        else
        {
            printer.logWarning(Strings.NO_READER_SELECTED + "\n");

            DialogCreator dialog = new DialogCreator(
                          mDialogContainer
                        , Strings.NO_READER_SELECTED
                        , "No reader selected, select one from the bottom left corner.");

            dialog.show();
        }
    }

    /**
     * Stores the stage object. This method is called from MainApp so
     * that this controller can have access to different components using the stage object.
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
     * @param index: index of the test to be removed.
     */
    public void removeTestAtIndex(int index)
    {
        LOGGER.debug("Removing test at index: " + index);

        // Remove the test and the controller from the lists.
        mTestListVBox.getChildren().remove(index);
        testItemControllerList.remove(index);

        index = 0;
        for (FXMLTestItemController controller : testItemControllerList)
        {
            controller.setAttributes(index++);
        }

        LOGGER.debug("Test removed");
    }

    /**
     * Increments the number of tests in execution. This method is called from a
     * test controller to notify that a task has started. If it's the first task, disable all the buttons.
     * @param index: index of the test.
     */
    public void notifyStartExecution(int index)
    {
        // Save the index of the test.
        currentTest = index;

        if (numOfTestsInExecution.getAndIncrement() == 0)
        {
            disableButtons();
        }
    }

    /**
     * Decrements the number of tests in execution. This method is called from a
     * test controller to notify that a task has finished. If it's the last task, enable all the buttons.
     */
    public void notifyFinishedExecution()
    {
        // Clear the flag.
        currentTest = -1;

        if (numOfTestsInExecution.decrementAndGet() == 0)
        {
            enableButtons();
        }
    }

    /**
     * When a reader is selected, this method will be called by the corresponding FXMLReaderItemController.
     *
     * @param readerName: name of the selected reader.
     * @param index: index of the reader in the list.
     */
    public void notifyReaderSelected(String readerName, int index)
    {
        LOGGER.info("'" + readerName + "' selected");

        if (readerController.getSelected() == null || !readerController.getSelected().getName().equals(readerName))
        {
            try
            {
                readerController.select(index);

                readerItemControllerList.stream().filter((controller)
                        -> (!controller.getName().equals(readerName))).forEachOrdered((controller) ->
                {
                    controller.selectReader(false);
                });

                // Update the reader label.
                mReaderSelectedLabel.setText(readerName);

            } catch (Exception ex) {
                LOGGER.error("Exception selecting reader '" + readerName + "'");
                LOGGER.error(ex);

                // Hide the list.
                mReadersContainer.setVisible(false);
            }
        }

        // Hide the list.
        mReadersContainer.setVisible(false);
    }

    /**
     * Gets called when a test gets deselected. If this happens the 'Select all'
     * checkbox has to be unchecked.
     */
    public void notifyDeselection()
    {
        mSelectAllCheckbox.setSelected(false);
    }

    /**
     * Restart variables and clear the log. Called when a new test has started.
     */
    public void requestClear()
    {
        numOfTestsInExecution.set(0);

        printer.clear();
    }
}
