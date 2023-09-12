package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CauldronController {
    @FXML private Label returnLbl;

    @FXML
    private void initialize() {
    
    }

    @FXML 
    private void goBack() {
    System.out.println("CAULDRON -> CAULDRON_ROOM");
    returnLbl.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    }
}
