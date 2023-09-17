package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CountdownTimer {
  private static int minutes;
  private static int initialSeconds;
  private static int currentSeconds;

  public static void setTimerLimit(String timeLimit) {
    String[] time = timeLimit.split(":");
    minutes = Integer.parseInt(time[0]);
    initialSeconds = Integer.parseInt(time[1]);
    currentSeconds = Integer.parseInt(time[1]);
  }

  private Timeline timeline;
  private Label cauldronTimerLabel;
  private Label leftTimerLabel;
  private Label rightTimerLabel;
  private Label bookTimerLabel;
  private Label brewingLabel;

  public CountdownTimer(String timeLimit) {
    String[] time = timeLimit.split(":");
    minutes = Integer.parseInt(time[0]);
    initialSeconds = Integer.parseInt(time[1]);
    currentSeconds = Integer.parseInt(time[1]);

    setupTimeline();
  }

  /**
   * Set up the timer to count down every second and
   * update the appropriate timer label
   */
  public void setupTimeline() {
    timeline = new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              try {
                oneSecondPassed();
              } catch (IOException e) {
                e.printStackTrace();
              }
              updateTimerLabel();
            }));
    // Updating timer label every second
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  /**
   * Logic that occurs every second. If the timer reaches 0, the game over scene
   * is
   * 
   * @throws IOException
   */
  public void oneSecondPassed() throws IOException {
    if (currentSeconds == 0 && minutes == 0) {
      handleTimeOut();
      timeline.stop();
    } else if (currentSeconds == 0) {
      // Converting minutes to seconds and decrementing minutes appropriately
      minutes--;
      currentSeconds = 60;
    }
    currentSeconds--;
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

  // Logic that occurs every second that updates the timer label. Make sure to
  // name the timer labels
  // timerLabel!!!
  private void updateTimerLabel() {
    Parent currentSceneRoot = SceneManager.getUiRoot(SceneManager.getTimerScene());
    // System.out.println(
    // "currentSceneRoot = "
    // + currentSceneRoot
    // + " || current scene = "
    // + SceneManager.getTimerScene()
    // + " || sec = "
    // + currentSeconds
    // + " || cauldron timer = "
    // + cauldronTimerLabel);

    if (currentSceneRoot != null) {

      Label timerLabel = (Label) currentSceneRoot.lookup("#timerLabel"); 
      // Assuming the ID is "timerLabel"

      if (timerLabel != null) {
        timerLabel.setText(formatTimerText());
      }

      if (cauldronTimerLabel != null) {
        cauldronTimerLabel.setText(formatTimerText());
      }

      if (leftTimerLabel != null) {
        leftTimerLabel.setText(formatTimerText());
      }

      if (rightTimerLabel != null) {
        rightTimerLabel.setText(formatTimerText());
      }

      if (bookTimerLabel != null) {
        bookTimerLabel.setText(formatTimerText());
      }

      if (brewingLabel != null) {
        brewingLabel.setText(formatTimerText());
      }
    }
  }

  public String formatTimerText() {
    if (currentSeconds < 10) {
      return String.format("%d" + ":" + "0" + "%d", minutes, currentSeconds);
    } else {
      return String.format("%d" + ":" + "%d", minutes, currentSeconds);
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

  public void setBookTimerLabel(Label bookTimerLabel) {
    this.bookTimerLabel = bookTimerLabel;
  }

  /**
   * Logic that occurs when the timer reaches 0 - sets the scene to the game over
   * scene
   */
  private void handleTimeOut() throws IOException {
    System.out.println("GAME_OVER");
    App.setRoot("you-lose");
    // Using App.setRoot() so that game over occurs in all scenes
  }

  public void setBrewingLabel(Label timerLabel) {
    this.brewingLabel = timerLabel;
  }
}
