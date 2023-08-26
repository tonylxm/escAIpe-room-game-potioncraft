package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.SceneManager;
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
  private TransitionAnimation animation = new TransitionAnimation();

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

  @FXML
  public void playGame() throws InterruptedException {
    animation.fade(playBtn, false);
    playBtn.setDisable(true);

    Task<Void> fadeInTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            Thread.sleep(1000);
            fadeAndDisableSettingsBtns(false, true);
            return null;
          }
        };
    Thread fadeInThread = new Thread(fadeInTask, "Fade In Thread");
    fadeInThread.start();
  }

  // tf stands for true/false
  public void fadeAndDisableSettingsBtns(boolean tf, boolean appear) {
    easyBtn.setDisable(tf);
    mediumBtn.setDisable(tf);
    hardBtn.setDisable(tf);
    twoMinBtn.setDisable(tf);
    fourMinBtn.setDisable(tf);
    sixMinBtn.setDisable(tf);
    startBtn.setDisable(tf);

    animation.fade(difficultyTxt, appear);
    animation.fade(timeLimitTxt, appear);
    animation.fade(easyBtn, appear);
    animation.fade(mediumBtn, appear);
    animation.fade(hardBtn, appear);
    animation.fade(twoMinBtn, appear);
    animation.fade(fourMinBtn, appear);
    animation.fade(sixMinBtn, appear);
    animation.fade(startBtn, appear);
  }

  @FXML
  public void setEasy() {
    difficulty = Difficulty.EASY;
  }

  @FXML
  public void setMedium() {
    difficulty = Difficulty.MEDIUM;
  }

  @FXML
  public void setHard() {
    difficulty = Difficulty.HARD;
  }

  @FXML
  public void setTwoMin() {
    timeLimit = TimeLimit.TWO_MIN;
  }

  @FXML
  public void setFourMin() {
    timeLimit = TimeLimit.FOUR_MIN;
  }

  @FXML
  public void setSixMin() {
    timeLimit = TimeLimit.SIX_MIN;
  }

  @FXML
  public void startGame() throws IOException {
    animation.fade(startBtn, false);
    fadeAndDisableSettingsBtns(true, false);

    // NEXT: FADE SCENES IN AND OUT
    System.out.println("CAULDRON ROOM");
    Scene sceneButtonIsIn = startBtn.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
  }
}
