package com.lsc.ctesterfx;

import com.lsc.ctesterfx.controllers.FXMLMainController;
import com.lsc.ctesterfx.background.MultithreadController;
import com.lsc.ctesterlib.persistence.Configuration;
import com.lsc.ctesterfx.reader.IReader;
import com.lsc.ctesterfx.reader.ReaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class MainApp extends Application
{
    private static final Logger LOGGER = Logger.getLogger(MainApp.class);

    @Override
    public void start(Stage stage) throws Exception
    {
        LOGGER.info("Starting CTester");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = (Parent) loader.load();
        FXMLMainController controller = (FXMLMainController) loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        stage.setTitle("CTester");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception
    {
        LOGGER.info("Exiting CTester, releasing resources");

        // Shut executors down.
        MultithreadController.shutdown();

        // Save last reader.
        ReaderController readerController = ReaderController.newInstance();
        Configuration.Editor editor = new Configuration().getEditor();

        IReader lastReader = readerController.getSelected();
        if (lastReader != null)
        {
            editor.edit(Configuration.LAST_READER, lastReader.getName());
            editor.commit();
        }
    }
}
