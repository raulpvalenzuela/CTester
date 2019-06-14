package com.lsc.ctesterfx.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    // Public constants
    public static final String CTESTER = "CTester";
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
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
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
                writer = new XMLWriter(new FileWriter(configurationFile));
                writer.write(document);

            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);

            } finally {
                if (writer != null)
                {
                    try
                    {
                        writer.close();

                    } catch (IOException ex) {
                        Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
