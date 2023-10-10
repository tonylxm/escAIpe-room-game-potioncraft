package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SoundEffects;

public class GameOverController {
  @FXML
  private Pane pane;
  @FXML
  private Button playAgainBtn;
  @FXML
  private Label timerLabel;
  @FXML
  private Rectangle fadeRectangle;

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
    SoundEffects.stop();
    //fade rectangle fades over 1 second
    FadeTransition ft = new FadeTransition(Duration.seconds(1), fadeRectangle);
    ft.setFromValue(0);
    ft.setToValue(1);
    ft.play();
    ft.setOnFinished(event -> {
      try {
        App.setRoot("main_menu");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    //App.setRoot("main_menu");
  }

  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }
}