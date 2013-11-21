package application;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class DraftView extends Application{
	FXMLLoader loader;
	Scene scene;
    String leagueName;
    String leaguePassword;
    DraftController controller;
    ArrayList<Team> teams;

    public DraftView(String leagueName, String leaguePassword){


        this.leagueName=leagueName;
        this.leaguePassword=leaguePassword;
    }

	@Override
	public void start(final Stage primaryStage) {

		try {

            loader = new FXMLLoader(getClass().getResource("../fxml/Draft.fxml"));
            loader.setController(new DraftController(leagueName,leaguePassword));

			BorderPane root = (BorderPane)loader.load();
			
			scene = new Scene(root,960,610);

			scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
            primaryStage.setScene(scene);



		
			final InvalidationListener resizeColumns = new InvalidationListener(){
				@Override
				public void invalidated(Observable arg0) {
                    /**
                     * Resizing thread to resize columns of tables actively
                     */
                    new Thread() {
                        // runnable for that thread

                        public void run() {
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    // what will be ran in gui thread


                                    Double width =scene.getWidth();
                                    controller = loader.getController();
                                    //TableView<Player> teamTable =controller.getTeamTable();
                                    //centerColumns(width, teamTable);


                                    TableView<Player>  top10Table = controller.getTop10Table();
                                    ArrayList<TableView<Player>> teamTables = controller.getTeamTables();

                                    for(int i=0 ; i<teamTables.size();i++){
                                        Double tableWidth= teamTables.get(i).getWidth();
                                        centerColumns(tableWidth+tableWidth*.06,teamTables.get(i));
                                    }
                                    AnchorPane anchor = controller.getAnchorPane();
                                    centerColumns(anchor.getWidth()+anchor.getWidth()*.04,top10Table);

                                }


                                private void centerColumns(Double width, TableView<Player> teamTable) {
                                    ObservableList<TableColumn<Player, ?>> columnList = teamTable.getColumns();
                                    for (int i=0 ; i<columnList.size(); i++){
                                        columnList.get(i).setPrefWidth((width-17)/teamTable.getColumns().size());
                                    }
                                }
                            });
                        }
                    }.start();
				}
				
			};
			
			scene.widthProperty().addListener(resizeColumns);
			scene.heightProperty().addListener(resizeColumns);


            /**
             *Polling thread for draft process
             */
			new Thread() {

                // runnable for that thread
                public void run() {

                   // draftSim();






                }
                
            }.start();





			primaryStage.show();

	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

    private void draftSim() {
        while(true){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // update ProgressIndicator on FX thread
            Platform.runLater(new Runnable() {
                public void run() {
                    controller = loader.getController();

                    controller.rotateDraftOrder();
                    controller.positionalBreakdown(controller.getDraftQ().peek());

                    Random r = new Random();
                    int random = r.nextInt(300);

                    System.out.println("random::" + random);
                    Player draftedPlayer = controller.getPlayersList().get(random);
                    controller.getDraftQ().peek().addPlayer(draftedPlayer);
                    controller.teamTableTab();
                    System.out.println("removed?" + controller.removePlayer(draftedPlayer.getName()));

                }
            });

        }
    }

}
	