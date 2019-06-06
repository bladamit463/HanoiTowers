/*
 * Default scene initializer, nothing was changed. Except for this header. 
 * 
 */
package hanoitowers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Alexandre Perreault
 */
public class HanoiTowers extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	
	Scene scene = new Scene(root);
	
	stage.setScene(scene);
	stage.show();
	stage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	launch(args);
    }
    
}