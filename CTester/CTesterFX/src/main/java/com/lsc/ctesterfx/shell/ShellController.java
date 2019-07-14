package com.lsc.ctesterfx.shell;

import com.lsc.ctesterfx.dao.Test;
import com.lsc.ctesterfx.logger.ApplicationLogger;
import com.lsc.ctesterfx.logger.FileLogger;
import com.lsc.ctesterfx.reader.ReaderController;
import com.lsc.ctesterfx.test.TestExecutor;
import com.lsc.ctesterfx.test.TestLoader;
import com.lsc.ctesterlib.persistence.Configuration;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.apache.log4j.Logger;

/**
 * Class that handles the execution of the tests
 * when the application has been executed from the command line.
 *
 * @author dma@logossmartcard.com
 */
public class ShellController
{
    private static final Logger LOGGER = Logger.getLogger(ShellController.class);

    // Constants
    private static final String LST_EXTENSION = ".lst";

    /**
     * Compiles and runs the java tests specified in the .lst file.
     *
     * @param lstPath path to the .lst file.
     * @param debug flag to indicate if debug information has to be logged.
     */
    public static void run(String lstPath, boolean debug)
    {
        if (debug) LOGGER.info("Executing tests from the .lst file");

        // Check and decode .lst
        if (lstPath.endsWith(LST_EXTENSION))
        {
            // Check reader selected.
            String reader = new Configuration().getValueAsString(
                                    Configuration.SHELL, Configuration.READER);

            if (reader != null)
            {
                try
                {
                    ReaderController readerController = ReaderController.newInstance();
                    readerController.select(reader);

                } catch (Exception ex) {
                    LOGGER.error("Error selecting reader (" + reader + ")");
                    LOGGER.error(" - " + ex.getMessage());

                    return;
                }

                // Read the .lst file and get the files in it.
                List<File> testFiles = getFiles(lstPath);

                // Check that the files are correct.
                // - List not empty.
                // - All files exist.
                // - All files are .java.
                if (checkList(testFiles))
                {
                    // Create a new ApplicationLogger and set the mode.
                    ApplicationLogger logger = ApplicationLogger.newInstance();
                    logger.setMode(ApplicationLogger.MODE.COMMAND_LINE_ONLY);

                    // It's needed to set the Java Home to the one inside the JDK (~/../Java/jdk1.8.xxx/jre)
                    // to be able to compile the tests. When running the .jar by default
                    // it will run against the JRE located in ~/../Java/jre1.8.xxx.
                    Configuration configuration = new Configuration();
                    String javaHome = configuration.getValueAsString(Configuration.JAVA_HOME);
                    if (javaHome != null && !javaHome.isEmpty())
                    {
                        if (debug) LOGGER.info("Setting JAVA_HOME read from config.xml: " + javaHome);
                        System.setProperty("java.home", javaHome);
                    }
                    else
                    {
                        LOGGER.warn("JAVA_HOME is not properly configured");
                    }

                    TestLoader testLoader = TestLoader.newInstance();
                    TestExecutor testExecutor = TestExecutor.newInstance();

                    testFiles.forEach((testFile) ->
                    {
                        Test test = new Test(testFile);
                        Pair<Object, List<Method>> result = null;

                        // Construct the file logger associated with this test file.
                        FileLogger fileLogger = new FileLogger.Builder()
                                                        .withName(test.getName())
                                                        .in(test.getPath())
                                                        .build();

                        // Initialize and set it as the current file logger.
                        fileLogger.initialize();
                        logger.setFileLogger(fileLogger);

                        logger.logComment("--- Compiling " + test.getName() + " ---");

                        if (debug) LOGGER.info("Compiling " + test.getName());
                        try
                        {
                            // Compile and load the test class.
                            if (testLoader.compile(test))
                            {
                                if (debug) LOGGER.info("Compilation of '" + test.getName()+ "' succesful");

                                if ((result = testLoader.load(test)) == null)
                                {
                                    LOGGER.error("Loading of '" + test.getName()+ "' failed");
                                }
                                else
                                {
                                    if (debug) LOGGER.info("Loading of '" + test.getName()+ "' succesful");

                                    logger.logComment("--- Compilation of " + test.getName()+ " succesful ---\n");
                                }
                            }
                            else
                            {
                                LOGGER.error("Compilation of '" + test.getName()+ "' failed\n");

                                logger.logError("--- Compilation of " + test.getName()+ " failed ---\n");
                            }

                        } catch (Exception ex) {
                            LOGGER.error("Exception compiling test (JavaHome not configured in config.xml?)");
                            LOGGER.error(ex + "\n");

                            logger.logError("Compilation of " + test.getName()+ " failed");
                            logger.logError("Exception: " + ex.toString() + "\n");
                        }

                        // Execution starts here
                        if (result != null)
                        {
                            if (debug) LOGGER.info("Executing '" + test.getName()+ "'");

                            // Get the test instance and the methods.
                            Object object        = result.getKey();
                            List<Method> methods = result.getValue();

                            methods.stream().map((method) ->
                            {
                                if (debug) LOGGER.info("Calling '" + method.getName() + "' method");
                                logger.logComment("--- Calling '" + method.getName() + "' method ---");

                                return method;

                            }).forEach((method) ->
                            {
                                try
                                {
                                    // Call the method.
                                    if (testExecutor.run(object, method))
                                    {
                                        if (debug) LOGGER.info("'" + method.getName() + "' method passed succesfully");

                                        logger.logComment("--- '" + method.getName() + "' method passed succesfully ---\n");
                                    }
                                    else
                                    {
                                        if (debug) LOGGER.info("'" + method.getName() + "' method failed");

                                        logger.logError("--- '" + method.getName() + "' method failed ---\n");
                                    }

                                } catch (Exception ex) {
                                    LOGGER.error("Exception executing test");
                                    LOGGER.error(ex + "\n");

                                    logger.logError("Exception executing '" + method.getName() + "' method");
                                    logger.logError("Exception: " + ex.toString() + "\n");
                                }
                            });

                            if (debug) LOGGER.info("Execution of '" + test.getName()+ "' succesful");
                        }
                    });
                }
            }
            else
            {
                LOGGER.error("No reader selected, set it in 'config.xml' and re-run\n");
            }
        }
        else
        {
            LOGGER.error("The file containing the java test should be a .lst file\n");
        }
    }

    /**
     * Reads the .lst file and returns the list of files included.
     *
     * @param lstPath: path to the .lst file.
     * @return list of files included in the .lst file.
     */
    private static List<File> getFiles(final String lstPath)
    {
        File lstFile = new File(lstPath);

        List<String> fileNames = LstReader.getFiles(lstFile);
        List<File> testFiles = new ArrayList<>();
        // Populate the files list.
        fileNames.forEach((fileName) ->
        {
            testFiles.add(new File(lstFile.getParent() + System.getProperty("file.separator") + fileName));
        });

        return testFiles;
    }

    /**
     * Checks that the file list is correct.
     *
     * @param files list of files to be checked.
     * @return true if the file list is correct.
     */
    private static boolean checkList(final List<File> files)
    {
        // Check it contains something
        if (files.isEmpty())
        {
            LOGGER.error("The LST file is empty");
            return false;
        }

        // Check that all the tests exist
        for (File file : files)
        {
            if (!file.exists())
            {
                LOGGER.error("Test file (" + file.getAbsolutePath() + ") does not exist");
                return false;
            }

            if (!file.getName().endsWith(".java"))
            {
                LOGGER.error("Test file (" + file.getAbsolutePath() + ") is not a java file");
                return false;
            }
        }

        return true;
    }
}
