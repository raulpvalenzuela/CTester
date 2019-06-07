package com.lsc.ctesterfx.controllers;

import com.lsc.ctesterfx.background.MultithreadController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.constants.Tooltips;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.background.CompilationTask;
import com.lsc.ctesterfx.background.ExecutionTask;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Pair;

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
        EXECUTION_FAILED
    }

    @FXML
    private JFXCheckBox mTestNameCheckbox;

    @FXML
    private JFXButton mRunTestButton;
    @FXML
    private JFXButton mRemoveTestButton;
    @FXML
    private JFXButton mTestStatusButton;

    private Test mTest;
    private int mIndex;

    // Reference to the Main controller.
    private FXMLMainController mMainController;

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
        this.compile(true);
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event)
    {
        mMainController.removeTestAtIndex(mIndex);
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
        mTest = new Test(file);

        mMainController = controller;

        mIndex = index;

        mTestNameCheckbox.setMnemonicParsing(false);
        mTestNameCheckbox.setText(mTest.getName());
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
     * Method that returns if the checkbox is selected.
     *
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean isSelected()
    {
        return mTestNameCheckbox.isSelected();
    }

    public void compile(boolean run)
    {
        // Create an executor and the compilation task.
        CompilationTask compilationTask = new CompilationTask(mTest, this);
        // Set a listener that will be triggered when the task is finished.
        compilationTask.setOnSucceeded((Event e) ->
        {
            // Get the result.
            Pair<Object, Method> result = (Pair<Object, Method>) compilationTask.getValue();

            // Check if it's succesful.
            if (result != null)
            {
                if (run)
                {
                    // Do the same process but this time for the execution of the test.
                    ExecutionTask executionTask = new ExecutionTask(result.getKey(), result.getValue(), this);

                    // Start the execution.
                    MultithreadController.execute(executionTask, MultithreadController.TYPE.EXECUTION);
                }
            }
        });

        // Start the compilation.
        MultithreadController.execute(compilationTask, MultithreadController.TYPE.COMPILATION);
    }

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
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                    mTestStatusButton.setText("Queued");
                    break;

                case NOT_COMPILED:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                    mTestStatusButton.setText("Not compiled");
                    break;

                case COMPILING:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                    mTestStatusButton.setText("Compiling");
                    break;

                case RUNNING:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                    mTestStatusButton.setText("Running");
                    break;

                case COMPILATION_OK:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #aeffad; -fx-text-fill: black");
                    mTestStatusButton.setText("Compiled");
                    break;

                case COMPILATION_FAILED:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                    mTestStatusButton.setText("Compilation error");
                    break;

                case EXECUTION_OK:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #42ff3f; -fx-text-fill: black");
                    mTestStatusButton.setText("Succesful");
                    break;

                case EXECUTION_FAILED:
                    mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                    mTestStatusButton.setText("Failed");
                    break;
            }
        }
    }
}
