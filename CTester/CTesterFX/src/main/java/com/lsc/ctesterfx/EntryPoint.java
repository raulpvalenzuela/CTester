package com.lsc.ctesterfx;

import com.lsc.ctesterfx.constants.ShellCommand;
import com.lsc.ctesterfx.reader.ReaderController;
import com.lsc.ctesterfx.shell.ShellController;
import com.lsc.ctesterlib.persistence.Configuration;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

/**
 * Entry point of the application.
 *
 * @author dma@logossmartcard.com
 */
public class EntryPoint
{
    private static final Logger LOGGER = Logger.getLogger(EntryPoint.class);

    /**
     * Entry point of the application. The application has two modes, one to be run
     * through the command line and the other using the GUI.
     *
     * @param args arguments received from the command line.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            MainApp.launch(MainApp.class, args);
        }
        else
        {
            CommandLineParser parser = new DefaultParser();
            Options options = new Options();

            options.addOption(ShellCommand.LST, ShellCommand.LONG_LST, true, ShellCommand.DESC_LST);
            options.addOption(ShellCommand.HELP, ShellCommand.LONG_HELP, false, ShellCommand.DESC_HELP);
            options.addOption(ShellCommand.VERSION, ShellCommand.LONG_VERSION, false, ShellCommand.DESC_VERSION);
            options.addOption(ShellCommand.VERBOSE, ShellCommand.LONG_VERBOSE, false, ShellCommand.DESC_VERBOSE);
            options.addOption(ShellCommand.READERS, ShellCommand.LONG_READERS, false, ShellCommand.DESC_READERS);

            try
            {
                CommandLine command = parser.parse(options, args);

                // Print the version if it's the only option
                if (isVersionRequired(command))
                {
                    printVersion();
                }
                // Print the help
                else if (isHelpRequired(command))
                {
                    printHelp(options);
                }
                // Print the readers
                else if (isReadersRequired(command))
                {
                    printReaders();
                }
                // Run the lst file.
                else if (isValidCommand(command))
                {
                    ShellController.run(
                            command.getOptionValue(ShellCommand.LST), command.hasOption(ShellCommand.VERBOSE));
                }
                // Unknown command
                else
                {
                    printHelp(options);
                }

            } catch (ParseException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    /**
     * Analyzes the command and returns if the help needs to be printed.
     *
     * @param command command entered.
     * @return true if the help is required.
     */
    private static boolean isHelpRequired(final CommandLine command)
    {
        return ((command.getOptions().length == 1) && command.hasOption(ShellCommand.HELP));
    }

    /**
     * Analyzes the command and returns if the version needs to be printed.
     *
     * @param command command entered.
     * @return true if the version is required.
     */
    private static boolean isVersionRequired(final CommandLine command)
    {
        return ((command.getOptions().length == 1) && command.hasOption(ShellCommand.VERSION));
    }

    /**
     * Analyzes the command and returns if the list of readers needs to be printed.
     *
     * @param command command entered.
     * @return true if it's the readers have to be printed.
     */
    private static boolean isReadersRequired(final CommandLine command)
    {
        return ((command.getOptions().length == 1) && command.hasOption(ShellCommand.READERS));
    }

    /**
     * Analyzes the command and returns if it's a valid command (it contains
     * the "-l" option and/or the "-v" option).
     *
     * @param command command entered.
     * @return true if it's a valid command.
     */
    private static boolean isValidCommand(final CommandLine command)
    {
        return (((command.getOptions().length == 2) && command.hasOption(ShellCommand.LST) && command.hasOption(ShellCommand.VERBOSE)) ||
                ((command.getOptions().length == 1) && command.hasOption(ShellCommand.LST)));
    }

    /**
     * Prints the help.
     *
     * @param options: command options.
     */
    private static void printHelp(final Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                  ShellCommand.HELP_CMD
                , ShellCommand.HELP_HEADER
                , options
                , ShellCommand.HELP_FOOTER
                , true);
    }

    /**
     * Prints the version.
     */
    private static void printVersion()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(
                    MainApp.class.getClassLoader().getResourceAsStream("application/application.properties"));

            System.out.println("CTester version " + properties.getProperty("version"));

        } catch (IOException | NullPointerException ex) {
            LOGGER.error("Exception reading version from application.properties");
            LOGGER.error(ex);
        }
    }

    /**
     * Prints the readers connected.
     */
    private static void printReaders()
    {
        try
        {
            // Get the selected reader if there is any.
            Configuration config = new Configuration();
            String selectedReader = config.getValueAsString(Configuration.SHELL, Configuration.READER);

            ReaderController readerController = ReaderController.newInstance();

            List<String> readers = readerController.list();

            System.out.println("\nReaders connected:");
            readers.forEach(reader ->
            {
                if (reader.equals(selectedReader))
                {
                    System.out.println("  * " + reader);
                }
                else
                {
                    System.out.println("    " + reader);
                }
            });

        } catch (Exception ex) {
            LOGGER.error("Error retrieving readers");
        }
    }
}
