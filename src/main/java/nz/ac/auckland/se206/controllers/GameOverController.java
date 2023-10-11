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

/**
 * Controller for the game over screen. This screen is displayed when the
 * player runs out of time or when the player has collected all the items
 * and solved the riddle and brews the potion.
 */
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

  /**
   * Initialising the glow effect and the drag and drop functionality
   * for the key and light images.
   */
  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setGameOverLabel(timerLabel);
  }

  /**
   * When the play again button is pressed, the game state is reset and the
   * main menu is loaded.
   * 
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onPlayAgain() throws IOException {
    System.out.println("GAME_OVER -> MAIN_MENU");
    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    GameState.isChestOpen = false;
    GameState.areItemsCollected = false;
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
  }

  /**
   * When the fade rectangle is clicked, the game state is reset and the
   * main menu is loaded.
   */
  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }
}