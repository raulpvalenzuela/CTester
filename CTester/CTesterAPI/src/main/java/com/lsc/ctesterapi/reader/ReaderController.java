package com.lsc.ctesterapi.reader;

import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.reader.IReaderController;
import java.util.List;

/**
 * Interface to access the different readers.
 *
 * @author dma@logossmartcard.com
 */
public class ReaderController implements IReaderController
{
    private static ReaderController readerController;
    private final IReaderController applicationReaderController;

    private ReaderController()
    {
        applicationReaderController = com.lsc.ctesterfx.reader.ReaderController.newInstance();
    }

    public static synchronized ReaderController newInstance()
    {
        if (readerController == null)
        {
            readerController = new ReaderController();
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
        return applicationReaderController.select(index);
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
        return applicationReaderController.select(name);
    }

    /**
     * Returns the selected reader.
     *
     * @return the selected reader. Null if there is no reader selected.
     */
    @Override
    public IReader getSelected()
    {
        return new PCSCReaderAccessor(applicationReaderController.getSelected());
    }
}
