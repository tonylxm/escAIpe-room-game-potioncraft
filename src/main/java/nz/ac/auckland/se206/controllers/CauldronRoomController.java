package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class CauldronRoomController {
  @FXML
  private Pane pane;
  @FXML
  private ImageView cauldronImg;
  @FXML
  private ImageView wizardImg;
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
  private Rectangle textRect;
  @FXML
  private ImageView wizardChatImage;
  @FXML
  private Rectangle mouseTrackRegion;
  @FXML
  private ImageView bookBtn;
  @FXML
  private ImageView bagBtn;
  @FXML
  private Label timerLabel;
  @FXML
  private ScrollPane calItemScroll;
  @FXML
  private Label riddleSelectLabel;
  @FXML
  private Label chooseLabel;

  @FXML 
  private TextArea chatTextArea;
  @FXML 
  private TextField inputText;
  @FXML 
  private Button sendButton;
  @FXML 
  private ImageView ttsBtn2;

  @FXML
  private ImageView notificationBack;
  @FXML
  private Label notificationText;

  private ChatMessage riddleSolveMsg;
  private boolean bagOpened;
  private CountdownTimer countdownTimer;
  private boolean showRecipe = true;

  @FXML
  public void initialize() {
    // Setting up the countdown and appropriate booleans before
    // anything happens to change them within the game
    bagOpened = false;
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setCauldronTimerLabel(timerLabel);

    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    mouseTrackRegion.setDisable(true);
    textRect.setDisable(true);
    toggleChat(true, 0);
    toggleBooks(true, 0);
    mouseTrackRegion.setOpacity(0);

    // Setting up the appropriate interactions for the cauldron, wizard, arrows, book button, bag button, book fire, book water, book air
    ItemRoomController.btnMouseActions(cauldronImg);
    ItemRoomController.btnMouseActions(wizardImg);
    ItemRoomController.arrowMouseActions(leftArrow);
    ItemRoomController.arrowMouseActions(rightArrow);
    ItemRoomController.btnMouseActions(bookBtn);
    ItemRoomController.btnMouseActions(bagBtn);
    ItemRoomController.btnMouseActions(bookFireRectangle);
    ItemRoomController.btnMouseActions(bookWaterRectangle);
    ItemRoomController.btnMouseActions(bookAirRectangle);

    // Message to be displayed when the user selected the correct book
    riddleSolveMsg =
        new ChatMessage(
            "Wizard",
            "You've done well to solve the riddle. The rest is now up to you. If you"
                + " require any assistance, please come talk to me again.");
  }

  /**
   * Enabling/disabling the chat functionality for the user to be able to talk to the wizard.
   */
  private void toggleChat(boolean disable, int opacity) {
    // Enabling/disabling the approrpiate fields and making everything invisible/visible
    chatTextArea.setDisable(disable);
    inputText.setDisable(disable);
    sendButton.setDisable(disable);
    ttsBtn2.setDisable(disable);
    textRect.setDisable(disable);
    mouseTrackRegion.setDisable(disable);
    wizardChatImage.setDisable(disable);
    chatTextArea.setOpacity(opacity);
    inputText.setOpacity(opacity);
    sendButton.setOpacity(opacity);
    ttsBtn2.setOpacity(opacity);
    textRect.setOpacity(opacity);
    wizardChatImage.setOpacity(opacity);
    if (opacity == 0) {
      mouseTrackRegion.setOpacity(0);
    } else {
      mouseTrackRegion.setOpacity(0.5);
    }
  }

  private void toggleBooks(boolean disable, int opacity) {
    bookFireRectangle.setDisable(disable);
    bookWaterRectangle.setDisable(disable);
    bookAirRectangle.setDisable(disable);
    bookFireRectangle.setOpacity(opacity);
    bookWaterRectangle.setOpacity(opacity);
    bookAirRectangle.setOpacity(opacity);
  }

  private void disableChat(boolean disable, double opacity) {
    inputText.setDisable(disable);
    inputText.setOpacity(opacity);
    sendButton.setDisable(disable);
    sendButton.setOpacity(opacity);
  }

  /**
   * Taking user to the cauldron scene from the room scene to be able to 
   * brew their potions.
   * 
   * @param event
   */
  @FXML
  public void clickCauldron(MouseEvent event) {
    CauldronController cauldronController = SceneManager.getCauldronControllerInstance();
    if (cauldronController != null) {
      cauldronController.updateImageStates();
    }

    // If the cauldronController exists, then switching scenes
    System.out.println("cauldron clicked");
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> CAULDRON");
    TransitionAnimation.changeScene(pane, AppUi.CAULDRON, false);
    SceneManager.setTimerScene(AppUi.CAULDRON);
    System.out.println(Items.necessary);
  }
  
  /**
   * Taking the user to the wizard's chat functionality.
   * Also, if the user hasn't been prompted with the riddle yet,
   * showing it to them too.
   * 
   * @param event
   * @throws InterruptedException
   */
  @FXML
  public void clickWizard(MouseEvent event) throws InterruptedException {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleGiven) {
      toggleChat(false, 1);
      disableChat(true, 0.5);
      MainMenuController.getChatHandler().appendChatMessage(MainMenuController.getRiddle(), chatTextArea, inputText, sendButton);

      // After the riddle scrolling text animation has finished, then allowing
      // the user to select the book and respond to the wizard
      MainMenuController.getChatHandler().getAppendTask().setOnSucceeded(e -> {
        chooseLabel.setOpacity(1);
        disableChat(false, 1);
        toggleBooks(false, 1);
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

  private void handleClickBooks(String element, ImageView bookImage, ImageView bookRectangle) {
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
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      MainMenuController.getChatHandler().appendChatMessage(riddleSolveMsg, chatTextArea, inputText, sendButton);
    }
  }

  @FXML
  public void goLeft(MouseEvent event) {
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> LIBRARY_ROOM");
    TransitionAnimation.changeScene(pane, AppUi.LIBRARY_ROOM, false);
    SceneManager.setTimerScene(AppUi.LIBRARY_ROOM);
  }

  @FXML
  public void goRight(MouseEvent event) {
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
    TransitionAnimation.changeScene(pane, AppUi.TREASURE_ROOM, false);
    SceneManager.setTimerScene(AppUi.TREASURE_ROOM);
  }

  /**
   * Handling events where menus or views need to be exited by clicking anywhere else on the screen
   *
   * @param event
   */
  @FXML
  public void clickOff(MouseEvent event) {
    if (GameState.isBookRiddleResolved) {
      System.out.println("click off");

      // Disabling mouseTrackRegion so it doesn't interfere with other interactions
      mouseTrackRegion.setDisable(true);

      wizardChatImage.setDisable(true);
      wizardChatImage.setOpacity(0);

      textRect.setDisable(true);
      
      textRect.setOpacity(0);
      mouseTrackRegion.setOpacity(0);
      chatTextArea.setDisable(true);
      chatTextArea.setOpacity(0);
      chooseLabel.setOpacity(0);
      enableRecipe();
      toggleChat(true, 0);
      toggleBooks(true, 0);

      // Handling closing the "bag" when clicking off inventory
      if (bagOpened) {
        calItemScroll.setOpacity(0);
        bagOpened = false;
        System.out.println("Bag closed");
      }
    }
  }

  @FXML
  private void enableRecipe() {
    bookBtn.setDisable(false);
    bookBtn.setOpacity(1);

    if(showRecipe) {
      notificationText.setText("Check bottom right for the recipe book!");
      notifyPopup();
    }

    showRecipe = false;
  }

  /**
   * Taking the user to the book scene from the room scene to be able to
   */
  @FXML
  public void openBook() {
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
      // Updating the book scene to reflect the current state of the book
      bookController.updateBackground();
    }
    System.out.println("CAULDRON_ROOM -> BOOK");
    SceneManager.currScene = AppUi.CAULDRON_ROOM;
    TransitionAnimation.changeScene(pane, AppUi.BOOK, false);
  }

  /** Dealing with the event where the bag icon is clicked */
  @FXML
  public void clickBag() {
    // If there are no items in the inventory, can't open the bag
    if (MainMenuController.inventory.size() == 0) {
      notificationText.setText("You have no ingredients in your bag!");
      notifyPopup();
      return;
    }
    // If the bag isn't opened already, open it
    if (!bagOpened) {
      calItemScroll.setVvalue(0);
      calItemScroll.setContent(null);
      calItemScroll.setContent(MainMenuController.getInventory().getBox());
      calItemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    // Not doing anything if there is no message
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    disableChat(true, 0.5);
    ChatMessage msg =
        new ChatMessage("user", message);
    MainMenuController.getChatHandler().appendChatMessage(msg, chatTextArea, inputText, sendButton);

    Task<Void> runGptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            MainMenuController.getChatHandler().runGptGameMaster(msg, chatTextArea, inputText, sendButton);
            return null;
          }
        };
    new Thread(runGptTask).start();
  }

  /**
   * Handles when ENTER is pressed on the input text area.
   *
   * @throws IOException
   */
  @FXML
  public void onEnterPressed(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode().toString().equals("ENTER")) {
      System.out.println("key " + event.getCode() + " pressed");
      onSendMessage(new ActionEvent());
    }
  }

  /** Uses text to speech to read the game master's response to the user's message. */
  public void readGameMasterResponse() {
    // Using concurency to prevent the system freezing
    Task<Void> speakTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // need to update the chat text area with the game master's response & riddle
            App.textToSpeech.speak(chatTextArea.getText());
            return null;
          }
        };
    new Thread(speakTask, "Speak Thread").start();
  }

  @FXML
  private void notifyPopup() {
    // Create a FadeTransition to gradually change opacity over 3 seconds
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5), notificationBack);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(1.0);
            FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(5), notificationText);
            fadeTransition2.setFromValue(1.0);
            fadeTransition2.setToValue(1.0);

            // Play the fade-in animation
            fadeTransition.play();
            fadeTransition2.play();

            // Schedule a task to fade out the image after 3 seconds
            fadeTransition.setOnFinished(fadeEvent -> {
              if (notificationBack.getOpacity() == 1.0) {
                FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1.5), notificationBack);
                fadeOutTransition.setFromValue(1.0);
                fadeOutTransition.setToValue(0.0);
                fadeOutTransition.play();
              }
            });

            fadeTransition2.setOnFinished(fadeEvent -> {
              if (notificationText.getOpacity() == 1.0) {
                FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1.5), notificationText);
                fadeOutTransition.setFromValue(1.0);
                fadeOutTransition.setToValue(0.0);
                fadeOutTransition.play();
              }
            });

            
  }
}
