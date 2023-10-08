package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;

public class GameOverController {
  @FXML
  private Pane pane;
  @FXML
  private Button playAgainBtn;
  @FXML
  private Label timerLabel;

  private CountdownTimer countdownTimer;

  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setGameOverLabel(timerLabel);
  }

  public void onPlayAgain() throws IOException {
    System.out.println("GAME_OVER -> MAIN_MENU");
    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    GameState.isChestOpen = false;
    GameState.areItemsCollected = false;
    App.setRoot("main_menu");
  }
}