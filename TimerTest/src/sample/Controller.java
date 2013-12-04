package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextField clockTextArea;
    @FXML GridPane gridPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        clockTextArea.setText("166");
        gridPane.add(clockTextArea);
    }
}
