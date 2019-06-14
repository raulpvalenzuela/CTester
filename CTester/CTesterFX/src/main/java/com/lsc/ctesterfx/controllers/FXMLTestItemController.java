package com.lsc.ctesterfx.controllers;

import com.lsc.ctesterfx.background.MultithreadController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.constants.Tooltips;
import com.lsc.ctesterfx.background.CompilationTask;
import com.lsc.ctesterfx.background.ExecutionTask;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.test.TestController;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class of every test item in the VBox.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLTestItemController implements Initializable
{
    public enum TEST_STATE {
        QUEUED,
        NOT_COMPILED,
        COMPILING,
        COMPILATION_OK,
        COMPILATION_FAILED,
        RUNNING,
        EXECUTION_OK,
        EXECUTION_FAILED,
        STOPPED
    }

    private int index;

    // Reference to the Main controller.
    private FXMLMainController mainController;
    // Reference to the TestController.
    private TestController testController;

    @FXML
    private JFXCheckBox mTestNameCheckbox;
    @FXML
    private JFXButton mRunTestButton;
    @FXML
    private JFXButton mRemoveTestButton;
    @FXML
    private JFXButton mTestStatusButton;

    /**
     * Initializes the controller class.
     * @param url: unused.
     * @param rb: unused.
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
        mRunTestButton.setTooltip(Tooltips.create(Tooltips.RUN_TEST));
        mRemoveTestButton.setTooltip(Tooltips.create(Tooltips.REMOVE_TEST));
    }

    @FXML
    private void onClickRunTestButton(ActionEvent event)
    {
        // Clear the output first and restart variables
        mainController.requestClear();

        run();
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event)
    {
        mainController.removeTestAtIndex(index);
    }

    /**
     * Method that updates the GUI with the new state.
     *
     * @param state: new state of the test.
     */
    public void setState(TEST_STATE state)
    {
        UpdateStateRunnable updateStateRunnable = new UpdateStateRunnable(state);

        Platform.runLater(updateStateRunnable);
    }

    /**
     * Sets only the index of the test in the list.
     *
     * @param index: index of the test in the list.
     */
    public void setAttributes(int index)
    {
        this.index = index;
    }

    /**
     * Sets the different attributes needed to initialize the test item.
     *
     * @param file: name of the test file.
     * @param mainController: controller that has to be notified if something happens.
     * @param index: index in the list.
     */
    public void setAttributes(File file, FXMLMainController mainController, int index)
    {
        testController      = new TestController(new Test(file), this);

        this.mainController = mainController;
        this.index          = index;

        mTestNameCheckbox.setMnemonicParsing(false);
        mTestNameCheckbox.setText(testController.getTestName());
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

    /**
     * Returns if the checkbox is selected.
     *
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean isSelected()
    {
        return mTestNameCheckbox.isSelected();
    }

    /**
     * Notifies the main controller that a task has started.
     */
    public void notifyStartExecution()
    {
        mainController.notifyStartExecution(index);
    }

    /**
     * Notifies the main controller that a task has finished.
     */
    public void notifyFinishedExecution()
    {
        mainController.notifyFinishedExecution();
    }

    /**
     * Disables all the buttons.
     */
    public void disableButtons()
    {
        mRunTestButton.setDisable(true);
        mRemoveTestButton.setDisable(true);
    }
    /**
     * Enables all the buttons.
     */
    public void enableButtons()
    {
        mRunTestButton.setDisable(false);
        mRemoveTestButton.setDisable(false);
    }

    /**
     * Compiles the test. It will create new CompilationTask to be run in the background
     * to avoid freezing the GUI.
     */
    public void compile()
    {
        // Create an executor and the compilation task.
        CompilationTask compilationTask = new CompilationTask(testController);
        // Start the compilation.
        MultithreadController.execute(compilationTask, MultithreadController.TYPE.COMPILATION);
    }

    /**
     * Compiles and runs the test. It will create new ExecutionTask to be run in the background
     * to avoid freezing the GUI.
     */
    public void run()
    {
        // Do the same process but this time for the execution of the test.
        ExecutionTask executionTask = new ExecutionTask(testController);

        // Start the execution.
        MultithreadController.execute(executionTask, MultithreadController.TYPE.EXECUTION);
    }

    /**
     * Runnable to modify the the test state from a thread. Called from either a CompilationTask or
     * an ExecutionTask.
     */
    private class UpdateStateRunnable implements Runnable
    {
        private final TEST_STATE mState;

        public UpdateStateRunnable(TEST_STATE state)
        {
            mState = state;
        }

        @Override
        public void run()
        {
            switch (mState)
            {
                case QUEUED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Queued");
                    break;

                case NOT_COMPILED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Not compiled");
                    break;

                case COMPILING:
                    notifyStartExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Compiling");
                    break;

                case RUNNING:
                    notifyStartExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Running");
                    break;

                case COMPILATION_OK:
                    notifyFinishedExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #aeffad; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Compiled");
                    break;

                case COMPILATION_FAILED:
                    notifyFinishedExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                    mTestStatusButton.setText(
                            "Compilation error");
                    break;

                case EXECUTION_OK:
                    notifyFinishedExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #42ff3f; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Succesful");
                    break;

                case EXECUTION_FAILED:
                    notifyFinishedExecution();

                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                    mTestStatusButton.setText(
                            "Failed");
                    break;

                case STOPPED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Stopped");
                    break;
            }
        }
    }
}
