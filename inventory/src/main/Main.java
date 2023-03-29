package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the Main class. This gets the program started.
 * FUTURE ENHANCEMENT - The auto-generated IDs could be randomized and checked against a database to ensure they are not already in use. These would be stored indefinitely, so that a new product or part will not have the same ID as an already existing one, even if the old one is no longer part of the current inventory. This would prevent confusion in the future because it prevents an ID being associated with multiple items. This would be especially helpful for larger inventories, and would prevent confusion as inventory items change in the future.
 *
 * RUNTIME ERROR - is documented in the Inventory class in the updateProduct() method comments.
 * @author Derek Brown
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1100, 450));
        primaryStage.show();
    }

    /**
     * This is the main method. This launches the java program.
     * Javadocs folder is located in inventory\javadocs.
     * @param args
     */
    public static void main(String[] args){
        launch(args);
    }
}
