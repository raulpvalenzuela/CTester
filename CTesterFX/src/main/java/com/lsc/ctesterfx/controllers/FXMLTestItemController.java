package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.test.TestLoader;
import com.lsc.ctesterfx.constants.Constants;
import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.test.TestExecutor;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.util.Pair;

/**
 * FXML Controller class of every test item in the VBox.
 *
 * @author dma@logossmartcard.com
 */
public class FXMLTestItemController implements Initializable
{
    private enum TEST_STATE {
        WAITING,
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
        Pair<Object, Method> result;

        // Update the status image.
        setState(TEST_STATE.COMPILING);
        result = compileTest();

        if (result != null)
        {
            setState(TEST_STATE.COMPILATION_OK);
            setState(TEST_STATE.RUNNING);

            if (runTest(result.getKey(), result.getValue()))
            {
                setState(TEST_STATE.EXECUTION_OK);
            }
            else
            {
                setState(TEST_STATE.EXECUTION_FAILED);
            }
        }
        else
        {
            setState(TEST_STATE.COMPILATION_FAILED);
        }
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event)
    {
        mMainController.removeTestAtIndex(mIndex);
    }

    /**
     * Method that compiles and loads the test.
     *
     * @return Pair containing the object and the method to be invoked.
     */
    private Pair<Object, Method> compileTest()
    {
        Pair<Object, Method> result = null;
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(mTest))
            {
                result = testLoader.load(mTest);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return result;
    }

    /**
     * Method that runs the specified method.
     *
     * @param object: instance of the test.
     * @param method: method to be invoked.
     * @return true if the execution is succesful, false otherwise.
     */
    private boolean runTest(Object object, Method method)
    {
        TestExecutor testExecutor = TestExecutor.newInstance();

        try
        {
            setState(TEST_STATE.RUNNING);

            // Call the 'run' method.
            return testExecutor.run(object, method);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return false;
    }

    /**
     * Method that updates the GUI with the new state.
     *
     * @param state: new state of the test.
     */
    private void setState(TEST_STATE state)
    {
        switch (state) {
            case NOT_COMPILED:
                System.out.println("Not compiled");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                mTestStatusButton.setText("Not compiled");
                break;

            case COMPILING:
                System.out.println("Compiling");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                mTestStatusButton.setText("Compiling");
                break;

            case RUNNING:
                System.out.println("Running");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                mTestStatusButton.setText("Running");
                break;

            case COMPILATION_OK:
                System.out.println("Compilation ok");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #aeffad; -fx-text-fill: black");
                mTestStatusButton.setText("Compiled");
                break;

            case COMPILATION_FAILED:
                System.out.println("Compilation failed");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                mTestStatusButton.setText("Compilation error");
                break;

            case EXECUTION_OK:
                System.out.println("Execution ok");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: #42ff3f; -fx-text-fill: black");
                mTestStatusButton.setText("Succesful");
                break;

            case EXECUTION_FAILED:
                System.out.println("Execution failed");
                mTestStatusButton.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                mTestStatusButton.setText("Failed");
                break;
        }
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
}
