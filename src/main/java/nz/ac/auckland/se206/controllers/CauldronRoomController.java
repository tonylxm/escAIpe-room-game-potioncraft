package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class CauldronRoomController {
  @FXML private Rectangle cauldronRectangle;
  @FXML private Rectangle wizardRectangle;

  @FXML
  public void clickCauldron(MouseEvent event) {
    System.out.println("cauldron clicked");
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
  }
}
