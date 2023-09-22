package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Inventory;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
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
  private static String resolvedRiddle;
  private static int hints;
  private static ShapeInteractionHandler interactionHandler;

  public static int getHints() {
    return hints;
  }

  public static void setHints(int changedHints) {
    hints = changedHints;
  }

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

  public static String getResolvedMessage() {
    return resolvedRiddle;
  }

  public static CountdownTimer getCountdownTimer() {
    System.out.println("getting timer");
    return countdownTimer;
  }

  private Difficulty difficulty;
  private TimeLimit timeLimit;
  private String[] options = {"fire", "water", "air"};

  private boolean difficultySelected;
  private boolean timeSelected;

  @FXML
  private Pane masterPane;
  @FXML
  private Pane pane;
  @FXML
  private Button playBtn;
  @FXML
  private Button continueBtn;
  @FXML
  private Button continueBtnOne;
  @FXML
  private Button startBtn;
  @FXML
  private Text difficultyTxt;
  @FXML
  private Text timeLimitTxt;
  @FXML
  private ImageView easyBtn;
  @FXML
  private ImageView mediumBtn;
  @FXML
  private ImageView hardBtn;
  @FXML
  private ImageView twoMinBtn;
  @FXML
  private ImageView fourMinBtn;
  @FXML
  private ImageView sixMinBtn;
  @FXML
  private Text hintInfinity;
  @FXML
  private Text hintFive;
  @FXML
  private Text hintZero;
  @FXML
  private Text twoMin;
  @FXML
  private Text fourMin;
  @FXML
  private Text sixMin;

  @FXML
  private ImageView wizardChatImage;
  @FXML
  private ImageView wizardImg;
  @FXML
  private Rectangle textRect;
  @FXML 
  private TextArea chatTextArea;
  @FXML
  private ImageView ttsBtn2;
  @FXML
  private Rectangle mouseTrackRegion;

  private boolean easyBtnClicked;
  private boolean mediumBtnClicked;
  private boolean hardBtnClicked;
  private boolean twoMinBtnClicked;
  private boolean fourMinBtnClicked;
  private boolean sixMinBtnClicked;

  private ChatMessage introMsg;
  private boolean appendIntroMsgFinished;

  public void initialize() {
    // Item & inventory generation
    items = new Items(5);
    inventory = new Inventory();
    TransitionAnimation.setMasterPane(masterPane);
    difficultySelected = false;
    timeSelected = false;
    appendIntroMsgFinished = false;
    interactionHandler = new ShapeInteractionHandler();

    // Initialise booleans for settings selection
    easyBtnClicked = false;
    mediumBtnClicked = false;
    hardBtnClicked = false;
    twoMinBtnClicked = false;
    fourMinBtnClicked = false;
    sixMinBtnClicked = false;

    // Setting appropriate interactable features for the settings buttons including hover hints
    difficultyMouseActions(easyBtn, easyBtnClicked, hintInfinity, "EASY");
    difficultyMouseActions(mediumBtn, mediumBtnClicked, hintFive, "MEDIUM");
    difficultyMouseActions(hardBtn, hardBtnClicked, hintZero, "HARD");

    timeMouseActions(twoMinBtn, twoMinBtnClicked, twoMin, "TWO_MIN");
    timeMouseActions(fourMinBtn, fourMinBtnClicked, fourMin, "FOUR_MIN");
    timeMouseActions(sixMinBtn, sixMinBtnClicked, sixMin, "SIX_MIN");

    // Pregenerate wizard intro message
    // Task<Void> introTask =
    //     new Task<Void>() {
    //       @Override
    //       protected Void call() throws Exception {
    //         // FIX no click off
    //         introMsg = new ChatMessage("Wizard", 
    //             chatHandler.runGpt(GptPromptEngineering.getIntroMsg()));
    //         return null;
    //       }
    //     };
    // new Thread(introTask).start();
    // System.out.println(introMsg);
  }

  public void difficultyMouseActions(
      ImageView difficultyBtn, boolean difficultyBtnClicked, Text hint, String difficulty) {
    difficultyBtn.setOnMouseEntered(event -> difficultyHoverOn(difficultyBtn, hint));
    difficultyBtn.setOnMouseExited(event -> difficultyHoverOff(
        difficultyBtn, difficultyBtnClicked, hint));
    difficultyBtn.setOnMouseClicked(event -> difficultySelect(difficulty));
  }

  public void timeMouseActions(
      ImageView timeBtn, boolean timeBtnClicked, Text timeTxt, String time) {
    timeBtn.setOnMouseEntered(event -> timeLimitHoverOn(timeBtn, timeTxt));
    timeBtn.setOnMouseExited(event -> timeLimitHoverOff(timeBtn, timeBtnClicked, timeTxt));
    timeBtn.setOnMouseClicked(event -> timeSelect(time));
  }

  public void difficultyHoverOn(
      ImageView settingsBtn, Text hint) {
    interactionHandler.glowThis(settingsBtn);
    if (!difficultySelected) {
      hint.setOpacity(1);
    } 
  }

  public void difficultyHoverOff(
      ImageView settingsBtn, boolean settingsBtnClicked, Text hint) {
    interactionHandler.unglowThis(settingsBtn, settingsBtnClicked);
    if (!difficultySelected) {
      hint.setOpacity(0);
    } 
  }

  public void timeLimitHoverOn(ImageView settingsBtn, Text hint) {
    interactionHandler.glowThis(settingsBtn);
    if (!timeSelected) {
      hint.setOpacity(1);
    } 
  }

  public void timeLimitHoverOff(
      ImageView settingsBtn, boolean settingsBtnClicked, Text timeLimit) {
    interactionHandler.unglowThis(settingsBtn, settingsBtnClicked);
    if (!timeSelected) {
      timeLimit.setOpacity(0);
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
        interactionHandler.glowThis(easyBtn);
        hints = -1;
        hintInfinity.setOpacity(1);
        hintFive.setOpacity(0);
        hintZero.setOpacity(0);
        break;
      // Medium level capping hints at 5
      case "MEDIUM":
        interactionHandler.glowThis(mediumBtn);
        hints = 5;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(1);
        hintZero.setOpacity(0);
        break;
      // No hints are allowed to be given on hard level
      case "HARD":
        interactionHandler.glowThis(hardBtn);
        hints = 0;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(0);
        hintZero.setOpacity(1);
        break;
    }
    continueBtnEnable();
  }

  public void timeSelect(String time) {
    timeSelected = true;
    switch (time) {
      case "TWO_MIN":
        interactionHandler.glowThis(twoMinBtn);
        twoMinBtnClicked = true;
        twoMin.setOpacity(1);
        fourMinBtnClicked = false;
        fourMin.setOpacity(0);
        sixMinBtnClicked = false;
        sixMin.setOpacity(0);
        break;
      case "FOUR_MIN":
        interactionHandler.glowThis(fourMinBtn);
        twoMinBtnClicked = false;
        twoMin.setOpacity(0);
        fourMinBtnClicked = true;
        fourMin.setOpacity(1);
        sixMinBtnClicked = false;
        sixMin.setOpacity(0);
        break;
      case "SIX_MIN":
        interactionHandler.glowThis(sixMinBtn);
        twoMinBtnClicked = false;
        twoMin.setOpacity(0);
        fourMinBtnClicked = false;
        fourMin.setOpacity(0);
        sixMinBtnClicked = true;
        sixMin.setOpacity(1);
        break;
    }
    continueBtnOneEnable();
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
    Thread instantiateScenesThread = new Thread(instantiateScenes);
    instantiateScenesThread.start();

    TransitionAnimation.fade(playBtn, 0.0);
    playBtn.setDisable(true);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInDifficultyBtnsTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        Thread.sleep(500);
        disableAndOrFadeDifficultyBtns(false, 1.0, true);
        TransitionAnimation.fade(continueBtn, 0.4);
        return null;
      }
    };
    new Thread(fadeInDifficultyBtnsTask).start();
  }

  /**
   * Handles continuing a game by loading the appropriate settings
   */
  @FXML
  public void continueGame() {
    continueBtn.setDisable(true);
    continueBtn.setOpacity(0.4);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInSettingsBtnsTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        disableAndOrFadeDifficultyBtns(true, 0, true);
        disableAndOrFadeTimeBtns(false, 1.0, true);
        continueBtnDisable();
        continueBtnOne.setDisable(false);
        continueBtnOne.setOpacity(0.4);
        return null;
      }
    };
    new Thread(fadeInSettingsBtnsTask).start();
  }

  /**
   * Handles continuing a game by loading the appropriate settings
   */
  @FXML
  public void continueGameOne() {
    continueBtnOne.setDisable(true);
    continueBtnOne.setOpacity(0.4);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInStartBtnTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        disableAndOrFadeTimeBtns(true, 0, true);
        TransitionAnimation.fade(continueBtnOne, 0.0);
        Thread.sleep(1000);
        TransitionAnimation.fade(wizardImg, 1.0);
        Thread.sleep(1000);
        
        TransitionAnimation.fade(wizardChatImage, 1.0);
        TransitionAnimation.fade(textRect, 1.0);
        TransitionAnimation.fade(chatTextArea, 1.0);
        TransitionAnimation.fade(ttsBtn2, 1.0);
        chatTextArea.setDisable(false);
        ttsBtn2.setDisable(false);
        mouseTrackRegion.setOpacity(0.5);

        String introMsg = 
          "Welcome apprentice! Are you ready for your test?"
          + " Come talk to me for your instructions once you start the test."
          + " Good Luck!";

        Thread.sleep(500);
        appendIntroMessage(new ChatMessage("Wizard", introMsg), chatTextArea);

        TransitionAnimation.fade(startBtn, 0.4);
        return null;
      }
    };
    new Thread(fadeInStartBtnTask).start();
  }

    /**
   * Appends intro message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendIntroMessage(ChatMessage msg, TextArea chatTextArea) {
    // Adding the role of the chatter to the start of each message
    String displayRole;
    switch (msg.getRole()) {
      case "assistant":
        displayRole = "Wizard";
        break;
      case "user":
        displayRole = "You";
        break;
      default:
        displayRole = msg.getRole();
        break;
    }

    chatTextArea.appendText(displayRole + ": ");

    // Appending the message character by character to the chat text area
    Task<Void> appendIntroTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            for (char c : msg.getContent().toCharArray()) {
              chatTextArea.appendText(String.valueOf(c));
              Thread.sleep(20);
            }
            System.out.println("finished");
            mouseTrackRegion.setDisable(false);
            appendIntroMsgFinished = true;
            return null;
          }
        };
    new Thread(appendIntroTask).start();
    }

  /**
   * Handles click off for after the intro message is displayed
   * 
   * @param event
   * @throws InterruptedException
   */
  @FXML
  public void clickOff(MouseEvent event) throws InterruptedException {
    System.out.println("click off");
    if (appendIntroMsgFinished) {
      Task<Void> wizardLeaveTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          TransitionAnimation.fade(wizardChatImage, 0.0);
          TransitionAnimation.fade(textRect, 0.0);
          TransitionAnimation.fade(chatTextArea, 0.0);
          TransitionAnimation.fade(ttsBtn2, 0.0);
          chatTextArea.setDisable(true);
          ttsBtn2.setDisable(true);
          mouseTrackRegion.setDisable(true);
          mouseTrackRegion.setOpacity(0);

          Thread.sleep(1000);
          TransitionAnimation.fade(wizardImg, 0.0);
          Thread.sleep(500);
          startBtnEnable();
          return null;
        }
      };
    new Thread(wizardLeaveTask).start();
    }
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
   * Approprately disables or enables the difficulty buttons
   * 
   * @param tf       stands for true of false, if true then disable buttons, if
   *                 false then enable buttons
   * @param ocpacity
   * @param fade
   */
  public void disableAndOrFadeDifficultyBtns(
      boolean tf, double opacity, boolean fade) {
    easyBtn.setDisable(tf);
    mediumBtn.setDisable(tf);
    hardBtn.setDisable(tf);
    // Handing animations for fading
    if (fade) {
      TransitionAnimation.fade(difficultyTxt, opacity);
      TransitionAnimation.fade(easyBtn, opacity);
      TransitionAnimation.fade(mediumBtn, opacity);
      TransitionAnimation.fade(hardBtn, opacity);

      if (opacity == 0.0) {
        hintZero.setOpacity(0);
        hintFive.setOpacity(0);
        hintInfinity.setOpacity(0);

        hintZero.setDisable(true);
        hintFive.setDisable(true);
        hintInfinity.setDisable(true);
      }
    }
  }

  /**
   * Approprately disables or enables the time buttons
   * 
   * @param tf       stands for true of false, if true then disable buttons, if
   *                 false then enable buttons
   * @param ocpacity
   * @param fade
   */
  public void disableAndOrFadeTimeBtns(
      boolean tf, double opacity, boolean fade) {
    twoMinBtn.setDisable(tf);
    fourMinBtn.setDisable(tf);
    sixMinBtn.setDisable(tf);
    // Handing animations for fading
    if (fade) {
      TransitionAnimation.fade(timeLimitTxt, opacity);
      TransitionAnimation.fade(twoMinBtn, opacity);
      TransitionAnimation.fade(fourMinBtn, opacity);
      TransitionAnimation.fade(sixMinBtn, opacity);

      if (opacity == 0.0) {
        twoMin.setOpacity(0);
        fourMin.setOpacity(0);
        sixMin.setOpacity(0);

        twoMin.setDisable(true);
        fourMin.setDisable(true);
        sixMin.setDisable(true);
      }
    }
  }

  @FXML
  public void setEasy() {
    difficulty = Difficulty.EASY;
    continueBtnEnable();
  }

  @FXML
  public void setMedium() {
    difficulty = Difficulty.MEDIUM;
    continueBtnEnable();
  }

  @FXML
  public void setHard() {
    difficulty = Difficulty.HARD;
    continueBtnEnable();
  }

  @FXML
  public void setTwoMin() {
    timeLimit = TimeLimit.TWO_MIN;
    CountdownTimer.setTimerLimit("2:00");
  }

  @FXML
  public void setFourMin() {
    timeLimit = TimeLimit.FOUR_MIN;
    CountdownTimer.setTimerLimit("4:00");
  }

  @FXML
  public void setSixMin() {
    timeLimit = TimeLimit.SIX_MIN;
    CountdownTimer.setTimerLimit("6:00");
  }

  /**
   * Enable continueBtn and set visible
   */
  public void continueBtnEnable() {
    continueBtn.setDisable(false);
    continueBtn.setOpacity(1.0);
  }

  /**
   * Enable continueBtn, set invisible and pregenerate book riddle
   */

  public void continueBtnDisable() {
    continueBtn.setDisable(true);
    continueBtn.setOpacity(0.0);

    Task<Void> bookRiddleTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              switch (hints) {
                case -1:
                  // When on Dobby mode, selecting the prompt to give the user 
                  // unlimited hints
                  riddle = new ChatMessage(
                      "Wizard", chatHandler.runGpt(
                        GptPromptEngineering.getBookRiddleEasy(book)));

                  // Message to send to GPT after user has resolved the riddle
                  resolvedRiddle = GptPromptEngineering.getEasyResolved();
                  break;
                case 5:
                  // When on Harry mode, selecting the prompt to give the user 
                  // only 5 hints
                  riddle = new ChatMessage(
                      "Wizard", chatHandler.runGpt(
                        GptPromptEngineering.getBookRiddleMedium(book)));
                  
                  // Message to send to GPT after user has resolved the riddle
                  resolvedRiddle = GptPromptEngineering.getMediumResolved();
                  break;
                case 0:
                  // When on Voldemort mode, selecting the prompt to give the 
                  // user no hints at all
                  riddle = new ChatMessage(
                      "Wizard", chatHandler.runGpt(
                        GptPromptEngineering.getBookRiddleHard(book)));
                  
                  // Message to send to GPT after user has resolved the riddle
                  resolvedRiddle = GptPromptEngineering.getHardResolved();
                  break;
              }
              return null;
            }
        };
      new Thread(bookRiddleTask).start();
      System.out.println(riddle.getContent());
  }

  /**
   * Enable continueBtnOne and set visible
   */
  public void continueBtnOneEnable() {
    continueBtnOne.setDisable(false);
    continueBtnOne.setOpacity(1.0);
  }

  /**
   * Enable startBtn and set visible
   */
  public void startBtnEnable() {
    startBtn.setDisable(false);
    startBtn.setOpacity(1.0);
  }

  @FXML
  public void startGame() throws IOException {
    // Fade buttons and scene
    disableAndOrFadeTimeBtns(true, 0, false);
    System.out.println("MAIN MENU -> CAULDRON_ROOM");
    TransitionAnimation.changeScene(
        pane, AppUi.CAULDRON_ROOM, true);
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);

    Task<Void> timerStartTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        Thread.sleep(2000);
        countdownTimer.start();
        return null;
      }
    };
    countdownTimer.updateHintLabel(hints);
    Thread timerStartThread = new Thread(
        timerStartTask, "timer start thread");
    timerStartThread.start();
  }

  public void readGameMasterResponse() {
    // Using concurency to prevent the system freezing
    Task<Void> speakTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        // need to update the chat text area with the game master's response 
        // & riddle
        App.textToSpeech.speak(chatTextArea.getText());
        return null;
      }
    };
    new Thread(speakTask, "Speak Thread").start();
  }
}
