package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CauldronRoomController {
  @FXML private Rectangle cauldronRectangle;
  @FXML private Rectangle wizardRectangle;
  @FXML private Polygon rightArrow;
  @FXML private Polygon leftArrow;

  @FXML
  public void clickCauldron(MouseEvent event) {
    System.out.println("cauldron clicked");
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
  }

  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("CAULDRON ROOM > SHELF LEFT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_LEFT));
  }

  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("CAULDRON ROOM > SHELF RIGHT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_RIGHT));
  }
}
