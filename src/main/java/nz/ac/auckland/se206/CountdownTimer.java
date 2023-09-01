package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CountdownTimer {
  private int initialSeconds;
  private int currentSeconds;
  private Timeline timeline;

  public CountdownTimer(int initialSeconds) {
    this.initialSeconds = initialSeconds;
    this.currentSeconds = initialSeconds;

    setupTimeline();
  }

  private void setupTimeline() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  currentSeconds--;
                  if (currentSeconds <= 0) {
                    handleTimeout();
                    timeline.stop();
                  } else {
                    updateTimerLabel();
                  }
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void start() {
    currentSeconds = initialSeconds;
    updateTimerLabel();
    timeline.play();
  }

  public void stop() {
    timeline.stop();
    currentSeconds = initialSeconds;
    updateTimerLabel();
  }

  private void updateTimerLabel() {
    Parent currentSceneRoot = SceneManager.getUiRoot(SceneManager.getCurrentScene());

    if (currentSceneRoot != null) {
      Label timerLabel =
          (Label) currentSceneRoot.lookup("#timerLabel"); // Assuming the ID is "timerLabel"
      if (timerLabel != null) {
        timerLabel.setText(String.format("Time left: %d seconds", currentSeconds));
      }
    }
  }

  private void handleTimeout() {
    // Perform actions when the timer reaches 0
    // For example, show a message, trigger an event, etc.
  }
}
