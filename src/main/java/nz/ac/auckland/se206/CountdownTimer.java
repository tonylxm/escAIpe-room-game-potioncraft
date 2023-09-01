package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CountdownTimer {
  private int initialSeconds;
  private int currentSeconds;
  private Label timerLabel;
  private Timeline timeline;

  public CountdownTimer(Label timerLabel, int initialSeconds) {
    this.timerLabel = timerLabel;
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
                  updateTimerLabel();
                  if (currentSeconds <= 0) {
                    handleTimeout();
                    timeline.stop();
                  } else {
                    currentSeconds--;
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
    timerLabel.setText(String.format("Time left: %d seconds", currentSeconds));
  }

  private void handleTimeout() {
    // Perform actions when the timer reaches 0
    // For example, show a message, trigger an event, etc.
  }
}
