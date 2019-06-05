package com.lsc.ctesterfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.lsc.ctesterfx.tests.TestLoader;
import com.lsc.ctesterfx.constants.Constants;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private JFXButton mTestStatusIcon;

    private File mTestFile;
    private String mTestname;
    private String mTestPath;

    private TEST_STATE mTestState;

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
        mTestState = TEST_STATE.NOT_COMPILED;

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
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            setState(TEST_STATE.COMPILING);

            testLoader.compile(Paths.get(mTestFile.getParent()), mTestFile);
            result = testLoader.load(mTestFile);

            setState(TEST_STATE.COMPILATION_OK);

        } catch (Exception ex) {
            setState(TEST_STATE.COMPILATION_FAILED);

            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);

            return;
        }

        try
        {
            Method method = result.getValue();
            Object object = result.getKey();

            setState(TEST_STATE.RUNNING);

            method.invoke(object);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            setState(TEST_STATE.EXECUTION_FAILED);

            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        setState(TEST_STATE.EXECUTION_OK);
    }

    @FXML
    private void onClickRemoveTestButton(ActionEvent event)
    {
        mMainController.removeTestAtIndex(mIndex);
    }

    private void setState(TEST_STATE state)
    {
        mTestState = state;

        switch (state) {
            case NOT_COMPILED:
                System.out.println("Not compiled");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: #dddddd; -fx-text-fill: black");
                mTestStatusIcon.setText("Not compiled");
                break;

            case COMPILING:
            case RUNNING:
                System.out.println("Compiling/running");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: #eeeeee; -fx-text-fill: black");
                mTestStatusIcon.setText("Running");
                break;

            case COMPILATION_OK:
                System.out.println("Compilation ok");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: #aeffad; -fx-text-fill: black");
                mTestStatusIcon.setText("Compiled");
                break;

            case COMPILATION_FAILED:
                System.out.println("Compilation failed");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                mTestStatusIcon.setText("Compilation error");
                break;

            case EXECUTION_OK:
                System.out.println("Execution ok");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: #42ff3f; -fx-text-fill: black");
                mTestStatusIcon.setText("Succesful");
                break;

            case EXECUTION_FAILED:
                System.out.println("Execution failed");
                mTestStatusIcon.setStyle("-fx-background-radius: 32; -fx-background-color: red; -fx-text-fill: white");
                mTestStatusIcon.setText("Failed");
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
