package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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

  private boolean showRecipe = true;
  private CountdownTimer countdownTimer;

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
   * Setting the text for the riddle select label.
   * @param disable
   * @param opacity
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
   * @param event
   */
  @FXML
  public void clickCauldron(MouseEvent event) {
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
      TransitionAnimation.changeScene(pane, AppUi.CAULDRON, false);
      SceneManager.setTimerScene(AppUi.CAULDRON);
      System.out.println(Items.necessary);
    }
  }
  
  /**
   * Talking the user to the wizard's chat functionality.
   * Also, if the user hasn't been prompted with the riddle yet,
   * showing it to them too.
   * 
   * @param event
   * @throws InterruptedException
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
          mouseTrackRegion.setDisable(true);
          disableChat(true, 0.5);
          return null;
        }
      };
      new Thread(waitForAnimationTask).start();
      // TODO: add half sec delay
      // Thred.sleep(500)
      MainMenuController.getChatHandler().appendChatMessage(MainMenuController.getRiddle(), chatTextArea, inputText, sendButton);

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

  @FXML
  public void clickBookFire(MouseEvent event) {
    handleClickBooks("fire", bookFireImage, bookFireRectangle);
  }

  @FXML
  public void clickBookWater(MouseEvent event) {
    handleClickBooks("water", bookWaterImage, bookWaterRectangle);
  }

  @FXML
  public void clickBookAir(MouseEvent event) {
    handleClickBooks("air", bookAirImage, bookAirRectangle);
  }

  private void handleClickBooks(
      String element, ImageView bookImage, ImageView bookRectangle) {
    System.out.println("book " + element +  " clicked");
    if (MainMenuController.getBook() == element) {
      // remove the book from the scene
      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();

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
      // TODO: change to after appendTask is completed
      new Thread(resolvedTask).start();
      resolvedTask.setOnSucceeded(e -> {
        mouseTrackRegion.setDisable(false);
      });
    }
  }

  /**
   * Enabling the items to be able to be clicked on.
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
   * Taking the user to the library room from the room scene.
   * @param event
   */
  @FXML
  public void goLeftRoom(MouseEvent event) {
    if (!GameState.isBookRiddleResolved) {
      notificationText.setText(
          "The Wizard has some instructions for you! Talk to him first!");
      Notification.notifyPopup(notificationBack, notificationText);
    } else {
      // If you've solved the riddle, you can freely travel between rooms
      System.out.println("CAULDRON_ROOM -> LIBRARY_ROOM");
      itemScroll.setOpacity(0);
      RoomController.goDirection(pane, AppUi.LIBRARY_ROOM);
    }
  }

  /**
   * Taking the user to the treasure room from the room scene.
   * @param event
   */
  @FXML
  public void goRightRoom(MouseEvent event) {
    if (!GameState.isBookRiddleResolved) {
      notificationText.setText(
          "The Wizard has some instructions for you! Talk to him first!");
      Notification.notifyPopup(notificationBack, notificationText);
    } else {
      // If you've solved the riddle, you can freely travel between rooms
      System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
      itemScroll.setOpacity(0);
      RoomController.goDirection(pane, AppUi.TREASURE_ROOM);
    }
  }

  /**
   * Handling events where menus or views need to be exited by clicking 
   * anywhere else on the screen
   *
   * @param event
   */
  @FXML
  public void clickOffCauldronRoom(MouseEvent event) {
    if (GameState.isBookRiddleResolved) {
      System.out.println("click off");
      setText("", false, false);
      chooseLabel.setOpacity(0);
      enableRecipe();
      toggleChat(true, 0);
      toggleBooks(true, 0);

      // Handling closing the "bag" when clicking off inventory
      if (bagOpened) {
        itemScroll.setOpacity(0);
        bagOpened = false;
        System.out.println("Bag closed");
      }
    }
  }

  /**
   * Enabling the recipe book button to be able to be clicked on.
   */
  @FXML
  private void enableRecipe() {
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
   * Taking the user to the book scene from the room scene.
   */
  @FXML
  public void openBook() {
    System.out.println("CAULDRON_ROOM -> BOOK");
    RoomController.openBook(AppUi.CAULDRON_ROOM, pane);
  }
}
