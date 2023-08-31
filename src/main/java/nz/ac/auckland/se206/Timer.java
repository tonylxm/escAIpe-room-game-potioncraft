package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer {
  private Label timerLabel;
  private int secondsElapsed;
  private Timeline timeline;

  public Timer(Label timerLabel) {
    this.timerLabel = timerLabel;
    this.secondsElapsed = 0;
    createTimeline();
  }

  private void createTimeline() {
    timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  secondsElapsed++;
                  updateTimerLabel();
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void start() {
    timeline.play();
  }

  public void pause() {
    timeline.pause();
  }

  public void reset() {
    secondsElapsed = 0;
    updateTimerLabel();
  }

  private void updateTimerLabel() {
    int minutes = secondsElapsed / 60;
    int seconds = secondsElapsed % 60;
    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }
}
