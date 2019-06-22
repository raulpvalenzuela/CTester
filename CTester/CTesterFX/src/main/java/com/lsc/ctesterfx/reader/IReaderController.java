package com.lsc.ctesterfx.reader;

import java.util.List;

/**
 * Interface to access the different readers.
 *
 * @author dma@logossmartcard.com
 */
public interface IReaderController
{
    /**
     * Returns a list containing all the readers' names.
     *
     * @return list with all the readers' names.
     * @throws java.lang.Exception if there's an error accessing the
     */
    public List<String> list() throws Exception;

    /**
     * Selects a specific reader to be used from now on.
     *
     * @param index index of the reader to be selected.
     * @throws java.lang.Exception if there's an error selecting the
     *      reader or releasing the previous one.
     */
    public void select(int index) throws Exception;

    /**
     * Returns the selected reader.
     *
     * @return the selected reader. Null if there is no reader selected.
     */
    public IReader getSelected();
}
