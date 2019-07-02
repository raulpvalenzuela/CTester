package com.lsc.ctesterlib.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Class to handle the configuration file.
 *
 * @author dma@logossmartard.com
 */
public class Configuration
{
    private static final Logger LOGGER = Logger.getLogger(Configuration.class);

    // Config.xml fields
    public static final String CTESTER = "CTester";
        public static final String JAVA_HOME   = "JavaHome";
        public static final String LAST_READER = "Reader";
        public static final String LAST_PATH   = "TestsPath";

    public static final String VIRGINIZE = "Virginize";
        public static final String MODES = "Modes";
            public static final String MODE = "Mode";
                public static final String KEY        = "Key";
                public static final String PARAMETERS = "Parameters";
                    public static final String PARAMETER  = "Parameter";

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
        private Document document;

        /**
         * Constructor.
         */
        public Editor()
        {
            try
            {
                SAXReader reader = new SAXReader();
                document = reader.read(configurationFile);

            } catch (DocumentException ex) {
                LOGGER.error("Exception opening config.xml'");
                LOGGER.error(ex);
            }
        }

        /**
         * Updates the node with a new value.
         *
         * @param key: node to be updated.
         * @param newValue: new value to be set.
         */
        public void edit(String key, String newValue)
        {
            LOGGER.debug("Updating '" + newValue + "' into" + "'" + key + "'");

            document.selectSingleNode("//" + key).setText(newValue);
        }

        /**
         * Commits all the changes done to the document.
         */
        public void commit()
        {
            XMLWriter writer = null;

            try
            {
                LOGGER.debug("Committing changes in config.xml");
                writer = new XMLWriter(new FileWriter(configurationFile));
                writer.write(document);
                LOGGER.debug("Changes saved correctly");

            } catch (UnsupportedEncodingException ex) {
                LOGGER.error("Exception writing config.xml'");
                LOGGER.error(ex);

            } catch (IOException ex) {
                LOGGER.error("Exception writing config.xml'");
                LOGGER.error(ex);

            } finally {
                if (writer != null)
                {
                    try
                    {
                        writer.close();

                    } catch (IOException ex) {
                        LOGGER.error("Exception closing config.xml'");
                        LOGGER.error(ex);
                    }
                }
            }
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
     * Creates and returns a new configuration editor.
     *
     * @return reference to a new configuration editor.
     */
    public Editor getEditor()
    {
        return new Editor();
    }

    /**
     * Returns a string of the specified key.
     *
     * @param keys list of keys to find the element.
     * @return the value pointed by key as a string.
     */
    public String getValueAsString(String... keys)
    {
        StringBuilder xpath = new StringBuilder();
        for (String key : keys)
        {
            xpath.append((xpath.length() == 0) ? "//" : "/");
            xpath.append(key);
        }

        try
        {
            SAXReader reader = new SAXReader();
            Document document = reader.read(configurationFile);

            return document.selectSingleNode(xpath.toString()).getStringValue();

        } catch (DocumentException ex) {
            LOGGER.error("Exception reading config.xml'");
            LOGGER.error(ex);
        }

        return null;
    }
}
