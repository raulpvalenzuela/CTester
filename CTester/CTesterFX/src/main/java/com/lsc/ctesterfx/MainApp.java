package com.lsc.ctesterfx;

import com.lsc.ctesterfx.controllers.FXMLMainController;
import com.lsc.ctesterfx.background.MultithreadController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class MainApp extends Application
{
    private static final Logger logger = Logger.getLogger(MainApp.class);

    @Override
    public void start(Stage stage) throws Exception
    {
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
        MultithreadController.shutdown();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
