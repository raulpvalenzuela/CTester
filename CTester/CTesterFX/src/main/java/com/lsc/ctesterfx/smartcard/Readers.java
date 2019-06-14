package com.lsc.ctesterfx.smartcard;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author dma@logossmartard.com
 */
public class Readers
{
    private static int currentReader;

    public static void list()
    {
        try
        {
            for (CardTerminal cardTerminal : TerminalFactory.getDefault().terminals().list())
            {
                System.out.println(cardTerminal.getName());
            }

        } catch (CardException ex) {
            Logger.getLogger(Readers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void select(int index)
    {
        currentReader = index;
    }
}
