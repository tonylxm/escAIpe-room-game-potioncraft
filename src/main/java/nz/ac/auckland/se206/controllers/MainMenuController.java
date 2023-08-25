package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

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

  @FXML private ToggleButton easyBtn;
  @FXML private ToggleButton mediumBtn;
  @FXML private ToggleButton hardBtn;
  @FXML private ToggleButton twoMinBtn;
  @FXML private ToggleButton fourMinBtn;
  @FXML private ToggleButton sixMinBtn;
  @FXML private ToggleButton startBtn;

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
  public void startGame() {
    System.out.println("CAULDRON ROOM");
    SceneManager.getUiRoot(AppUi.CAULDRON);
  }
}
