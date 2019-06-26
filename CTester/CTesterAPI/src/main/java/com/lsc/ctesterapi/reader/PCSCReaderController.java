package com.lsc.ctesterapi.reader;

import com.lsc.ctesterfx.gui.GUIController;
import com.lsc.ctesterfx.reader.IReaderController;
import java.util.List;

/**
 * Interface to access the different readers.
 *
 * @author dma@logossmartcard.com
 */
public class PCSCReaderController implements IReaderController
{
    private static PCSCReaderController readerController;
    private final IReaderController applicationReaderController;
    private final GUIController guiController;

    private PCSCReaderController()
    {
        applicationReaderController = com.lsc.ctesterfx.reader.ReaderController.newInstance();
        guiController               = GUIController.newInstance();
    }

    public static synchronized PCSCReaderController newInstance()
    {
        if (readerController == null)
        {
            readerController = new PCSCReaderController();
        }

        return readerController;
    }

    /**
     * Returns a list containing all the readers' names.
     *
     * @return list with all the readers' names.
     * @throws java.lang.Exception if there's an error accessing the
     */
    @Override
    public List<String> list() throws Exception
    {
        return applicationReaderController.list();
    }

    /**
     * Selects a specific reader to be used from now on.
     *
     * @param index index of the reader to be selected.
     * @return true if succesful.
     * @throws java.lang.Exception if there's an error selecting the
     *      reader or releasing the previous one.
     */
    @Override
    public boolean select(int index) throws Exception
    {
        if (applicationReaderController.select(index))
        {
            guiController.updateReader(applicationReaderController.getSelected().getName());

            return true;
        }

        return false;
    }

    /**
     * Selects a specific reader to be used from now on.
     *
     * @param name name of the reader to be selected.
     * @return true if succesful.
     * @throws java.lang.Exception if there's an error selecting the
     *      reader or releasing the previous one.
     */
    @Override
    public boolean select(String name) throws Exception
    {
        if (applicationReaderController.select(name))
        {
            guiController.updateReader(name);

            return true;
        }

        return false;
    }

    /**
     * Returns the selected reader.
     *
     * @return the selected reader. Null if there is no reader selected.
     */
    @Override
    public PCSCReaderAccessor getSelected()
    {
        return new PCSCReaderAccessor(applicationReaderController.getSelected());
    }
}
