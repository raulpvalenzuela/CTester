package runnables;

import com.lsc.ctesterfx.logger.AbstractLogger;
import com.lsc.ctesterfx.test.AbstractTest;
import com.lsc.ctesterfx.logger.Logger;

public class Test extends AbstractTest
{
    AbstractLogger logger = Logger.newInstance();

    @Override
    public boolean setUp()
    {
        logger.logComment("Personalization completed\n");

        return true;
    }

    @Override
    public boolean run()
    {
        try
        {
            Thread.sleep(500);

            logger.logComment("Virginize command");
            logger.log("I: 80 2E 00 00 00");
            logger.log("O: 6B 02");
            logger.log("");
            logger.logWarning("Status word not expected\n");

            Thread.sleep(700);
            logger.log("I: 80 2E 01 00 00");
            logger.log("O: 90 00");
            logger.log("");

            return true;

        } catch (InterruptedException ex) {
            return false;
        }
    }

    @Override
    public boolean tearDown()
    {
        return true;
    }
}
