package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class GameOverController {
  @FXML private Pane pane;
  @FXML private Button playAgainBtn;

  public void playAgain() throws IOException {
    System.out.println("GAME OVER -> MAIN MENU");
    App.setRoot("main_menu");
    // TODO: RESET ALL SCENES!!!
  }
}
