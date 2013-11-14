package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("../fxml/Intro2.fxml"));
			
			
			Scene scene = new Scene(root,320,600);
			scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();

			//server =  new Server();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
