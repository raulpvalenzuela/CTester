package com.lsc.ctesterfx.logger;

import com.lsc.ctesterfx.constants.Colors;
import javafx.application.Platform;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

/**
 * Class that logs the results in the output panel. There's only one
 * panel, so only one instance of Printer is forced with a Singleton.
 *
 * The MainController is responsible of setting this up once.
 *
 * @author dma@logossmartcard.com
 */
public class Printer extends AbstractLogger
{
    private static Printer printer;
    // RichTextArea that will contain the output of the test.
    private InlineCssTextArea outputTextArea;

    // Flag to disable the auto-scroll.
    private boolean autoscroll;

    private Printer()
    {
        autoscroll = true;
    }

    public static synchronized Printer newInstance()
    {
        if (printer == null)
        {
            printer = new Printer();
        }

        return printer;
    }

    /**
     * Sets up the output area.
     *
     * @param container: layout containing the output pane.
     */
    public void setup(BorderPane container)
    {
        outputTextArea = new InlineCssTextArea();

        // Set the common style for output. Monospace and font size.
        outputTextArea.setStyle("-fx-font-family: monospace; -fx-font-size: 10pt;");
        // Not editable.
        outputTextArea.setEditable(false);
        // Transparent background
        outputTextArea.setBackground(Background.EMPTY);
        // No wrapping.
        outputTextArea.setWrapText(false);

        // Container of the output text area. The virtualized container will only render the text visible.
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane<>(outputTextArea);
        VBox.setVgrow(vsPane, Priority.ALWAYS);

        // Force to fill the parent size.
        vsPane.prefWidthProperty().bind(container.prefWidthProperty());
        vsPane.prefHeightProperty().bind(container.prefHeightProperty());

        container.setCenter(vsPane);
    }

    /**
     * Clears the output area.
     */
    public void clear()
    {
        outputTextArea.clear();
    }

    /**
     * Method to enable/disable the auto-scroll.
     *
     * @param autoscroll true to enable the autoscroll, false to disable.
     */
    public void setAutoscroll(boolean autoscroll)
    {
        this.autoscroll = autoscroll;
    }

    @Override
    public void log(String text)
    {
        text = text + "\n";

        _logWithFormat(text, Colors.createAsString(Colors.Color.GRAY));
    }

    @Override
    public void logComment(String text)
    {
        text = COMMENT_HEADER + text + "\n";

        _logWithFormat(text, Colors.createAsString(Colors.Color.DARK_GRAY));
    }

    @Override
    public void logError(String text)
    {
        text = ERROR_HEADER + text + "\n";

        _logWithFormat(text, Colors.createAsString(Colors.Color.RED));
    }

    @Override
    public void logWarning(String text)
    {
        text = WARNING_HEADER + text + "\n";

        _logWithFormat(text, Colors.createAsString(Colors.Color.YELLOW));
    }

    @Override
    public void logDebug(String text)
    {
        text = DEBUG_HEADER + text + "\n";

        _logWithFormat(text, Colors.createAsString(Colors.Color.BLUE));
    }

    @Override
    public void logSuccess(String text)
    {
        text = SUCCESS_HEADER + text + "\n";
        
        _logWithFormat(text, Colors.createAsString(Colors.Color.GREEN));
    }

    /**
     * Logs in the output panel a text with a specific color.
     *
     * @param text: text to be printed.
     * @param color: color to be used.
     */
    private void _logWithFormat(String text, String color)
    {
        LogRunnable logRunnable = new LogRunnable(text, color);

        Platform.runLater(logRunnable);
    }

    /**
     * Runnable needed to print something from a thread.
     */
    private class LogRunnable implements Runnable
    {
        private final String mText;
        private final String mColor;
        private int mIndex;

        public LogRunnable(String text, String color)
        {
            mText = text;
            mColor = color;
        }

        @Override
        public void run()
        {
            // Save the last paragraph's index
            mIndex = outputTextArea.getDocument().getParagraphs().size() - 1;

            outputTextArea.appendText(mText);
            outputTextArea.setStyle(mIndex, "-fx-fill: " + mColor);

            if (autoscroll)
            {
                outputTextArea.scrollYBy(Integer.MAX_VALUE);
            }
        }
    }
}
