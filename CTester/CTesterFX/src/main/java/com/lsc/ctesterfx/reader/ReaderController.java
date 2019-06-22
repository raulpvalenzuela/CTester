package com.lsc.ctesterfx.reader;

import java.util.ArrayList;
import java.util.List;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * Class that manages the readers connected to the computer.
 *
 * @author dma@logossmartard.com
 */
public class ReaderController implements IReaderController
{
    private static ReaderController readerController;
    // Reference to the current reader selected.
    private static IReader currentReader;

    private ReaderController() {}

    public static synchronized ReaderController newInstance()
    {
        if (readerController == null)
        {
            readerController = new ReaderController();
        }

        return readerController;
    }

    @Override
    public List<String> list() throws Exception
    {
        List<String> readers = new ArrayList<>();
        for (CardTerminal cardTerminal : TerminalFactory.getDefault().terminals().list())
        {
            readers.add(cardTerminal.getName());
        }

        return readers;
    }

    @Override
    public void select(int index) throws Exception
    {
        // Release the previous one
        if (currentReader != null)
        {
            currentReader.release();
        }

        currentReader = new PCSCReader.Builder()
                .fromCardTerminal(TerminalFactory.getDefault().terminals().list().get(index))
                .build();
    }

    @Override
    public IReader getSelected()
    {
        return currentReader;
    }
}
