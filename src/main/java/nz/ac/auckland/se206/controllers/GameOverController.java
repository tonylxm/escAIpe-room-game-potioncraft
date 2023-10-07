package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;

public class GameOverController {
  @FXML
  private Pane pane;
  @FXML
  private Button playAgainBtn;
  @FXML
  private Label timerLabel;
  @FXML
  private Label initialTimeLabel;

  private CountdownTimer countdownTimer;

  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setYouWinLabel(timerLabel);
    initialTimeLabel.setText("/ " + MainMenuController.getInitialTimeLimit());
  }

  public void onPlayAgain() throws IOException {
    System.out.println("GAME_OVER -> MAIN_MENU");
    App.setRoot("main_menu");
  }
}