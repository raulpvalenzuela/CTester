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
import javafx.util.Pair;

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
    private static final String WILDCARD        = "*";

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
            logger.logComment("Executing script: " + script.getName() + "\n");

            BufferedReader br = new BufferedReader(new FileReader(script));

            String line;
            String command = null;
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
                    command = line.replace(COMMAND_HEADER, "").trim();
                }
                // If it's a response, send the command and check the response.
                else if (line.startsWith(RESPONSE_HEADER))
                {
                    try
                    {
                        // Create the APDU command.
                        ApduCommand apduCommand = new ApduCommand(
                                Formatter.fromStringToByteArray(command));

                        // Transmit the command and get the response.
                        ApduResponse apduResponse = reader.transmit(apduCommand);

                        byte[] swReceived   = apduResponse.getSW();
                        byte[] dataReceived = apduResponse.getData();

                        Pair<String, String> expectedResponse = parseExpectedResponse(line.replace(RESPONSE_HEADER, ""));
                        String swExpected = expectedResponse.getKey();
                        String dataExpected = expectedResponse.getValue();

                        // Check the SW.
                        if ((swExpected != null) && !swExpected.isEmpty())
                        {
                            if (!apduResponse.checkSW(Formatter.fromStringToByteArray(swExpected)))
                            {
                                logger.logError("Status Word incorrect:");
                                logger.logError(" - SW Expected: " + Formatter.separate(swExpected, 2));
                                logger.logError(" - SW Received: " + Formatter.fromByteArrayToString(swReceived));

                                return false;
                            }
                        }

                        // Data received.
                        if (dataReceived.length > 0)
                        {
                            // None expected
                            if (dataExpected == null)
                            {
                                logger.logError("Data received but none expected");
                                logger.logError(" - " + line.replace(RESPONSE_HEADER, ""));

                                return false;
                            }

                            // Data expected and has to be checked.
                            if (!dataExpected.isEmpty())
                            {
                                if (!apduResponse.checkData(dataExpected))
                                {
                                    logger.logError("Data incorrect:");
                                    logger.logError(" - Data Expected: " + Formatter.separate(dataExpected, 2));
                                    logger.logError(" - Data Received: " + Formatter.fromByteArrayToString(dataReceived));

                                    return false;
                                }
                            }
                        }

                    } catch (Exception ex) {
                        logger.logError("Exception transmitting command");
                        logger.logError(" - Ex: " + ex.getMessage());

                        return false;
                    }
                }
                // If it's a reset.
                else if (line.startsWith(RESET_HEADER))
                {
                    reader.reset();
                }
            }

            return true;
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

    /**
     * Parses the expected response int two strings containing the SW and the data.
     * If no data is expected the string will be null. If data is expected but should
     * not be checked, the string will be empty.
     *
     * @param line string containing the response.
     * @return pair containing the SW and the response data.
     */
    private Pair<String, String> parseExpectedResponse(String line)
    {
        String response = line.replace(" ", "").trim();

        // If it's empty or it only contains a star "*".
        if (response.isEmpty() || response.equals(WILDCARD))
        {
            return new Pair<>("", "");
        }

        // If contains a star and SW.
        if (response.contains(WILDCARD))
        {
            response = response.replace(WILDCARD, "");

            return new Pair<>(response, "");
        }

        // If it contains only the SW.
        if (response.length() == 4)
        {
            return new Pair<>(response, null);
        }

        // If we reach this point, SW and response is expected.
        return new Pair<>(
              response.substring(response.length() - 4)
            , response.substring(0, response.length() - 4)
        );
    }
}
