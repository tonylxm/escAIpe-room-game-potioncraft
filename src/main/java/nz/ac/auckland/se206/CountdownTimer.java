package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CountdownTimer {
  private int initialSeconds;
  private int currentSeconds;
  private Timeline timeline;
  private Label cauldronTimerLabel;
  private Label leftTimerLabel;
  private Label rightTimerLabel;

  public CountdownTimer(int initialSeconds) {
    this.initialSeconds = initialSeconds;
    this.currentSeconds = initialSeconds;

    setupTimeline();
  }

  // Set up the timer to count down every second
  public void setupTimeline() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  currentSeconds--;
                  if (currentSeconds <= -1) {
                    handleTimeout();
                    timeline.stop();
                  } else {
                    updateTimerLabel();
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  // Start the timer
  public void start() {
    currentSeconds = initialSeconds;
    updateTimerLabel();
    timeline.play();
  }

  // Stop the timer
  public void stop() {
    timeline.stop();
    currentSeconds = initialSeconds;
    updateTimerLabel();
  }

  // Logic that occurs every second that updates the timer label. Make sure to name the timer labels
  // timerLabel!!!
  private void updateTimerLabel() {
    Parent currentSceneRoot = SceneManager.getUiRoot(SceneManager.getTimerScene());
    // System.out.println(
    //     "currentSceneRoot = "
    //         + currentSceneRoot
    //         + "  ||  current scene = "
    //         + SceneManager.getTimerScene()
    //         + "  ||  sec = "
    //         + currentSeconds
    //         + "  || cauldron timer = "
    //         + cauldronTimerLabel);

    if (currentSceneRoot != null) {

      Label timerLabel =
          (Label) currentSceneRoot.lookup("#timerLabel"); // Assuming the ID is "timerLabel"

      if (timerLabel != null) {
        timerLabel.setText(String.format("Time left: %d seconds", currentSeconds));

        if (cauldronTimerLabel != null) {
          cauldronTimerLabel.setText(String.format("Time left: %d seconds", currentSeconds));
        }

        if (leftTimerLabel != null) {
          leftTimerLabel.setText(String.format("Time left: %d seconds", currentSeconds));
        }

        if (rightTimerLabel != null) {
          rightTimerLabel.setText(String.format("Time left: %d seconds", currentSeconds));
        }
      }
    }
  }

  // Getters and setters
  public void setCauldronTimerLabel(Label cauldronTimerLabel) {
    this.cauldronTimerLabel = cauldronTimerLabel;
  }

  public void setLeftTimerLabel(Label leftTimerLabel) {
    this.leftTimerLabel = leftTimerLabel;
  }

  public void setRightTimerLabel(Label rightTimerLabel) {
    this.rightTimerLabel = rightTimerLabel;
  }

  // Logic that occurs when the timer reaches 0 - sets the scene to the game over scene
  private void handleTimeout() {
    cauldronTimerLabel.getScene().setRoot(SceneManager.getUiRoot(AppUi.GAME_OVER));
    SceneManager.setTimerScene(AppUi.GAME_OVER);
  }
}
