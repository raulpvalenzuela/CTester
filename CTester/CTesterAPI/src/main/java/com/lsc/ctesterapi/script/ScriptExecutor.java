package com.lsc.ctesterapi.script;

import com.lsc.ctesterapi.logger.Logger;
import com.lsc.ctesterapi.reader.PCSCReaderAccessor;
import com.lsc.ctesterapi.reader.PCSCReaderController;
import com.lsc.ctesterfx.script.IScriptExecutor;
import com.lsc.ctesterlib.iso7816.ApduCommand;
import com.lsc.ctesterlib.iso7816.ApduResponse;
import com.lsc.ctesterlib.utils.Formatter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Interface to interact with a IO script file.
 *
 * @author dma@logossmartcard.com
 */
public class ScriptExecutor implements IScriptExecutor
{
    private static final String COMMENT_HEADER  = "// ";
    private static final String COMMAND_HEADER  = "I: ";
    private static final String RESPONSE_HEADER = "O: ";
    private static final String RESET_HEADER    = "Reset";

    private final Logger logger;
    private final PCSCReaderAccessor reader;

    public ScriptExecutor()
    {
        logger = Logger.newInstance();
        reader = PCSCReaderController.newInstance().getSelected();
    }

    /**
     * Executes the script given.
     *
     * @param script script file to be executed.
     * @return true if the execution is succesful.
     * @throws Exception if there is some communication error.
     */
    @Override
    public boolean execute(File script) throws Exception
    {
        // First, check that exists.
        if (script.exists())
        {
            logger.logComment("Executing script: " + script.getName());

            BufferedReader br = new BufferedReader(new FileReader(script));

            String line;
            String command = null;
            byte[] response;
            while ((line = br.readLine()) != null)
            {
                // If it's a comment.
                if (line.startsWith(COMMENT_HEADER))
                {
                    logger.logComment(line.replace(COMMENT_HEADER, ""));
                }
                // If it's a command, we'll send it once we read the response.
                else if (line.startsWith(COMMAND_HEADER))
                {
                    command = line.replace(COMMAND_HEADER, "");
                }
                // If it's a response, send the command and check the response.
                else if (line.startsWith(RESPONSE_HEADER))
                {
                    response = Formatter.fromStringToByteArray(line.replace(RESET_HEADER, ""));

                    // Create the APDU command.
                    ApduCommand apduCommand = new ApduCommand(
                            Formatter.fromStringToByteArray(command));

                    try
                    {
                        // Transmit the command and get the response.
                        ApduResponse apduResponse = reader.transmit(apduCommand);

                        byte[] sw   = apduResponse.getSW();
                        byte[] data = apduResponse.getData();

                        if (!line.contains("*"))
                        {
                            // If it's a case 1/3 command.
                            if (response.length == 2)
                            {
                                if (!apduResponse.checkSW(sw))
                                {
                                    return false;
                                }
                            }
                            // If it's a case 2/4 command.
                            else
                            {
                                if (!apduResponse.checkData(data))
                                {
                                    return false;
                                }
                            }
                        }

                        logger.log(COMMAND_HEADER + command);
                        logger.log(RESPONSE_HEADER + apduResponse.toString() + "\n");

                    } catch (Exception ex) {
                        logger.logError("Exception transmitting command");
                        logger.logError("Exception: " + ex.getMessage());

                        return false;
                    }
                }
                // If it's a reset.
                else if (line.startsWith(RESET_HEADER))
                {
                    logger.log(line);
                    byte[] atr = reader.reset();
                    logger.logComment(Formatter.fromByteArrayToString(atr) + "\n");
                }
            }
        }
        else
        {
            logger.logWarning("Script file does not exist\n");
        }

        return false;
    }

    @Override
    public boolean execute(String script) throws Exception
    {
        return execute(new File(script));
    }
}
