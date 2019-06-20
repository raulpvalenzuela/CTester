package runnables;

import com.lsc.ctesterapi.AbstractTest;
import com.lsc.ctesterapi.Logger;
import com.lsc.ctesterapi.ReaderAccessor;

public class Test extends AbstractTest
{
    // To log messages in the screen and in the log file.
    Logger logger = Logger.newInstance();
    // To communicate with the card. 
    ReaderAccessor readerAccessor = new ReaderAccessor();

    @Override
    public boolean setUp()
    {
        return true;
    }

    @Override
    public boolean run()
    {
        try
        {
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean tearDown()
    {
        return true;
    }
}
