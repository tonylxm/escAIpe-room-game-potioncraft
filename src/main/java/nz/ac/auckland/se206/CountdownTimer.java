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

  /**
   * Sets the timer limit based on the time limit string.
   * 
   * @param timeLimit
   */
  public static void setTimerLimit(String timeLimit) {
    String[] time = timeLimit.split(":");
    minutes = Integer.parseInt(time[0]);
    initialSeconds = Integer.parseInt(time[1]);
    currentSeconds = Integer.parseInt(time[1]);
  }

  private Timeline timeline;
  private Label cauldronTimerLabel;
  private Label cauldronHintLabel;
  private Label libraryTimerLabel;
  private Label libraryHintLabel;
  private Label rightTimerLabel;
  private Label rightHintLabel;
  private Label bookTimerLabel;
  private Label brewingLabel;
  private Label gameOverLabel;
  private Label chestTimerLabel;

  /**
   * Constructor for the timer.
   * 
   * @param timeLimit
   */
  public CountdownTimer(String timeLimit) {
    String[] time = timeLimit.split(":");
    minutes = Integer.parseInt(time[0]);
    initialSeconds = Integer.parseInt(time[1]);
    currentSeconds = Integer.parseInt(time[1]);
    setTimeline();
  }

  /**
   * Set up the timer to count down every second and
   * update the appropriate timer label.
   */
  public void setTimeline() {
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
   * is.
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
    updateTimerLabel();
  }

  /**
   * Logic that occurs every second that updates the timer label.
   */
  private void updateTimerLabel() {
    Parent currentSceneRoot = SceneManager.getUiRoot(SceneManager.getTimerScene());

    if (currentSceneRoot != null) {

      Label timerLabel = (Label) currentSceneRoot.lookup("#timerLabel"); 
      // Assuming the ID is "timerLabel"

      if (timerLabel != null) {
        timerLabel.setText(formatTimerText());
      }

      if (cauldronTimerLabel != null) {
        cauldronTimerLabel.setText(formatTimerText());
      }

      if (libraryTimerLabel != null) {
        libraryTimerLabel.setText(formatTimerText());
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

      if (gameOverLabel != null) {
        gameOverLabel.setText(formatTimerText());
      }
      
      if (chestTimerLabel != null) {
        chestTimerLabel.setText(formatTimerText());
      }
    }
  }

  /**
   * Formats the timer text to be displayed in the format mm:ss.
   * 
   * @return
   */
  public String formatTimerText() {
    // In order to properly format the time no matter how long is left
    if (currentSeconds < 10) {
      // Accounting for the minutes when the seconds are less than 10
      if (minutes == 0 && currentSeconds % 2 == 1) {
        setTimerRed(cauldronTimerLabel, libraryTimerLabel, 
            rightTimerLabel, bookTimerLabel, brewingLabel);
      } else if (minutes == 0 && currentSeconds % 2 == 0) {
        setTimerBlack(cauldronTimerLabel, libraryTimerLabel, 
            rightTimerLabel, bookTimerLabel, brewingLabel);
      }
      return String.format("%d" + ":" + "0" + "%d", minutes, currentSeconds);
    } else {
      return String.format("%d" + ":" + "%d", minutes, currentSeconds);
    }
  }

  /**
   * Sets the timer label to red on certain timer values.
   * 
   * @param timer1
   * @param timer2
   * @param timer3
   * @param timer4
   * @param timer5
   */
  public void setTimerRed(Label timer1, Label timer2, Label timer3, 
      Label timer4, Label timer5) {
    timer1.setStyle("-fx-text-fill: red");
    timer2.setStyle("-fx-text-fill: red");
    timer3.setStyle("-fx-text-fill: red");
    timer4.setStyle("-fx-text-fill: red");
    timer5.setStyle("-fx-text-fill: red");
  }

  /**
   * Sets the timer label to black.
   * 
   * @param timer1
   * @param timer2
   * @param timer3
   * @param timer4
   * @param timer5
   */
  public void setTimerBlack(Label timer1, Label timer2, Label timer3, 
      Label timer4, Label timer5) {
    timer1.setStyle("-fx-text-fill: black");
    timer2.setStyle("-fx-text-fill: black");
    timer3.setStyle("-fx-text-fill: black");
    timer4.setStyle("-fx-text-fill: black");
    timer5.setStyle("-fx-text-fill: black");
  }

  /**
   * Sets the timer label for the cauldron room.
   * 
   * @param cauldronTimerLabel
   */
  public void setCauldronTimerLabel(Label cauldronTimerLabel) {
    this.cauldronTimerLabel = cauldronTimerLabel;
  }

  /**
   * Sets the hint label for the cauldron room.
   * 
   * @param cauldronHintLabel
   */
  public void setCauldronHintLabel(Label cauldronHintLabel) {
    this.cauldronHintLabel = cauldronHintLabel;
  }

  /**
   * Sets the timer label for the library room.
   * 
   * @param libraryTimerLabel
   */
  public void setLibraryTimerLabel(Label libraryTimerLabel) {
    this.libraryTimerLabel = libraryTimerLabel;
  }

  /**
   * Sets the hint label for the library room.
   * 
   * @param libraryHintLabel
   */
  public void setLibraryHintLabel(Label libraryHintLabel) {
    this.libraryHintLabel = libraryHintLabel;
  }

  /**
   * Sets the timer label for the treasure room.
   * 
   * @param rightTimerLabel
   */
  public void setTreasureTimerLabel(Label rightTimerLabel) {
    this.rightTimerLabel = rightTimerLabel;
  }

  /**
   * Sets the hint label for the treasure room.
   * 
   * @param rightHintLabel
   */
  public void setTreasureHintLabel(Label rightHintLabel) {
    this.rightHintLabel = rightHintLabel;
  }

  /**
   * Sets the timer label for the book view.
   * 
   * @param bookTimerLabel
   */
  public void setBookTimerLabel(Label bookTimerLabel) {
    this.bookTimerLabel = bookTimerLabel;
  }

  /**
   * Sets the timer label for the brewing view.
   * 
   * @param timerLabel
   */
  public void setBrewingLabel(Label timerLabel) {
    this.brewingLabel = timerLabel;
  }

  /**
   * Sets the timer label for the game over view.
   * 
   * @param gameOverLabel
   */
  public void setGameOverLabel(Label gameOverLabel) {
    this.gameOverLabel = gameOverLabel;
  }
  
  /**
   * Sets the timer label for the chest view.
   * 
   * @param chestTimerLabel
   */
  public void setChestLabel(Label chestTimerLabel) {
    this.chestTimerLabel = chestTimerLabel;
  }

  /**
   * Logic that occurs when the timer reaches 0 - sets the scene to the game over
   * scene.
   */
  private void handleTimeOut() throws IOException {
    System.out.println("GAME_OVER");
    // Using App.setRoot() so that game over occurs in all scenes
    App.setRoot("you-lose");
  }

  /**
   * Updates the hint label to display the number of hints left.
   * @param hints
   */
  public void updateHintLabel(int hints) {
    String text;
    // Setting the appropriate text for the hint label based on the
    // number of hjints remaining
    if (hints < 0) {
      text = "∞ hints";
    } else if (hints == 0) {
      text = "No hints";
    } else {
      text = Integer.toString(hints) + " hints";
    }

    // Setting the label text for all hint labels
    if (libraryHintLabel != null) {
      libraryHintLabel.setText(text);
    }
    if (cauldronHintLabel != null) {
      cauldronHintLabel.setText(text);
    }
    if (rightHintLabel != null) {
      rightHintLabel.setText(text);
    }
  }
}
