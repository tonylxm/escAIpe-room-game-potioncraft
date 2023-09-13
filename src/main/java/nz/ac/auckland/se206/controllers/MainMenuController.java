package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Inventory;
import nz.ac.auckland.se206.Items;
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

  public static CountdownTimer countdownTimer = new CountdownTimer("2:00");

  public static Items items;
  public static Inventory inventory;

  public static CountdownTimer getCountdownTimer() {
    System.out.println("getting timer");
    return countdownTimer;
  }

  private Difficulty difficulty;
  private TimeLimit timeLimit;

  private boolean difficultySelected;

  @FXML
  private Pane pane;
  @FXML
  private Button playBtn;
  @FXML
  private Button startBtn;
  @FXML
  private Text difficultyTxt;
  @FXML
  private Text timeLimitTxt;
  @FXML
  private ToggleButton easyBtn;
  @FXML
  private ToggleButton mediumBtn;
  @FXML
  private ToggleButton hardBtn;
  @FXML
  private ToggleButton twoMinBtn;
  @FXML
  private ToggleButton fourMinBtn;
  @FXML
  private ToggleButton sixMinBtn;
  @FXML
  private Text hintInfinity;
  @FXML
  private Text hintFive;
  @FXML
  private Text hintZero;

  public void initialize() {
    // Item & inventory generation
    items = new Items(5);
    inventory = new Inventory();

    difficultySelected = false;
    // Hover hints on difficulty selection
    easyBtn.setOnMouseEntered(event -> difficultyHoverOn(hintInfinity));
    easyBtn.setOnMouseExited(event -> difficultyHoverOff(hintInfinity));
    easyBtn.setOnMouseClicked(event -> difficultySelect("EASY"));
    mediumBtn.setOnMouseEntered(event -> difficultyHoverOn(hintFive));
    mediumBtn.setOnMouseExited(event -> difficultyHoverOff(hintFive));
    mediumBtn.setOnMouseClicked(event -> difficultySelect("MEDIUM"));
    hardBtn.setOnMouseEntered(event -> difficultyHoverOn(hintZero));
    hardBtn.setOnMouseExited(event -> difficultyHoverOff(hintZero));
    hardBtn.setOnMouseClicked(event -> difficultySelect("HARD"));
  }

  public void difficultyHoverOn(Text hint) {
    if (!difficultySelected) {
      hint.setOpacity(1);
    }
  }

  public void difficultyHoverOff(Text hint) {
    if (!difficultySelected) {
      hint.setOpacity(0);
    }
  }

  public void difficultySelect(String difficulty) {
    difficultySelected = true;
    switch (difficulty) {
      case "EASY":
        hintInfinity.setOpacity(1);
        hintFive.setOpacity(0);
        hintZero.setOpacity(0);
        break;
      case "MEDIUM":
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(1);
        hintZero.setOpacity(0);
        break;
      case "HARD":
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(0);
        hintZero.setOpacity(1);
        break;
    }
  }

  /**
   * Handles starting a new game by creating new instances of the required scenes
   */
  @FXML
  public void playGame() throws InterruptedException, IOException {
    // Using a task to make sure game does not freeze
    Task<Void> instantiateScenes = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        SceneManager.addAppUi(AppUi.CAULDRON_ROOM, App.loadFxml("cauldron_room"));
        SceneManager.addAppUi(AppUi.LIBRARY_ROOM, App.loadFxml("library_room"));
        SceneManager.addAppUi(AppUi.TREASURE_ROOM, App.loadFxml("treasure_room"));
        SceneManager.addAppUi(AppUi.BOOK, App.loadFxml("book"));
        return null;
      }
    };
    Thread instantiateScenesThread = new Thread(
        instantiateScenes, "instantiate scenes upon starting game");
    instantiateScenesThread.start();

    TransitionAnimation.fade(playBtn, 0.0);
    playBtn.setDisable(true);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInSettingsBtnsTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        Thread.sleep(500);
        disableAndOrFadeSettingsBtns(false, 1.0, true);
        return null;
      }
    };
    Thread fadeInSettingsBtnsThread = new Thread(
        fadeInSettingsBtnsTask, "fadeIn settings btns thread");
    fadeInSettingsBtnsThread.start();
  }

  /**
   * Approprately disables or enables the settings buttons
   * 
   * @param tf       stands for true of false, if true then disable buttons, if
   *                 false then enable buttons
   * @param ocpacity
   * @param fade
   */
  public void disableAndOrFadeSettingsBtns(boolean tf, double ocpacity, boolean fade) {
    easyBtn.setDisable(tf);
    mediumBtn.setDisable(tf);
    hardBtn.setDisable(tf);
    twoMinBtn.setDisable(tf);
    fourMinBtn.setDisable(tf);
    sixMinBtn.setDisable(tf);

    startBtn.setDisable(true);

    // Handing animations for fading
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
    CountdownTimer.setTimerLimit("2:00");
    startBtnEnable();
  }

  @FXML
  public void setFourMin() {
    timeLimit = TimeLimit.FOUR_MIN;
    CountdownTimer.setTimerLimit("4:00");
    startBtnEnable();
  }

  @FXML
  public void setSixMin() {
    timeLimit = TimeLimit.SIX_MIN;
    CountdownTimer.setTimerLimit("6:00");
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
    // Fade buttons and scene
    disableAndOrFadeSettingsBtns(true, 0, false);
    System.out.println("MAIN MENU -> CAULDRON ROOM");
    TransitionAnimation.fadeScene(pane, 0, AppUi.CAULDRON_ROOM, 2);
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);

    Task<Void> timerStartTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        Thread.sleep(2000);
        countdownTimer.start();
        return null;
      }
    };
    Thread timerStartThread = new Thread(timerStartTask, "timer start thread");
    timerStartThread.start();
  }
}
