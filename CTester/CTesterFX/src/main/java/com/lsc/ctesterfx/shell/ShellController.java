package com.lsc.ctesterfx.shell;

import com.lsc.ctesterfx.test.Test;
import com.lsc.ctesterfx.logger.ApplicationLogger;
import com.lsc.ctesterfx.logger.FileLogger;
import com.lsc.ctesterfx.reader.ReaderController;
import com.lsc.ctesterfx.test.TestExecutor;
import com.lsc.ctesterfx.test.TestLoader;
import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterlib.utils.Formatter;
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

    // Application Logger
    private static ApplicationLogger logger;

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
                    logger = ApplicationLogger.newInstance();
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

                    // Initialize stats
                    long startTime  = System.currentTimeMillis();
                    long endTime    = 0;
                    int totalTests  = 0;
                    int numTestsNok = 0;

                    for (File testFile : testFiles)
                    {
                        Test test = new Test(testFile);
                        Pair<Object, List<Method>> compilationResult;

                        // Construct the file logger associated with this test file.
                        FileLogger fileLogger = new FileLogger.Builder()
                                                        .withName(test.getName())
                                                        .in(test.getPath())
                                                        .build();

                        // Initialize and set it as the current file logger.
                        fileLogger.initialize();
                        logger.setFileLogger(fileLogger);

                        totalTests++;

                        logger.logComment("-------------------------------------------- //");
                        logger.logComment("Starting test: " + test.getName()+ "\n");
                        logger.logComment("Compiling");

                        // Compile
                        compilationResult = compile(test, debug);
                        if (compilationResult != null)
                        {
                            // And run
                            if (execute(test, compilationResult, debug))
                            {
                                if (debug) LOGGER.info("Execution of '" + test.getName()+ "' succesful");
                            }
                            else
                            {
                                if (debug) LOGGER.error("Execution of '" + test.getName()+ "' failed");

                                numTestsNok++;
                            }
                        }
                    }

                    endTime = System.currentTimeMillis();

                    logger.logComment("--------------------------------------------- //");
                    logger.logComment("Results:");
                    logger.logComment("");
                    logger.logComment("Tests run: " + totalTests + ", Failures: " + numTestsNok);
                    logger.logComment("");
                    logger.logComment("Time elapsed: " + Formatter.formatInterval(endTime - startTime));
                    logger.logComment("--------------------------------------------- //");
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
     * @return list of files included in the .lst file. Null if the test does not exist.
     */
    private static List<File> getFiles(final String lstPath)
    {
        File lstFile = new File(lstPath);

        List<String> fileNames = LstReader.getFiles(lstFile);
        List<File> testFiles = null;
        // Populate the files list.
        for (String fileName : fileNames)
        {
            if (testFiles == null)
            {
                testFiles = new ArrayList<>();
            }

            testFiles.add(new File(lstFile.getParent() + System.getProperty("file.separator") + fileName));
        }

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
        if (files == null)
        {
            return false;
        }

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

    /**
     * Helper method to compile the test.
     *
     * @return Pair containing the object and the
     *         methods 'setup', 'run' and 'teardown'. Null if there's been an exception.
     */
    private static Pair<Object, List<Method>> compile(Test test, boolean debug)
    {
        TestLoader testLoader = TestLoader.newInstance();
        Pair<Object, List<Method>> result = null;

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

                    logger.logComment("Compilation succesful\n");
                }
            }
            else
            {
                LOGGER.error("Compilation of '" + test.getName()+ "' failed\n");

                logger.logError("Compilation failed\n");
            }

        } catch (Exception ex) {
            LOGGER.error("Exception compiling test (JavaHome not configured in config.xml?)");
            LOGGER.error(ex + "\n");

            logger.logError("Compilation failed");
            logger.logError("Exception: " + ex.toString() + "\n");
        }

        return result;
    }

    /**
     * Executes the test.
     *
     * @param compilationResult: pair containing the object and the list of methods.
     * @return true if succesful.
     */
    private static boolean execute(Test test, Pair<Object, List<Method>> compilationResult, boolean debug)
    {
        TestExecutor testExecutor = TestExecutor.newInstance();

        if (debug) LOGGER.info("Executing '" + test.getName()+ "'");

        // Get the test instance and the methods.
        Object object        = compilationResult.getKey();
        List<Method> methods = compilationResult.getValue();

        for (Method method : methods)
        {
            if (debug) LOGGER.info("Calling '" + method.getName() + "' method");
            logger.logComment("Calling '" + method.getName() + "' method");

            try
            {
                // Call the method.
                if (testExecutor.run(object, method))
                {
                    if (debug) LOGGER.info("'" + method.getName() + "' method passed succesfully");
                    logger.logComment("'" + method.getName() + "' method passed succesfully\n");
                }
                else
                {
                    if (debug) LOGGER.info("'" + method.getName() + "' method failed");
                    logger.logError("'" + method.getName() + "' method failed\n");

                    return false;
                }

            } catch (Exception ex) {
                LOGGER.error("Exception executing test");
                LOGGER.error(ex + "\n");

                logger.logError("Exception executing '" + method.getName() + "' method");
                logger.logError("Exception: " + ex.toString() + "\n");

                return false;
            }
        }

        return true;
    }
}
