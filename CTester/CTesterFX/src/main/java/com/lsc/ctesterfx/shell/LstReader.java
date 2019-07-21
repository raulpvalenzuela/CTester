package com.lsc.ctesterfx.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class that manages .lst files.
 *
 * @author dma@logossmartcard.com
 */
public class LstReader
{
    private static final Logger LOGGER = Logger.getLogger(LstReader.class);

    // Constants
    private static final String LST_EXTENSION = ".lst";
    private static final String COMMENT_HEADER = ";";

    /**
     * Returns true if the file is an .lst file.
     *
     * @param path path to the file.
     * @return true if the file is an .lst file.
     */
    public static boolean isLstFile(final String path)
    {
        return path.endsWith(LST_EXTENSION);
    }

    /**
     * Reads an lst and returns the list of lines with no comments.
     *
     * @param lstFile: lst file to be read.
     * @return list of strings with no comments.
     */
    public static List<String> getFiles(final File lstFile)
    {
        List<String> lines = new ArrayList<>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(lstFile));

            String line;
            while ((line = br.readLine()) != null)
            {
                if (!line.startsWith(COMMENT_HEADER) && !line.isEmpty())
                {
                    lines.add(line.trim());
                }
            }

        } catch (FileNotFoundException ex) {
            LOGGER.error("LST file not found");

        } catch (IOException ex) {
            LOGGER.error("Exception reading LST file");
            LOGGER.error(" - Exception: " + ex.getMessage());
        }

        return lines;
    }
}
