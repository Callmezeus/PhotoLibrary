package photoapp;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main Stage for Photos app
 * @author Tomer Levy
 */
public class Photos extends Application {
	Stage mainStage;
	@Override
	public void start(Stage primaryStage) throws Exception, ClassNotFoundException {
		mainStage = primaryStage;
		try {			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/LoginPage.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root);
			mainStage.setScene(scene);
			mainStage.setResizable(false);
			mainStage.setTitle("Photo Album");
			mainStage.setResizable(false);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}

}
