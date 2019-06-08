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
    private static Printer mPrinter;
    // RichTextArea that will contain the output of the test.
    private InlineCssTextArea mOutputTextArea;

    private Printer() {}

    public static synchronized Printer newInstance()
    {
        if (mPrinter == null)
        {
            mPrinter = new Printer();
        }

        return mPrinter;
    }

    /**
     * Sets up the output area.
     *
     * FOR INTERNAL USE ONLY.
     *
     * @param container: layout containing the output pane.
     */
    public void setup(BorderPane container)
    {
        mOutputTextArea = new InlineCssTextArea();

        // Set the common style for output. Monospace and font size.
        mOutputTextArea.setStyle("-fx-font-family: monospace; -fx-font-size: 10pt;");
        // Not editable.
        mOutputTextArea.setEditable(false);
        // Transparent background
        mOutputTextArea.setBackground(Background.EMPTY);
        // No wrapping.
        mOutputTextArea.setWrapText(false);

        // Container of the output text area. The virtualized container will only render the text visible.
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane<>(mOutputTextArea);
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
        mOutputTextArea.clear();
    }

    @Override
    public void log(String text)
    {
        _logWithFormat(text, Colors.createAsString(Colors.Color.GRAY));
    }

    @Override
    public void logComment(String text)
    {
        _logWithFormat(text, Colors.createAsString(Colors.Color.DARK_GRAY));
    }

    @Override
    public void logError(String text)
    {
        _logWithFormat(text, Colors.createAsString(Colors.Color.RED));
    }

    @Override
    public void logWarning(String text)
    {
        _logWithFormat(text, Colors.createAsString(Colors.Color.YELLOW));
    }

    @Override
    public void logDebug(String text)
    {
        _logWithFormat(text, Colors.createAsString(Colors.Color.BLUE));
    }

    @Override
    public void logSuccess(String text)
    {
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
            mIndex = mOutputTextArea.getDocument().getParagraphs().size() - 1;

            mOutputTextArea.appendText(mText);
            mOutputTextArea.setStyle(mIndex, "-fx-fill: " + mColor);
            mOutputTextArea.scrollYBy(1000);
        }
    }
}
