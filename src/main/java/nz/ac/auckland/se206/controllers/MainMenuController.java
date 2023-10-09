package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import nz.ac.auckland.se206.SoundEffects;
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
  private static String openedChest;
  private static String collectedItems;
  private static int hints;
  private static ShapeInteractionHandler interactionHandler;
  private SoundEffects soundEffects;

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

  public static String getOpenedChestMessage() {
    return openedChest;
  }

  public static String getCollectedItemsMessage() {
    return collectedItems;
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
  private ImageView cancelTtsBtn;
  @FXML
  private Rectangle mouseTrackRegion;

  private boolean easyBtnClicked;
  private boolean mediumBtnClicked;
  private boolean hardBtnClicked;
  private boolean twoMinBtnClicked;
  private boolean fourMinBtnClicked;
  private boolean sixMinBtnClicked;

  private String introMsg;
  private boolean ttsOn;
  private boolean appendIntroMsgFinished;

  /**
   * Initialises the main menu controller.
   */
  public void initialize() {
    // Item & inventory generation
    items = new Items(5);
    inventory = new Inventory();
    TransitionAnimation.setMasterPane(masterPane);
    difficultySelected = false;
    timeSelected = false;
    ttsOn = false;
    appendIntroMsgFinished = false;
    interactionHandler = new ShapeInteractionHandler();
    soundEffects = new SoundEffects();

    // Initialise booleans for settings selection
    easyBtnClicked = false;
    mediumBtnClicked = false;
    hardBtnClicked = false;
    twoMinBtnClicked = false;
    fourMinBtnClicked = false;
    sixMinBtnClicked = false;

    // Setting appropriate interactable features for the settings buttons including hover hints
    difficultyMouseActions(easyBtn, easyBtnClicked, hintInfinity, Difficulty.EASY);
    easyBtn.setOnMouseExited(event -> difficultyHoverOff(
        easyBtn, easyBtnClicked, hintInfinity));
    difficultyMouseActions(mediumBtn, mediumBtnClicked, hintFive, Difficulty.MEDIUM);
    mediumBtn.setOnMouseExited(event -> difficultyHoverOff(
        mediumBtn, mediumBtnClicked, hintFive));
    difficultyMouseActions(hardBtn, hardBtnClicked, hintZero, Difficulty.HARD);
    hardBtn.setOnMouseExited(event -> difficultyHoverOff(
        hardBtn, hardBtnClicked, hintZero));

    timeMouseActions(twoMinBtn, twoMinBtnClicked, twoMin, TimeLimit.TWO_MIN);
    twoMinBtn.setOnMouseExited(event -> timeLimitHoverOff(
        twoMinBtn, twoMinBtnClicked, twoMin));
    timeMouseActions(fourMinBtn, fourMinBtnClicked, fourMin, TimeLimit.FOUR_MIN);
    fourMinBtn.setOnMouseExited(event -> timeLimitHoverOff(
        fourMinBtn, fourMinBtnClicked, fourMin));
    timeMouseActions(sixMinBtn, sixMinBtnClicked, sixMin, TimeLimit.SIX_MIN);
    sixMinBtn.setOnMouseExited(event -> timeLimitHoverOff(
        sixMinBtn, sixMinBtnClicked, sixMin));

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

  /**
   * Handles the mouse actions for the difficulty and time limit buttons.
   * 
   * @param difficultyBtn
   * @param difficultyBtnClicked
   * @param hint
   * @param difficulty
   */
  public void difficultyMouseActions(
      ImageView difficultyBtn, boolean difficultyBtnClicked, Text hint, Difficulty difficulty) {
    difficultyBtn.setOnMouseEntered(event -> difficultyHoverOn(difficultyBtn, hint));
    difficultyBtn.setOnMouseClicked(event -> difficultySelect(difficulty));
  }

  /**
   * Handles the mouse actions for the difficulty and time limit buttons.
   * 
   * @param timeBtn
   * @param timeBtnClicked
   * @param timeTxt
   * @param time
   */
  public void timeMouseActions(
      ImageView timeBtn, boolean timeBtnClicked, Text timeTxt, TimeLimit time) {
    timeBtn.setOnMouseEntered(event -> timeLimitHoverOn(timeBtn, timeTxt));
    timeBtn.setOnMouseClicked(event -> timeSelect(time));
  }

  /**
   * Handles the hover on for the difficulty and time limit buttons.
   * 
   * @param settingsBtn
   * @param hint
   */
  public void difficultyHoverOn(
      ImageView settingsBtn, Text hint) {
    interactionHandler.glowThis(settingsBtn);
    if (!difficultySelected) {
      hint.setOpacity(1);
    } 
  }

  /**
   * Handles the hover off for the difficulty and time limit buttons.
   * 
   * @param settingsBtn
   * @param settingsBtnClicked
   * @param hint
   */
  public void difficultyHoverOff(
      ImageView settingsBtn, boolean settingsBtnClicked, Text hint) {
    interactionHandler.unglowThis(settingsBtn, settingsBtnClicked);
    if (!difficultySelected) {
      hint.setOpacity(0);
    } 
  }

  /**
   * Handles the hover on and off for the difficulty and time limit buttons.
   * @param settingsBtn
   * @param hint
   */
  public void timeLimitHoverOn(ImageView settingsBtn, Text hint) {
    interactionHandler.glowThis(settingsBtn);
    if (!timeSelected) {
      hint.setOpacity(1);
    } 
  }

  /**
   * Handles the hover off for the difficulty and time limit buttons.
   * 
   * @param settingsBtn
   * @param settingsBtnClicked
   * @param timeLimit
   */
  public void timeLimitHoverOff(
      ImageView settingsBtn, boolean settingsBtnClicked, Text timeLimit) {
    interactionHandler.unglowThis(settingsBtn, settingsBtnClicked);
    if (!timeSelected) {
      timeLimit.setOpacity(0);
    } 
  }

  /**
   * Displays the appropriate number of hints when hovering over a difficulty.
   * 
   * @param gameDifficulty
   */
  public void difficultySelect(Difficulty gameDifficulty) {
    difficultySelected = true;
    switch (gameDifficulty) {
      // Easiest level granting unlimited hints
      case EASY:
        interactionHandler.glowThis(easyBtn);
        hints = -1;
        hintInfinity.setOpacity(1);
        hintFive.setOpacity(0);
        hintZero.setOpacity(0);
        easyBtnClicked = true;
        interactionHandler.unglowThis(mediumBtn);
        mediumBtnClicked = false;
        interactionHandler.unglowThis(hardBtn);
        hardBtnClicked = false;
        break;
      // Medium level capping hints at 5
      case MEDIUM:
        difficulty = Difficulty.MEDIUM;
        interactionHandler.glowThis(mediumBtn);
        hints = 5;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(1);
        hintZero.setOpacity(0);
        mediumBtnClicked = true;
        interactionHandler.unglowThis(easyBtn);
        easyBtnClicked = false;
        interactionHandler.unglowThis(hardBtn);
        hardBtnClicked = false;
        break;
      // No hints are allowed to be given on hard level
      case HARD:
        difficulty = Difficulty.HARD;
        interactionHandler.glowThis(hardBtn);
        hints = 0;
        hintInfinity.setOpacity(0);
        hintFive.setOpacity(0);
        hintZero.setOpacity(1);
        hardBtnClicked = true;
        interactionHandler.unglowThis(easyBtn);
        easyBtnClicked = false;
        interactionHandler.unglowThis(mediumBtn);
        mediumBtnClicked = false;
        break;
    }
    continueBtnEnable();
  }

  /**
   * Displays the appropriate time limit when hovering over a time limit.
   * 
   * @param time
   */
  public void timeSelect(TimeLimit time) {
    timeSelected = true;
    switch (time) {
      // Using the appropriate glow animation over the 2 minuts image
      case TWO_MIN:
        interactionHandler.glowThis(twoMinBtn);
        twoMinBtnClicked = true;
        twoMin.setOpacity(1);
        interactionHandler.unglowThis(fourMinBtn);
        fourMinBtnClicked = false;
        fourMin.setOpacity(0);
        interactionHandler.unglowThis(sixMinBtn);
        sixMinBtnClicked = false;
        sixMin.setOpacity(0);
        CountdownTimer.setTimerLimit("2:00");
        break;
      // Using the appropriate glow animation over the 4 minutes image
      case FOUR_MIN:
        interactionHandler.glowThis(fourMinBtn);
        interactionHandler.unglowThis(twoMinBtn);
        twoMinBtnClicked = false;
        twoMin.setOpacity(0);
        fourMinBtnClicked = true;
        fourMin.setOpacity(1);
        interactionHandler.unglowThis(sixMinBtn);
        sixMinBtnClicked = false;
        sixMin.setOpacity(0);
        CountdownTimer.setTimerLimit("4:00");
        break;
      // Using the appropriate glow animation over the 6 minutes image
      case SIX_MIN:
        interactionHandler.glowThis(sixMinBtn);
        interactionHandler.unglowThis(twoMinBtn);
        twoMinBtnClicked = false;
        twoMin.setOpacity(0);
        interactionHandler.unglowThis(fourMinBtn);
        fourMinBtnClicked = false;
        fourMin.setOpacity(0);
        sixMinBtnClicked = true;
        sixMin.setOpacity(1);
        CountdownTimer.setTimerLimit("6:00");
        break;
    }
    // After selected a time limit, enabling the continue button
    continueBtnOneEnable();
  }

  /**
   * Handles starting a new game by creating new instances of the required scenes.
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
        SceneManager.addAppUi(
            AppUi.YOU_WIN, App.loadFxml("you-win"));

        //Create instance of you-win
        FXMLLoader youWinLoader = new FXMLLoader(
            App.class.getResource("/fxml/you-win.fxml"));
        Parent youWinRoot = youWinLoader.load();
        GameOverController youWinController = youWinLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.YOU_WIN, youWinRoot);
        SceneManager.setGameOverControllerInstance(youWinController);

    
        FXMLLoader treasureLoader = new FXMLLoader(
            App.class.getResource("/fxml/treasure_room.fxml"));
        Parent treasureRoot = treasureLoader.load();
        TreasureRoomController treasureController = treasureLoader.getController();
        SceneManager.addAppUi(AppUi.TREASURE_ROOM, treasureRoot);
        SceneManager.setTreasureRoomControllerInstance(treasureController);


        // Create an instance of CauldronController
        FXMLLoader loader = new FXMLLoader(
            App.class.getResource("/fxml/cauldron.fxml"));
        Parent cauldronRoot = loader.load();
        CauldronController cauldronController = loader.getController();
        
        // Store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.CAULDRON, cauldronRoot);
        SceneManager.setCauldronControllerInstance(cauldronController);

        //create an instance of BookController
        FXMLLoader bookLoader = new FXMLLoader(
            App.class.getResource("/fxml/book.fxml"));
        Parent bookRoot = bookLoader.load();
        BookController bookController = bookLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.BOOK, bookRoot);
        SceneManager.setBookControllerInstance(bookController);  

        //create an instance of LibraryRoomController
        FXMLLoader libraryRoomLoader = new FXMLLoader(
            App.class.getResource("/fxml/library_room.fxml"));
        Parent libraryRoomRoot = libraryRoomLoader.load();
        LibraryRoomController libraryRoomController = libraryRoomLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.LIBRARY_ROOM, libraryRoomRoot);
        SceneManager.setLibraryRoomControllerInstance(libraryRoomController);

        //create an instance of TreasureRoomController
        FXMLLoader treasureRoomLoader = new FXMLLoader(
            App.class.getResource("/fxml/treasure_room.fxml"));
        Parent treasureRoomRoot = treasureRoomLoader.load();
        TreasureRoomController treasureRoomController = treasureRoomLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.TREASURE_ROOM, treasureRoomRoot);
        SceneManager.setTreasureRoomControllerInstance(treasureRoomController);

        //create an instance of CauldronRoomController
        FXMLLoader cauldronRoomLoader = new FXMLLoader(
            App.class.getResource("/fxml/cauldron_room.fxml"));
        Parent cauldronRoomRoot = cauldronRoomLoader.load();
        CauldronRoomController cauldronRoomController = cauldronRoomLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.CAULDRON_ROOM, cauldronRoomRoot);
        SceneManager.setCauldronRoomControllerInstance(cauldronRoomController);

        //create an instance of ChestController
        FXMLLoader chestLoader = new FXMLLoader(
            App.class.getResource("/fxml/chest.fxml"));
        Parent chestRoot = chestLoader.load();
        ChestController chestController = chestLoader.getController();

        //store the controller instance in SceneManager
        SceneManager.addAppUi(AppUi.CHEST, chestRoot);
        SceneManager.setChestControllerInstance(chestController);

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
        // Setting the disable and enabling the appropriate animation
        Thread.sleep(500);
        disableAndOrFadeDifficultyBtns(false, 1.0, true);
        TransitionAnimation.fade(continueBtn, 0.4);
        return null;
      }
    };
    new Thread(fadeInDifficultyBtnsTask).start();
  }

  /**
   * Handles continuing a game by loading the appropriate settings.
   */
  @FXML
  public void onContinueGame() {
    continueBtn.setDisable(true);
    continueBtn.setOpacity(0.4);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInSettingsBtnsTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        // Setting the disable and fade to true and 0.0 respectively
        disableAndOrFadeDifficultyBtns(true, 0, true);
        disableAndOrFadeTimeBtns(false, 1.0, true);
        
        continueBtnOne.setOpacity(0.4);
        continueBtnDisable();
        return null;
      }
    };
    new Thread(fadeInSettingsBtnsTask).start();
  }

  /**
   * Handles continuing a game by loading the appropriate settings.
   */
  @FXML
  public void onContinueGameOne() {
    continueBtnOne.setDisable(true);
    continueBtnOne.setOpacity(0.4);
    // Using a task to make sure game does not freeze
    Task<Void> fadeInStartBtnTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        // Setting the disable and fade to true and 0.0 respectively
        disableAndOrFadeTimeBtns(true, 0, true);
        TransitionAnimation.fade(continueBtnOne, 0.0);
        Thread.sleep(1000);
        TransitionAnimation.fade(wizardImg, 1.0);
        Thread.sleep(1000);
        // Setting the appropriate animation transition
        TransitionAnimation.fade(wizardChatImage, 1.0);
        TransitionAnimation.fade(textRect, 1.0);
        TransitionAnimation.fade(chatTextArea, 1.0);
        TransitionAnimation.fade(ttsBtn2, 1.0);
        TransitionAnimation.fade(mouseTrackRegion, 0.4);
        
        // Enabling the chat text area and tts button
        chatTextArea.setDisable(false);
        ttsBtn2.setDisable(false);

        // Intro message to let the user know hwat to do
        introMsg = 
            "Welcome apprentice! Are you ready for your test?"
            + " Come talk to me for your instructions once you start the test."
            + " Good Luck!";

        Thread.sleep(500);
        // Showing the intro message, given by the wizard, to the user
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
    chatTextArea.setText(msg.getContent());
    // Appending the message character by character to the chat text area
    Task<Void> appendIntroTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        // After completion, letting user click off
        System.out.println("finished");
        mouseTrackRegion.setDisable(false);
        appendIntroMsgFinished = true;
        return null;
      }
    };
    new Thread(appendIntroTask).start();
  }

  /**
   * Handles click off for after the intro message is displayed.
   * 
   * @param event
   * @throws InterruptedException
   */
  @FXML
  public void clickOff(MouseEvent event) throws InterruptedException {
    System.out.println("click off");
    // Only using the click off property if the wizard has been shown already
    if (appendIntroMsgFinished) {
      // Using concurency to prevent lag or delay in the program
      Task<Void> wizardLeaveTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          TransitionAnimation.fade(wizardChatImage, 0.0);
          TransitionAnimation.fade(textRect, 0.0);
          TransitionAnimation.fade(chatTextArea, 0.0);
          TransitionAnimation.fade(ttsBtn2, 0.0);
          TransitionAnimation.fade(cancelTtsBtn, 0.0);
          TransitionAnimation.fade(mouseTrackRegion, 0);
          // Disabling the chat text area and tts button
          chatTextArea.setDisable(true);
          ttsBtn2.setDisable(true);
          cancelTtsBtn.setDisable(true);
          mouseTrackRegion.setDisable(true);

          Thread.sleep(1000);
          TransitionAnimation.fade(wizardImg, 0.0);
          Thread.sleep(1000);
          startBtnEnable();
          return null;
        }
      };
      new Thread(wizardLeaveTask).start();
    }
  }
  
  /**
   * Generating a random book for the user to guess through the riddle.
   * 
   * @return
   */
  private String getRandomBook() {
    int randomIndex = (int) (Math.random() * options.length);
    System.out.println(options[randomIndex]);
    return options[randomIndex];
  }

  /**
   * Approprately disables or enables the difficulty buttons.
   * 
   * @param tf       stands for true of false, if true then disable buttons, if
   *                 false then enable buttons.
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
   * Approprately disables or enables the time buttons.
   * 
   * @param tf       stands for true of false, if true then disable buttons, if
   *                 false then enable buttons.
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

  /**
   * Handles the difficulty selection.
   */
  @FXML
  public void setEasy() {
    difficulty = Difficulty.EASY;
    continueBtnEnable();
  }

  /**
   * Handles the difficulty selection.
   */
  @FXML
  public void setMedium() {
    difficulty = Difficulty.MEDIUM;
    continueBtnEnable();
  }

  /**
   * Handles the difficulty selection.
   */
  @FXML
  public void setHard() {
    difficulty = Difficulty.HARD;
    continueBtnEnable();
  }

  /**
   * Handles the time limit selection.
   */
  @FXML
  public void setTwoMin() {
    timeLimit = TimeLimit.TWO_MIN;
    CountdownTimer.setTimerLimit("2:00");
  }

  /**
   * Handles the time limit selection.
   */
  @FXML
  public void setFourMin() {
    timeLimit = TimeLimit.FOUR_MIN;
    CountdownTimer.setTimerLimit("4:00");
  }

  /**
   *  Handles the time limit selection.
   */
  @FXML
  public void setSixMin() {
    timeLimit = TimeLimit.SIX_MIN;
    CountdownTimer.setTimerLimit("6:00");
  }

  /**
   * Enable continueBtn and set visible.
   */
  public void continueBtnEnable() {
    continueBtn.setDisable(false);
    continueBtn.setOpacity(1.0);
  }

  /**
   * Enable continueBtn, set invisible and pregenerate book riddle.
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
                  openedChest = GptPromptEngineering.getEasyChestOpened();
                  collectedItems = GptPromptEngineering.getEasyItemsCollected();
                  break;
                case 5:
                  // When on Harry mode, selecting the prompt to give the user 
                  // only 5 hints
                  riddle = new ChatMessage(
                      "Wizard", chatHandler.runGpt(
                        GptPromptEngineering.getBookRiddleMedium(book)));
                  
                  // Message to send to GPT after user has resolved the riddle
                  resolvedRiddle = GptPromptEngineering.getMediumResolved();
                  openedChest = GptPromptEngineering.getMediumChestOpened();
                  collectedItems = GptPromptEngineering.getMediumItemsCollected();
                  break;
                case 0:
                  // When on Voldemort mode, selecting the prompt to give the 
                  // user no hints at all
                  riddle = new ChatMessage(
                      "Wizard", chatHandler.runGpt(
                        GptPromptEngineering.getBookRiddleHard(book)));
                  
                  // Message to send to GPT after user has resolved the riddle
                  resolvedRiddle = GptPromptEngineering.getHardResolved();
                  openedChest = GptPromptEngineering.getHardChestOpened();
                  collectedItems = GptPromptEngineering.getHardItemsCollected();
                  break;
              }
              return null;
            }
        };
    new Thread(bookRiddleTask).start();
    System.out.println(riddle.getContent());
  }

  /**
   * Enable continueBtnOne and set visible.
   */
  public void continueBtnOneEnable() {
    continueBtnOne.setDisable(false);
    continueBtnOne.setOpacity(1.0);
  }

  /**
   * Enable startBtn and set visible.
   */
  public void startBtnEnable() {
    startBtn.setDisable(false);
    startBtn.setOpacity(1.0);
  }

  /**
   * Handles the start button to start the game.
   * 
   * @throws IOException
   */
  @FXML
  public void onStartGame() throws IOException, URISyntaxException {
    System.out.println("MAIN MENU -> CAULDRON_ROOM");
    disableAndOrFadeTimeBtns(true, 0, false);
    // TransitionAnimation.changeScene(
    //     pane, AppUi.CAULDRON_ROOM, true);
    Scene currentScene = wizardImg.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.getCauldronRoomControllerInstance().fadeIn();
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    soundEffects.playGameTheme();

    countdownTimer.start();
    countdownTimer.updateHintLabel(hints);
  }

  /**
   * Handles the text to speech button.
   */
  @FXML
  private void onReadGameMasterResponse() {
    // Using concurency to prevent the system freezing
    if (!ttsOn) {
      ttsOn = true;
      cancelTtsBtn.setDisable(false);
      cancelTtsBtn.setOpacity(1);
      Task<Void> speakTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          App.textToSpeech.speak(chatTextArea.getText());
          return null;
        }
      };
      new Thread(speakTask).start();
      speakTask.setOnSucceeded(e -> {
        ttsOn = false;
        cancelTtsBtn.setDisable(true);
        cancelTtsBtn.setOpacity(0);
      });
    }
  }

  /**
   * Handles the cancel text to speech button.
   */
  @FXML
  private void onCancelTts() {
    ttsOn = false;
    cancelTtsBtn.setDisable(true);
    cancelTtsBtn.setOpacity(0);
    App.textToSpeech.stop();
  }
}
