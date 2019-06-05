package com.lsc.ctesterfx.printer;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;

/**
 *
 * @author danim
 */
public class Printer 
{
    // RichTextArea that will contain the output of the test.
    private final InlineCssTextArea mOutputTextArea = new InlineCssTextArea();
    
    private static Printer mPrinter;
    
    private Printer() {}
    
    public static synchronized Printer newInstance()
    {
        if (mPrinter == null)
        {
            System.out.println("New printer");
            
            mPrinter = new Printer();
        }
        
        return mPrinter;
    }
    
    public void setup(BorderPane container)
    {
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
    
    public void log(String message)
    {
        mOutputTextArea.appendText(message);
        mOutputTextArea.setStyle(0, "-fx-fill: white");
    }
}
