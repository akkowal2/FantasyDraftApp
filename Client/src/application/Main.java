package application;
	
import Data.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		
			FXMLLoader root = new FXMLLoader(getClass().getResource("View.fxml"));
			AnchorPane painin = (AnchorPane) root.load();
			Scene scene = new Scene(painin,999,543);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Draft Lobby");
			primaryStage.setResizable(false);
			
			primaryStage.show();
			
			BackgroundThread thread = new BackgroundThread(root);
			thread.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
