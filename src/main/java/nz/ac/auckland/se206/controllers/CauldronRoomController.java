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
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatHandler;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
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

  private ShapeInteractionHandler interactionHandler;
  private ChatHandler chatHandler = new ChatHandler();
  private String book;
  private String[] options = {"fire", "water", "air"};
  private ChatMessage riddle;
  private ChatMessage riddleSolveMsg;

  private boolean bagOpened;

  private CountdownTimer countdownTimer;

  @FXML
  public void initialize() {
    bagOpened = false;
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setCauldronTimerLabel(timerLabel);
    interactionHandler = new ShapeInteractionHandler();
    GameState.isBookRiddleGiven = false;
    GameState.isBookRiddleResolved = false;
    // highlightThis(wizardRectangle);
    mouseTrackRegion.setDisable(true);
    textRect.setDisable(true);
    disableBooks();
    mouseTrackRegion.setOpacity(0);
    book = getRandomBook();

    if (cauldronImg != null) {
      cauldronImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(cauldronImg));
      cauldronImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(cauldronImg));
      
    }
    if (wizardImg != null) {
      wizardImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(wizardImg));
      wizardImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(wizardImg));
    }
    if (rightArrow != null) {
      rightArrow.setOnMouseEntered(
          event -> rightArrow.setOpacity(0.9));
      rightArrow.setOnMouseExited(
          event -> rightArrow.setOpacity(0.5));
    }
    if (leftArrow != null) {
      leftArrow.setOnMouseEntered(
          event -> leftArrow.setOpacity(0.9));
      leftArrow.setOnMouseExited(
          event -> leftArrow.setOpacity(0.5));
    }
    if (bookBtn != null) {
      bookBtn.setOnMouseEntered(
          event -> interactionHandler.glowThis(bookBtn));
      bookBtn.setOnMouseExited(
          event -> interactionHandler.unglowThis(bookBtn));
    }
    if (bagBtn != null) {
      bagBtn.setOnMouseEntered(
          event -> interactionHandler.glowThis(bagBtn));
      bagBtn.setOnMouseExited(
          event -> interactionHandler.unglowThis(bagBtn));
      // ELSE NO ITEMS IN BAG MESSAGE
    }
    
    //glow the books on hover
    if (bookFireRectangle != null) {
      bookFireRectangle.setOnMouseEntered(
          event -> interactionHandler.glowThis(bookFireRectangle));
      bookFireRectangle.setOnMouseExited(
          event -> interactionHandler.unglowThis(bookFireRectangle));
    }
    if (bookWaterRectangle != null) {
      bookWaterRectangle.setOnMouseEntered(
          event -> interactionHandler.glowThis(bookWaterRectangle));
      bookWaterRectangle.setOnMouseExited(
          event -> interactionHandler.unglowThis(bookWaterRectangle));
    }
    if (bookAirRectangle != null) {
      bookAirRectangle.setOnMouseEntered(
          event -> interactionHandler.glowThis(bookAirRectangle));
      bookAirRectangle.setOnMouseExited(
          event -> interactionHandler.unglowThis(bookAirRectangle));
    }

    try {
      chatHandler.initialize();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
    Task<Void> bookRiddleTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            riddle =
                new ChatMessage(
                    "Wizard", chatHandler.runGpt(GptPromptEngineering.getBookRiddle(book)));
            return null;
          }
        };
    new Thread(bookRiddleTask).start();
    System.out.println(riddle);

    riddleSolveMsg =
        new ChatMessage(
            "Wizard",
            "You've done well to solve the riddle. The rest is now up to you. If you"
                + " require any assistance, please come talk to me again.");

    // Force the user to talk to the wizard first and solve book riddle
  }

  private void disableChat() {
    chatTextArea.setDisable(true);
    inputText.setDisable(true);
    sendButton.setDisable(true);
    ttsBtn2.setDisable(true);
    wizardChatImage.setDisable(true);
    chatTextArea.setOpacity(0);
    inputText.setOpacity(0);
    sendButton.setOpacity(0);
    ttsBtn2.setOpacity(0);
    wizardChatImage.setOpacity(0);
  }

  private void enableChat() {
    chatTextArea.setDisable(false);
    inputText.setDisable(false);
    sendButton.setDisable(false);
    ttsBtn2.setDisable(false);
    wizardChatImage.setDisable(false);
    chatTextArea.setOpacity(1);
    inputText.setOpacity(1);
    sendButton.setOpacity(1);
    ttsBtn2.setOpacity(1);
    wizardChatImage.setOpacity(1);
  }

  private void disableBooks() {
    bookFireRectangle.setDisable(true);
    bookWaterRectangle.setDisable(true);
    bookAirRectangle.setDisable(true);
    bookFireRectangle.setOpacity(0);
    bookWaterRectangle.setOpacity(0);
    bookAirRectangle.setOpacity(0);
  }

  private void enableBooks() {
    bookFireRectangle.setDisable(false);
    bookWaterRectangle.setDisable(false);
    bookAirRectangle.setDisable(false);
    bookFireRectangle.setOpacity(100);
    bookWaterRectangle.setOpacity(100);
    bookAirRectangle.setOpacity(100);
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
  
  @FXML
  public void clickWizard(MouseEvent event) throws InterruptedException {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleGiven ) {
      showWizardChat();
      // mouseTrackRegion.setDisable(true);
      inputText.setDisable(true);
      inputText.setOpacity(0.5);
      sendButton.setDisable(true);
      sendButton.setOpacity(0.5);
      chatHandler.appendChatMessage(riddle, chatTextArea, inputText, sendButton);

      chatHandler.appendTask.setOnSucceeded(e -> {
          chooseLabel.setOpacity(1);
          inputText.setDisable(false);
          inputText.setOpacity(1);
          sendButton.setDisable(false);
          sendButton.setOpacity(1);
          enableBooks();
        });
      GameState.isBookRiddleGiven = true;
    } else {
      showWizardChat();
      enableChat();

    }
  }

  @FXML
  public void clickBookFire(MouseEvent event) {
    System.out.println("book fire clicked");
    if (book == "fire") {
      // remove the book from the scene
      //bookFireRectangle.setOpacity(0);
      //fade the book out
      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookFireRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();
      bookFireImage.setOpacity(0);
      bookFireImage.setDisable(true);
      bookFireRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      chatHandler.appendChatMessage(riddleSolveMsg, chatTextArea, inputText, sendButton);
    }
  }

  @FXML
  public void clickBookWater(MouseEvent event) {
    System.out.println("book water clicked");
    if (book == "water") {
      // remove the book from the scene
      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookWaterRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();
      bookWaterImage.setOpacity(0);
      bookWaterImage.setDisable(true);
      bookWaterRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      chatHandler.appendChatMessage(riddleSolveMsg, chatTextArea, inputText, sendButton);
    }
  }

  @FXML
  public void clickBookAir(MouseEvent event) {
    System.out.println("book air clicked");
    if (book == "air") {
      // remove the book from the scene
      FadeTransition ft = new FadeTransition(Duration.seconds(1), bookAirRectangle);
      ft.setFromValue(1);
      ft.setToValue(0);
      ft.play();
      bookAirImage.setOpacity(0);
      bookAirImage.setDisable(true);
      bookAirRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      chatHandler.appendChatMessage(riddleSolveMsg, chatTextArea, inputText, sendButton);
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
      wizardChatImage.setOpacity(0);
      textRect.setDisable(true);
      // Disabling mouseTrackRegion so it doesn't interfere with other interactions
      mouseTrackRegion.setDisable(true);
      textRect.setOpacity(0);
      mouseTrackRegion.setOpacity(0);
      chatTextArea.setDisable(true);
      chatTextArea.setOpacity(0);
      disableBooks();
      chooseLabel.setOpacity(0);

      disableChat();

      // Handling closing the "bag" when clicking off inventory
      if (bagOpened) {
        calItemScroll.setOpacity(0);
        bagOpened = false;
        System.out.println("Bag closed");
      }
    }
  }

  // @FXML
  // public void highlightThis(Shape shape) {
  // shape.setStroke(Color.GOLD);
  // shape.setStrokeWidth(5);
  // }

  // @FXML
  // public void unhighlightThis(Shape shape) {
  // shape.setStrokeWidth(0);
  // shape.setStroke(Color.BLACK);
  // }

  private String getRandomBook() {
    int randomIndex = (int) (Math.random() * options.length);
    System.out.println(options[randomIndex]);
    return options[randomIndex];
  }

  /** Displaying wizard chat to user when prompted */
  private void showWizardChat() {
    // Setting approrpiate fields to be visible and interactable
    wizardChatImage.setOpacity(1);
    textRect.setDisable(false);
    mouseTrackRegion.setDisable(false);
    textRect.setOpacity(1);
    enableChat();
    mouseTrackRegion.setOpacity(0.5);
  }

  @FXML
  public void openBook() {
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
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
      return;
    }
    // If the bag isn't opened already, open it
    if (!bagOpened) {
      calItemScroll.setVvalue(0);
      calItemScroll.setContent(null);
      calItemScroll.setContent(MainMenuController.inventory.box);
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
    inputText.setDisable(true);
    inputText.setOpacity(0.5);
    sendButton.setDisable(true);
    sendButton.setOpacity(0.5);
    ChatMessage msg =
        new ChatMessage("user", message); // TODO: Cannot change to You without generating error
    chatHandler.appendChatMessage(msg, chatTextArea, inputText, sendButton);

    Task<Void> runGptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Could refactor for hint checking
            ChatMessage lastMsg = chatHandler.runGptGameMaster(msg, chatTextArea, inputText, sendButton);
            // if (lastMsg.getRole().equals("assistant")
            //     && lastMsg.getContent().startsWith("Correct")) {
            //   GameState.isBookRiddleResolved = true;
            // }
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
            App.textToSpeech.speak(chatHandler.result.getChatMessage().getContent());
            return null;
          }
        };
    new Thread(speakTask, "Speak Thread").start();
  }
}
