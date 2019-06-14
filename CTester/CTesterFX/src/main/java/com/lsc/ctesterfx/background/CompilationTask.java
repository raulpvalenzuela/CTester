package com.lsc.ctesterfx.background;

import com.lsc.ctesterfx.controllers.FXMLTestItemController;
import com.lsc.ctesterfx.test.TestController;
import com.lsc.ctesterfx.test.TestLoader;
import java.lang.reflect.Method;
import java.util.List;
import javafx.concurrent.Task;
import javafx.util.Pair;

/**
 * Class to run the compilation process in the background.
 *
 * @author dma@logossmartcard.com
 */
public class CompilationTask extends Task
{
    // Instance to the test controller.
    private final TestController testController;

    public CompilationTask(TestController testController)
    {
        this.testController = testController;
    }

    @Override
    protected Object call() throws Exception
    {
        return _compileTest();
    }

    /**
     * Compiles and loads the test, returning the object and a list of methods.
     *
     * @return Pair containing the object and the methods to be invoked.
     */
    private Pair<Object, List<Method>> _compileTest()
    {
        testController.notifyStartTest();

        testController.getLogger().logComment("Compiling " + testController.getTestName() + "\n");
        testController.setState(FXMLTestItemController.TEST_STATE.COMPILING);

        Pair<Object, List<Method>> result = null;
        TestLoader testLoader = TestLoader.newInstance();

        try
        {
            // Compile and load the test class.
            if (testLoader.compile(testController.getTest()))
            {
                result = testLoader.load(testController.getTest());

                testController.getLogger().logComment("Compilation of " + testController.getTestName() + " succesful!\n");
                testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_OK);
            }
            else
            {
                testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed\n");
                testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
            }

        } catch (Exception ex) {
            testController.getLogger().logError("Compilation of " + testController.getTestName() + " failed");
            testController.getLogger().logError("Exception: " + ex.toString() + "\n");
            testController.setState(FXMLTestItemController.TEST_STATE.COMPILATION_FAILED);
        }

        testController.notifyFinishStart();

        return result;
    }
}
