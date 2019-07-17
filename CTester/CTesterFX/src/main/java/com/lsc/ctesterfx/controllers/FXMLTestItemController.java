package com.lsc.ctesterfx.controllers;

import com.lsc.ctesterfx.background.MultithreadController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.Context;
import com.lsc.ctesterfx.constants.Tooltips;
import com.lsc.ctesterfx.background.CompilationTask;
import com.lsc.ctesterfx.background.ExecutionTask;
import com.lsc.ctesterfx.background.MultithreadController.TYPE;
import com.lsc.ctesterfx.constants.Colors;
import com.lsc.ctesterfx.constants.Colors.Color;
import com.lsc.ctesterfx.test.Test;
import com.lsc.ctesterfx.test.Test.TEST_STATE;
import com.lsc.ctesterfx.test.TestController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class of every test item in the list.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLTestItemController implements Initializable
{
    // Index of this test.
    private int index;

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
        setupTooltips();
    }

    /**
     * Sets up all the buttons' tooltips.
     */
    private void setupTooltips()
    {
        mRunTestButton.setTooltip(Tooltips.create(Tooltips.RUN_TEST));
        mRemoveTestButton.setTooltip(Tooltips.create(Tooltips.REMOVE_TEST));
    }

    /**
     * Notifies the main controller that a task has started.
     * @param type type of the task (compilation or execution)
     */
    public void notifyStartExecution(TYPE type)
    {
        Context.newInstance()
                .getMainController().notifyStartExecution(type, index);
    }

    /**
     * Notifies the main controller that a task has finished.
     * @param success true if the task was succesful.
     * @param type type of the task (compilation or execution)
     */
    public void notifyFinishedExecution(boolean success, TYPE type)
    {
        Context.newInstance().getMainController()
                .notifyFinishedExecution(success, type);
    }

    @FXML
    private void onClickRunTestButton(ActionEvent event)
    {
        // Clear the output first and restart variables
        Context.newInstance().getMainController()
                .requestClear();

        run();
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event)
    {
        Context.newInstance().getMainController()
                .removeTestAtIndex(index);
    }

    @FXML
    private void onStateChangedSelect(ActionEvent event)
    {
        if (!mTestNameCheckbox.isSelected())
        {
            Context.newInstance().getMainController()
                    .notifyDeselection();
        }
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
     * @param test: reference to the test.
     * @param index: index in the list.
     */
    public void setAttributes(Test test, int index)
    {
        testController = new TestController(test, this);

        this.index     = index;

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
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.GRAY) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Not compiled");
                    break;

                case COMPILING:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.GRAY) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Compiling");
                    break;

                case RUNNING:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.GRAY) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Running");
                    break;

                case COMPILATION_OK:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.LIGHT_GREEN) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Compiled");
                    break;

                case COMPILATION_FAILED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.RED) + "; -fx-text-fill: white");
                    mTestStatusButton.setText(
                            "Compilation error");
                    break;

                case EXECUTION_OK:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.GREEN) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Succesful");
                    break;

                case EXECUTION_FAILED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.RED) + "; -fx-text-fill: white");
                    mTestStatusButton.setText(
                            "Failed");
                    break;

                case STOPPED:
                    mTestStatusButton.setStyle(
                            "-fx-background-radius: 32; -fx-background-color: " + Colors.createAsString(Color.GRAY) + "; -fx-text-fill: black");
                    mTestStatusButton.setText(
                            "Stopped");
                    break;
            }
        }
    }
}
