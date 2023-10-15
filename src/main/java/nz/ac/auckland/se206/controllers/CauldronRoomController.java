package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.Notification;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatMessage;

/**
 * Controller for the cauldron room scene. Handles all the interactions
 * between the user and the cauldron room scene.
 * Contains methods for handling the user clicking on the cauldron,
 * wizard, arrows, book button, bag button, book fire, book water,
 * book air.
 */
public class CauldronRoomController extends RoomController {
  @FXML
  private Pane pane;
  @FXML
  private ImageView cauldronImg;
  @FXML
  private Polygon rightArrow;
  @FXML
  private Polygon leftArrow;
  @FXML
  private ImageView bookFireRectangle;
  @FXML
  private ImageView bookWaterRectangle;
  @FXML
  private ImageView bookAirRectangle;
  @FXML
  private ImageView bookFireImage;
  @FXML
  private ImageView bookWaterImage;
  @FXML
  private ImageView bookAirImage;
  @FXML
  private ImageView fireImg;
  @FXML
  private ImageView waterImg;
  @FXML
  private ImageView airImg;
  @FXML
  private Rectangle textRect;
  @FXML
  private Label riddleSelectLabel;
  @FXML
  private Label chooseLabel;

  @FXML
  private ImageView itemElevenImg;
  @FXML
  private ImageView itemTwelveImg;
  @FXML
  private ImageView itemThirteenImg;
  @FXML
  private ImageView itemFourteenImg;
  @FXML
  private ImageView itemFifteenImg;
  @FXML
  private Rectangle fadeRectangle;

  private boolean showRecipe = true;
  private CountdownTimer countdownTimer;

  /**
   * Setting the appropriate fields and listeners when scene is initialised.
   * Setting the appropriate game states for the riddle and the books when
   * the scene is initialised.
   */
  public void initialize() {
    // Initialising everything from the superclass
    genericInitialise("Cauldron", itemElevenImg, itemTwelveImg, 
        itemThirteenImg, itemFourteenImg, itemFifteenImg);

    // Setting up the countdown and appropriate booleans before
    // anything happens to change them within the game
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setCauldronTimerLabel(timerLabel);
    countdownTimer.setCauldronHintLabel(hintLabel);
    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    
    toggleBooks(true, 0);

    // Setting up the appropriate interactions for the cauldron, wizard, 
    // arrows, book button, bag button, book fire, book water, book air
    btnMouseActions(cauldronImg);
    arrowMouseActions(leftArrow);
    arrowMouseActions(rightArrow);

    btnMouseActions(bookFireRectangle);
    btnMouseActions(bookWaterRectangle);
    btnMouseActions(bookAirRectangle);
  }

  /**
   * Setting the text for the riddle select label. Makes the book images
   * visible and interactable depending on the disable value.
   *
   * @param disable Whether the books should be disabled or not.
   * @param opacity The opacity of the books.
   */
  private void toggleBooks(boolean disable, int opacity) {
    // Making books interactable or not based on the diasble
    bookFireRectangle.setDisable(disable);
    bookWaterRectangle.setDisable(disable);
    bookAirRectangle.setDisable(disable);
    // Making appropriate images visiable or not based on the opacity
    bookFireRectangle.setOpacity(opacity);
    bookWaterRectangle.setOpacity(opacity);
    bookAirRectangle.setOpacity(opacity);
    fireImg.setOpacity(opacity);
    waterImg.setOpacity(opacity);
    airImg.setOpacity(opacity);
  }

  /**
   * Taking user to the cauldron scene from the room scene to be able to 
   * brew their potions.
   *
   * @param event The mouse event.
   * @throws URISyntaxException If the URI is invalid.
   */
  @FXML
  public void clickCauldron(MouseEvent event) throws URISyntaxException {
    if (!GameState.isBookRiddleResolved) {
      notificationText.setText(
          "The Wizard has some instructions for you! Talk to him first!");
      Notification.notifyPopup(notificationBack, notificationText);
    } else {
      CauldronController cauldronController = SceneManager.getCauldronControllerInstance();
      if (cauldronController != null) {
        cauldronController.updateImageStates();
      }

      // If the cauldronController exists, then switching scenes
      System.out.println("cauldron clicked");
      itemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("CAULDRON_ROOM -> CAULDRON");
      // TransitionAnimation.changeScene(pane, AppUi.CAULDRON, false);
      Scene currentScene = fadeRectangle.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON));
      SceneManager.getCauldronControllerInstance().fadeIn();
      SceneManager.setTimerScene(AppUi.CAULDRON);
      System.out.println(Items.necessary);
    }
  }
  
  /**
   * Talking the user to the wizard's chat functionality.
   * Also, if the user hasn't been prompted with the riddle yet,
   * showing it to them too.
   *
   * @param event The mouse event.
   * @throws InterruptedException If the thread is interrupted.
   */
  @FXML
  public void clickCauldronRoomWizard(MouseEvent event) throws InterruptedException {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleGiven) {
      Task<Void> waitForAnimationTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          // Running gpt to get the riddle from the wizard
          toggleChat(false, 1);
          // mouseTrackRegion.setDisable(true);
          disableChat(true, 0.5);
          return null;
        }
      };
      new Thread(waitForAnimationTask).start();
      MainMenuController.getChatHandler().appendChatMessage(
          MainMenuController.getRiddle(), chatTextArea, inputText, sendButton);

      // After the riddle scrolling text animation has finished, then allowing
      // the user to select the book and respond to the wizard
      MainMenuController.getChatHandler().getAppendTask().setOnSucceeded(e -> {
        chooseLabel.setOpacity(1);
        disableChat(false, 1);
        toggleBooks(false, 1);
        //set the fire, water and air images to glow
        ShapeInteractionHandler glow = new ShapeInteractionHandler();
        glow.glowThis(fireImg);
        glow.glowThis(waterImg);
        glow.glowThis(airImg);
      });
      GameState.isBookRiddleGiven = true;
    } else {
      toggleChat(false, 1);
    }
  }

  /**
   * Handling the user clicking on the book fire. Calling the handleClickBooks
   * method to handle the user clicking on the book fire.
   *
   * @param event The mouse event.
   * @throws URISyntaxException The URI is invalid.
   */
  @FXML
  public void clickBookFire(MouseEvent event) throws URISyntaxException {
    handleClickBooks("fire", bookFireImage, bookFireRectangle);
  }

  /**
   * Handling the user clicking on the book water. Calling the handleClickBooks
   * method to handle the user clicking on the book water.
   *
   * @param event The mouse event.
   * @throws URISyntaxException The URI is invalid.
   */
  @FXML
  public void clickBookWater(MouseEvent event) throws URISyntaxException {
    handleClickBooks("water", bookWaterImage, bookWaterRectangle);
  }

  /**
   * Handling the user clicking on the book air. Calling the handleClickBooks
   * method to handle the user clicking on the book air.
   *
   * @param event The mouse event.
   * @throws URISyntaxException The URI is invalid.
   */
  @FXML
  public void clickBookAir(MouseEvent event) throws URISyntaxException {
    handleClickBooks("air", bookAirImage, bookAirRectangle);
  }

  /**
   * Handling the user clicking any of the books, and depending on which book
   * is the correct book, fading out the other two books and their rectangles.
   * Handling the situations appropriately.
   *
   * @param element The element of the book.
   * @param bookImage The book image.
   * @param bookRectangle The book rectangle.
   * @throws URISyntaxException If the URI is invalid.
   */
  private void handleClickBooks(
      String element, ImageView bookImage, ImageView bookRectangle) throws URISyntaxException {
    System.out.println("book " + element +  " clicked");
    if (MainMenuController.getBook() == element) {
      soundEffects.playSound("correct.wav");
      // remove the book from the scene
      //disable all book rectangles
      bookAirRectangle.setDisable(true);
      bookFireRectangle.setDisable(true);
      bookWaterRectangle.setDisable(true);
      airImg.setDisable(true);
      fireImg.setDisable(true);
      waterImg.setDisable(true);

      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();

      // set on fade finish
      ft.setOnFinished(event -> {
        //fade transition for all 3 books
        FadeTransition ft1 = new FadeTransition(Duration.seconds(1), bookFireRectangle);
        ft1.setFromValue(1);
        ft1.setToValue(0);

        FadeTransition ft2 = new FadeTransition(Duration.seconds(1), bookWaterRectangle);
        ft2.setFromValue(1);
        ft2.setToValue(0);

        FadeTransition ft3 = new FadeTransition(Duration.seconds(1), bookAirRectangle);
        ft3.setFromValue(1);
        ft3.setToValue(0);

        //fade transition for all 3 images
        FadeTransition ft4 = new FadeTransition(Duration.seconds(1), fireImg);
        ft4.setFromValue(1);
        ft4.setToValue(0);

        FadeTransition ft5 = new FadeTransition(Duration.seconds(1), waterImg);
        ft5.setFromValue(1);
        ft5.setToValue(0);

        FadeTransition ft6 = new FadeTransition(Duration.seconds(1), airImg);
        ft6.setFromValue(1);
        ft6.setToValue(0);

        if (element == "fire") {
          if (bookAirRectangle.getOpacity() == 1) {
            ft5.play();
            ft6.play();
            ft2.play();
            ft3.play();
          }
        } else if (element == "water") {
          if (bookFireRectangle.getOpacity() == 1) {
            ft4.play();
            ft6.play();
            ft1.play();
            ft3.play();
          }
        } else if (element == "air") {
          if (bookWaterRectangle.getOpacity() == 1) {
            ft4.play();
            ft5.play();
            ft2.play();
            ft1.play();
          }
        } 
      });

      bookImage.setOpacity(0);
      bookImage.setDisable(true);
      bookRectangle.setDisable(true);

      //switch statement to set the correct image opacity to 0
      switch (element) {
        case "fire":
          fireImg.setOpacity(0);
          break;
        case "water":
          waterImg.setOpacity(0);
          break;
        case "air":
          airImg.setOpacity(0);
          break;
        default:
          break;
      }

      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);

      disableChat(true, 0.5);
      enableItems();
      // Code to send appropriate riddle resolved message to GPT
      Task<Void> resolvedTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          // Running gpt after the riddle has been resolved to congratulate
          // the user and tell GPT what hints to give next
          ChatMessage msg = new ChatMessage(
              "Wizard", MainMenuController.getChatHandler().runGpt(
              MainMenuController.getResolvedMessage()));
          MainMenuController.getChatHandler().appendChatMessage(
              msg, chatTextArea, inputText, sendButton);
          return null;
        }
      };
      new Thread(resolvedTask).start();
      resolvedTask.setOnSucceeded(e -> {
        mouseTrackRegion.setCursor(Cursor.HAND);
        mouseTrackRegion.setDisable(false);
      });
    } else {
      // Wrong book selected
      soundEffects.playSound("incorrect.wav");
    }  
  }

  /**
   * Enabling the items to be able to be clicked on. Turning off
   * the disable feature for each item and making them visible
   * to the user.
   */
  private void enableItems() {
    // Allowing all items to be interactable
    itemElevenImg.setDisable(false);
    itemTwelveImg.setDisable(false);
    itemThirteenImg.setDisable(false);
    itemFourteenImg.setDisable(false);
    itemFifteenImg.setDisable(false);
    // Making all items visible
    TransitionAnimation.fade(itemElevenImg, 1.0);
    TransitionAnimation.fade(itemTwelveImg, 1.0);
    TransitionAnimation.fade(itemThirteenImg, 1.0);
    TransitionAnimation.fade(itemFourteenImg, 1.0);
    TransitionAnimation.fade(itemFifteenImg, 1.0);
  }

  /**
   * Taking the user to the library room from the room scene. Only
   * called when the user is clicking the left room arrow to move
   * scenes.
   *
   * @param event The mouse event.
   * @throws URISyntaxException If the URI is invalid.
   */
  @FXML
  public void goLeftRoom(MouseEvent event) throws URISyntaxException {
    if (!GameState.isBookRiddleResolved) {
      notificationText.setText(
          "The Wizard has some instructions for you! Talk to him first!");
      Notification.notifyPopup(notificationBack, notificationText);
    } else {
      // If you've solved the riddle, you can freely travel between rooms
      System.out.println("CAULDRON_ROOM -> LIBRARY_ROOM");
      itemScroll.setOpacity(0);
      // RoomController.goDirection(pane, AppUi.LIBRARY_ROOM);
      Scene currentScene = fadeRectangle.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.LIBRARY_ROOM));
      SceneManager.getLibraryRoomControllerInstance().fadeIn();
    }
  }

  /**
   * Taking the user to the treasure room from the room scene. Only
   * called when the user is clicking the right room arrow to move
   * scenes.
   *
   * @param event The mouse event.
   * @throws URISyntaxException If the URI is invalid.
   */
  @FXML
  public void goRightRoom(MouseEvent event) throws URISyntaxException {
    if (!GameState.isBookRiddleResolved) {
      notificationText.setText(
          "The Wizard has some instructions for you! Talk to him first!");
      Notification.notifyPopup(notificationBack, notificationText);
    } else {
      // If you've solved the riddle, you can freely travel between rooms
      System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
      itemScroll.setOpacity(0);
      //RoomController.goDirection(pane, AppUi.TREASURE_ROOM);
      Scene currentScene = fadeRectangle.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(AppUi.TREASURE_ROOM));
      SceneManager.getTreasureRoomControllerInstance().fadeIn();
    }
  }

  /**
   * Handling events where menus or views need to be exited by clicking 
   * anywhere else on the screen. In this case, the user is clicking off
   * the cauldron room.
   *
   * @param event The mouse event.
   * @throws URISyntaxException If the URI is invalid.
   */
  @FXML
  public void clickOffCauldronRoom(MouseEvent event) throws URISyntaxException {
    if (GameState.isBookRiddleResolved) {
      System.out.println("click off");
      setText("", false, false);
      chooseLabel.setOpacity(0);
      enableRecipe();
      toggleChat(true, 0);
      toggleBooks(true, 0);
      itemDefault();
      loadingImage.setOpacity(0);
      loadingImage.setDisable(true);

      TransitionAnimation.fade(cancelTtsBtn, 0.0);
      cancelTtsBtn.setDisable(true);

      // Handling closing the "bag" when clicking off inventory
      if (bagOpened) {
        itemScroll.setOpacity(0);
        soundEffects.playSound("closeBag.mp3");
        bagOpened = false;
        System.out.println("Bag closed");
      }
    }
  }

  /**
   * Enabling the recipe book button to be able to be clicked on.
   * Making the button visible to the user. Also, showing the
   * notification to the user that the recipe book is available.
   *
   * @throws URISyntaxException If the URI is invalid.
   */
  @FXML
  private void enableRecipe() throws URISyntaxException {
    bookBtn.setDisable(false);
    bookBtn.setOpacity(1);
    // Showing the notification to the user that the recipe book is available
    if (showRecipe) {
      notificationText.setText("Check bottom right for the recipe book!");
      Notification.notifyPopup(notificationBack, notificationText);
    }
    showRecipe = false;
  }

  /**
   * Taking the user to the book scene from the room scene. Only
   * called when the user is clicking the recipe book button.
   *
   * @throws URISyntaxException If the sound file cannot be found.
   */
  @FXML
  public void openBook() throws URISyntaxException {
    System.out.println("CAULDRON_ROOM -> BOOK");
    //RoomController.openBook(AppUi.CAULDRON_ROOM, pane);
    soundEffects.playSound("openBook.wav");
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.BOOK));
    SceneManager.getBookControllerInstance().fadeIn();
    SceneManager.currScene = AppUi.CAULDRON_ROOM;
    SceneManager.getBookControllerInstance().updateBackground();
  }

  /**
   * Fading in the cauldron room scene. Called when the user is
   * transitioning from another scene to the cauldron room scene.
   */
  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }
}
