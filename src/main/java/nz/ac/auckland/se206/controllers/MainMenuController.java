package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.Inventory;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TransitionAnimation;

public class MainMenuController {
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  public enum TimeLimit {
    TWO_MIN,
    FOUR_MIN,
    SIX_MIN
  }

  private Difficulty difficulty;
  private TimeLimit timeLimit;

  public static Items items;
  public static Inventory inventory;

  @FXML private Pane pane;
  @FXML private Button playBtn;
  @FXML private Button startBtn;
  @FXML private Text difficultyTxt;
  @FXML private Text timeLimitTxt;
  @FXML private ToggleButton easyBtn;
  @FXML private ToggleButton mediumBtn;
  @FXML private ToggleButton hardBtn;
  @FXML private ToggleButton twoMinBtn;
  @FXML private ToggleButton fourMinBtn;
  @FXML private ToggleButton sixMinBtn;

  public void initialize() {
    // pane.setOpacity(1);
    // playBtn.setOpacity(1);
  }

  @FXML
  public void playGame() throws InterruptedException {
    TransitionAnimation.fade(playBtn, 0.0);
    playBtn.setDisable(true);

    Task<Void> fadeInSettingsBtnsTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            Thread.sleep(500);
            disableAndOrFadeSettingsBtns(false, 1.0, true);
            return null;
          }
        };
    Thread fadeInSettingsBtnsThread =
        new Thread(fadeInSettingsBtnsTask, "fadeIn settings btns thread");
    fadeInSettingsBtnsThread.start();
  }

  // tf stands for true/false
  public void disableAndOrFadeSettingsBtns(boolean tf, double ocpacity, boolean fade) {
    easyBtn.setDisable(tf);
    mediumBtn.setDisable(tf);
    hardBtn.setDisable(tf);
    twoMinBtn.setDisable(tf);
    fourMinBtn.setDisable(tf);
    sixMinBtn.setDisable(tf);

    startBtn.setDisable(true);

    if (fade) {
      TransitionAnimation.fade(difficultyTxt, ocpacity);
      TransitionAnimation.fade(timeLimitTxt, ocpacity);
      TransitionAnimation.fade(easyBtn, ocpacity);
      TransitionAnimation.fade(mediumBtn, ocpacity);
      TransitionAnimation.fade(hardBtn, ocpacity);
      TransitionAnimation.fade(twoMinBtn, ocpacity);
      TransitionAnimation.fade(fourMinBtn, ocpacity);
      TransitionAnimation.fade(sixMinBtn, ocpacity);
      TransitionAnimation.fade(startBtn, 0.4);
    }
  }

  @FXML
  public void setEasy() {
    difficulty = Difficulty.EASY;
    startBtnEnable();
  }

  @FXML
  public void setMedium() {
    difficulty = Difficulty.MEDIUM;
    startBtnEnable();
  }

  @FXML
  public void setHard() {
    difficulty = Difficulty.HARD;
    startBtnEnable();
  }

  @FXML
  public void setTwoMin() {
    timeLimit = TimeLimit.TWO_MIN;
    startBtnEnable();
  }

  @FXML
  public void setFourMin() {
    timeLimit = TimeLimit.FOUR_MIN;
    startBtnEnable();
  }

  @FXML
  public void setSixMin() {
    timeLimit = TimeLimit.SIX_MIN;
    startBtnEnable();
  }

  // Only set startBtn enabled when difficutly and time limit have been chosen
  public void startBtnEnable() {
    if (difficulty != null && timeLimit != null) {
      startBtn.setDisable(false);
      startBtn.setOpacity(1.0);
    }
  }

  @FXML
  public void startGame() throws IOException {
    // Item & inventory generation
    items = new Items(3);
    inventory = new Inventory();

    // Fade buttons and scene
    disableAndOrFadeSettingsBtns(true, 0, false);
    System.out.println("MAIN MENU -> CAULDRON ROOM");
    TransitionAnimation.fadeScene(pane, 0, AppUi.CAULDRON_ROOM);
  }
}
