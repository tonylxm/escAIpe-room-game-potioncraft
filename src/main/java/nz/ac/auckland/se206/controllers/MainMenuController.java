package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Inventory;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatHandler;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

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

  private static CountdownTimer countdownTimer = new CountdownTimer("2:00");

  private static Items items;
  static Inventory inventory;
  
  private static String book;
  private static ChatHandler chatHandler;
  private static ChatMessage riddle;
  private static int hints;

  public static Items getItems() {
    return items;
  }

  public static Inventory getInventory() {
    return inventory;
  }

  public static String getBook() {
    return book;
  }

  public static ChatHandler getChatHandler() {
    return chatHandler;
  }

  public static ChatMessage getRiddle() {
    return riddle;
  }

  public static int getHints() {
    return hints;
  }

  public static CountdownTimer getCountdownTimer() {
    System.out.println("getting timer");
    return countdownTimer;
  }

  private Difficulty difficulty;
  private TimeLimit timeLimit;
  private String[] options = {"fire", "water", "air"};

  private boolean difficultySelected;

  @FXML
  private Pane masterPane;
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
    TransitionAnimation.setMasterPane(masterPane);
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

  /**
   * Displays the appropriate number of hints when hovering over a difficulty.
   * @param difficulty
   */
  public void difficultySelect(String difficulty) {
    difficultySelected = true;
    switch (difficulty) {
      // Easiest level granting unlimited hints
      case "EASY":
        hints = -1;
        hintInfinity.setOpacity(1);
        hintFive.setOpacity(0);
        hintZero.setOpacity(0);
        break;
      // Medium level capping hints at 5
      case "MEDIUM":
        hints = 5;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(1);
        hintZero.setOpacity(0);
        break;
      // No hints are allowed to be given on hard level
      case "HARD":
        hints = 0;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(0);
        hintZero.setOpacity(1);
        break;
    }

    Task<Void> bookRiddleTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            switch (hints) {
              case -1:
                // When on Dobby mode, selecting the prompt to give the user unlimited hints
                riddle =
                  new ChatMessage(
                    "Wizard", chatHandler.runGpt(GptPromptEngineering.getBookRiddleEasy(book)));
                break;
              case 5:
                // When on Harry mode, selecting the prompt to give the user only 5 hints
                riddle =
                  new ChatMessage(
                    "Wizard", chatHandler.runGpt(GptPromptEngineering.getBookRiddleMedium(book)));
                break;
              case 0:
                // When on Voldemort mode, selecting the prompt to give the user no hints at all
                riddle =
                  new ChatMessage(
                      "Wizard", chatHandler.runGpt(GptPromptEngineering.getBookRiddleHard(book)));
                break;
            }
            return null;
          }
      };
    new Thread(bookRiddleTask).start();
    System.out.println(riddle);
  }

  /**
   * Handles starting a new game by creating new instances of the required scenes
   */
  @FXML
  public void playGame() throws InterruptedException, IOException {
    // Initialising the book and the chat handler whenever a new game is started from the
    // play button to have enough time to intialise the chat messages
    chatHandler = new ChatHandler();
    try {
      chatHandler.initialize();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
    book = getRandomBook();

    // Using a task to make sure game does not freeze
    Task<Void> instantiateScenes = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        SceneManager.addAppUi(AppUi.CAULDRON_ROOM, App.loadFxml("cauldron_room"));
        SceneManager.addAppUi(AppUi.LIBRARY_ROOM, App.loadFxml("library_room"));
        SceneManager.addAppUi(AppUi.TREASURE_ROOM, App.loadFxml("treasure_room"));
        //SceneManager.addAppUi(AppUi.BOOK, App.loadFxml("book"));
        SceneManager.addAppUi(AppUi.YOU_WIN, App.loadFxml("you-win"));
    
        // Create an instance of CauldronController
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/cauldron.fxml"));
        Parent cauldronRoot = loader.load();
        CauldronController cauldronController = loader.getController();
        
        // Store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.CAULDRON, cauldronRoot);
        SceneManager.setCauldronControllerInstance(cauldronController);

        //create an instance of BookController
        FXMLLoader bookLoader = new FXMLLoader(App.class.getResource("/fxml/book.fxml"));
        Parent bookRoot = bookLoader.load();
        BookController bookController = bookLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.BOOK, bookRoot);
        SceneManager.setBookControllerInstance(bookController);  

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
   * Generating a random book for the user to guess through the riddle
   * 
   * @return
   */
  private String getRandomBook() {
    int randomIndex = (int) (Math.random() * options.length);
    System.out.println(options[randomIndex]);
    return options[randomIndex];
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

  /**
   * Only set startBtn enabled when difficutly and time limit have been chosen
   */
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
    TransitionAnimation.changeScene(pane, AppUi.CAULDRON_ROOM, true);
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
