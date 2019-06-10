package com.lsc.ctesterfx.persistence;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * Class to handle the configuration file.
 *
 * @author dma@logossmartard.com
 */
public class Configuration
{
    // Public constants
    public static final String JAVA_HOME = "JavaHome";

    // Private constants
    private static final String CONFIG_PATH =
        System.getProperty("user.dir") + System.getProperty("file.separator") + "Config" + System.getProperty("file.separator");
    private static final String CONFIG_NAME = "config.xml";

    // Reference to the configuration file.
    private final File configurationFile;

    /**
     * Class used to modify the configuration file safely.
     */
    public class Editor
    {
        public void edit()
        {

        }

        public String commit()
        {
            return null;
        }
    }

    /**
     * Constructor.
     */
    public Configuration()
    {
        configurationFile = new File(CONFIG_PATH + CONFIG_NAME);
    }

    /**
     * Returns a string of the specified key.
     *
     * @param key: is the key te be searched of.
     * @return the value pointed by key as a string.
     */
    public String getValueAsString(String key)
    {
        try
        {
            SAXReader reader = new SAXReader();
            Document document = reader.read(configurationFile);

            return document.selectSingleNode("//" + key).getStringValue();

        } catch (DocumentException ex) {
            // TODO
        }

        return null;
    }
}
