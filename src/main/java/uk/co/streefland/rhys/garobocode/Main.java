package uk.co.streefland.rhys.garobocode;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

/**
 * The starting point for the JavaFX application
 */
public class Main extends Application {

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String java = System.getProperty("java.specification.version");
        double version = Double.valueOf(java);
        if (version < 1.8) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Java 8 is required to run this application!\nPlease install JRE 8 and try again...",
                            "Error", JOptionPane.ERROR_MESSAGE);
            Platform.exit();
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ui.fxml"));
            root = loader.load();

            UIController controller =
                    loader.getController();
            controller.init();

            Scene scene = new Scene(root, 800, 500);
            stage.setTitle("GARobocode");
            stage.setScene(scene);
            stage.setMinHeight(500);
            stage.setMinWidth(800);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}