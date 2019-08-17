package runnables;

import com.lsc.ctesterapi.AbstractTest;
import com.lsc.ctesterapi.logger.Logger;
import com.lsc.ctesterapi.reader.PCSCReaderAccessor;
import com.lsc.ctesterapi.reader.PCSCReaderController;

public class Test extends AbstractTest
{
    // To log messages in the screen and in the log file.
    private final Logger logger = Logger.newInstance();
    // To manage the readers.
    private final PCSCReaderController readerController = PCSCReaderController.newInstance();
    // To communicate with the reader.
    private PCSCReaderAccessor reader;

    @Override
    public boolean setUp()
    {
        try
        {
            // Personalise

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    @Override
    public boolean run()
    {
        boolean success = false;

        try
        {
            // Test logic goes here, remember to call `Thread.currentThread().isInterrupted()`
            // from time to time to check if the test has been stopped.

            return success;

        } catch (Exception ex) {
            return success;
        }
    }

    @Override
    public boolean tearDown()
    {
        try
        {
            // Release resources, delete applets, etc.

        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
