package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ShelfLeftController {
    @FXML private Rectangle itemOneRect;
    @FXML private Rectangle itemTwoRect;
    @FXML private Rectangle itemThreeRect;
    @FXML private Rectangle itemFourRect;
    @FXML private Rectangle itemFiveRect;
    @FXML private Polygon rightShpe;
    @FXML private Button rightBtn;

    @FXML 
    public void goRight(MouseEvent event){
        System.out.println("SHELF LEFT > CAULDRON ROOM");
        Scene currentScene = rightBtn.getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    }

    @FXML
    public void itemOneGlow() {
        itemOneRect.setStrokeWidth(5);
    }

    @FXML
    public void itemTwoGlow() {
        itemTwoRect.setStrokeWidth(5);
    }

    @FXML
    public void itemThreeGlow() {
        itemThreeRect.setStrokeWidth(5);
    }

    @FXML
    public void itemFourGlow() {
        itemFourRect.setStrokeWidth(5);
    }

    @FXML
    public void itemFiveGlow() {
        itemFiveRect.setStrokeWidth(5);
    }
}
