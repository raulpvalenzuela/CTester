package com.lsc.ctesterapi;

import com.lsc.ctesterfx.reader.IReaderController;
import com.lsc.ctesterfx.reader.IReader;
import java.util.List;

/**
 * Interface to access the different readers.
 *
 * @author dma@logossmartcard.com
 */
public class ReaderController implements IReaderController
{
    private final IReaderController readerController;

    public ReaderController()
    {
        readerController = com.lsc.ctesterfx.reader.ReaderController.newInstance();
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
        return readerController.list();
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
        return readerController.select(index);
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
        return readerController.select(name);
    }

    /**
     * Returns the selected reader.
     *
     * @return the selected reader. Null if there is no reader selected.
     */
    @Override
    public IReader getSelected()
    {
        return readerController.getSelected();
    }
}
