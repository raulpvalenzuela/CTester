package com.lsc.ctesterfx.constants;

/**
 * Class containing all the constants related to the command used to
 * run CTester through the command line.
 *
 * @author dma@logossmartcard.com
 */
public class ShellCommand
{
    // Options
    public static final String VERSION = "v";
    public static final String HELP    = "h";
    public static final String LST     = "l";
    public static final String READERS = "r";
    public static final String DEBUG   = "d";

    // Long options
    public static final String LONG_VERSION = "version";
    public static final String LONG_HELP    = "help";
    public static final String LONG_LST     = "lst";
    public static final String LONG_READERS = "readers";
    public static final String LONG_DEBUG   = "debug";

    // Descriptions
    public static final String DESC_VERSION = "Print the version of the application";
    public static final String DESC_HELP    = null;
    public static final String DESC_LST     = "Path to the .lst file";
    public static final String DESC_READERS = "Get the list of readers connected";
    public static final String DESC_DEBUG   = "Print debug information";

    // Help
    public static final String HELP_CMD    = "CTester";
    public static final String HELP_HEADER = "Execute all the java tests specified in the lst file.";
    public static final String HELP_FOOTER = "For more information, refer to the README.md file";
}
