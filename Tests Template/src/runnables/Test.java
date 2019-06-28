package runnables;

import com.lsc.ctesterapi.AbstractTest;
import com.lsc.ctesterapi.logger.Logger;
import com.lsc.ctesterapi.reader.PCSCReaderAccessor;
import com.lsc.ctesterapi.reader.PCSCReaderController;
import com.lsc.ctesterapi.script.ScriptExecutor;

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
            reader = readerController.getSelected();
            reader.connect();

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    @Override
    public boolean run()
    {
        int numErrors = 0;

        try
        {
            ScriptExecutor scriptExecutor = new ScriptExecutor();
            return scriptExecutor.execute("F:\\Coding\\GitHub\\CTester\\perso_visa.txt");

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean tearDown()
    {
        try
        {
            reader.release();

        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
