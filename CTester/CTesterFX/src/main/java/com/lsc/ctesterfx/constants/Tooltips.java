package com.lsc.ctesterfx.constants;

import javafx.scene.control.Tooltip;

/**
 * Class containing all the tooltips.
 *
 * @author dma@logossmartcard.com
 */
public class Tooltips
{
    public static final String SHOW_MORE     = "Show more";
    public static final String SHOW_LESS     = "Show less";
    public static final String ADD_TESTS     = "Add new tests";
    public static final String COMPILE_TESTS = "Compile selected tests";
    public static final String RUN_TESTS     = "Run selected tests";
    public static final String REMOVE_TESTS  = "Remove selected tests";
    public static final String SETTINGS      = "Settings";
    public static final String READERS       = "Show readers";
    public static final String RUN_TEST      = "Run test";
    public static final String STOP_TEST     = "Stop test";
    public static final String REMOVE_TEST   = "Remove test";
    public static final String SEND          = "Send command";
    public static final String RESET         = "Reset the card";
    public static final String VIRGINIZE     = "Generate Virginize command";
    public static final String SEC_HISTORY   = "Generate Get Security History command";
    public static final String GET_PROD_CODE = "Generate Get Product Code command";
    public static final String BOOTLOADER    = "Generate Back to Bootloader command";

    /**
     * Create a tooltip object from a string.
     * 
     * @param tooltip: Tooltip object with the string received set.
     * @return Tooltip object with the string received set.
     */
    public static Tooltip create(String tooltip)
    {
        return new Tooltip(tooltip);
    }
}
