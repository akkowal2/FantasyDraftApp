package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DraftView extends Application{
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Draft.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root,960,610);
			scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setResizable(false);
			primaryStage.show();
			System.out.println("Hello");
			
			while(true){
				Thread.sleep(10000);
				System.out.println("update");
				DraftController controller = (DraftController)loader.getController();
				controller.rotateDraftOrder();
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
