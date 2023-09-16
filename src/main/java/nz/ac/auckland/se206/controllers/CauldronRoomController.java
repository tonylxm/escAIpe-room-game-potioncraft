package nz.ac.auckland.se206.controllers;

import java.io.IOException;

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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.gpt.ChatHandler;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class CauldronRoomController {
  @FXML
  private Rectangle cauldronRectangle;
  @FXML
  private Rectangle wizardRectangle;
  @FXML
  private Polygon rightArrow;
  @FXML
  private Polygon leftArrow;
  @FXML
  private Rectangle bookFireRectangle;
  @FXML
  private Rectangle bookWaterRectangle;
  @FXML
  private Rectangle bookAirRectangle;
  @FXML
  private Rectangle bookFireImage;
  @FXML
  private Rectangle bookWaterImage;
  @FXML
  private Rectangle bookAirImage;
  @FXML
  private Rectangle textRect;
  @FXML
  private Rectangle wizardChatImage;
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
  private ShapeInteractionHandler interactionHandler;
  private ChatHandler chatHandler = new ChatHandler();
  private boolean wizardFirstTime = true;
  private String book;
  private String[] options = { "fire", "water", "air" };
  private String riddle;

  private boolean bagOpened;

  private CountdownTimer countdownTimer;
  private ChatCompletionRequest chatCompletionRequest;
  private Choice result;

  @FXML
  public void initialize() throws ApiProxyException {
    bagOpened = false;
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setCauldronTimerLabel(timerLabel);
    interactionHandler = new ShapeInteractionHandler();
    // highlightThis(wizardRectangle);
    mouseTrackRegion.setDisable(true);
    textRect.setDisable(true);
    riddleSelectLabel.setDisable(true);
    disableBooks();
    mouseTrackRegion.setOpacity(0);
    book = getRandomBook();

    if (cauldronRectangle != null) {
      cauldronRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      cauldronRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    if (wizardRectangle != null) {
      wizardRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      wizardRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    if (rightArrow != null) {
      rightArrow.setOnMouseEntered(event -> rightArrow.setOpacity(0.9));
      rightArrow.setOnMouseExited(event -> rightArrow.setOpacity(0.5));
    }
    if (leftArrow != null) {
      leftArrow.setOnMouseEntered(event -> leftArrow.setOpacity(0.9));
      leftArrow.setOnMouseExited(event -> leftArrow.setOpacity(0.5));
    }
    if (bookBtn != null) {
      bookBtn.setOnMouseEntered(event -> interactionHandler.glowThis(bookBtn));
      bookBtn.setOnMouseExited(event -> interactionHandler.unglowThis(bookBtn));
    }
    if (bagBtn != null) {
      bagBtn.setOnMouseEntered(event -> interactionHandler.glowThis(bagBtn));
      bagBtn.setOnMouseExited(event -> interactionHandler.unglowThis(bagBtn));
      // ELSE NO ITEMS IN BAG MESSAGE
    }
    if (bookFireRectangle != null) {
      bookFireRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      bookFireRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    if (bookWaterRectangle != null) {
      bookWaterRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      bookWaterRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    if (bookAirRectangle != null) {
      bookAirRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      bookAirRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    try {
      chatHandler.initialize();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
    Task<Void> bookRiddleTask = new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        // riddle = chatHandler.runGpt(GptPromptEngineering.getBookRiddle(book));
        // System.out.println(riddle);
        return null;
      }
    };
    new Thread(bookRiddleTask).start();
    System.out.println(riddle);

    chatCompletionRequest = new ChatCompletionRequest()
        .setN(1).setTemperature(0.2)
        .setTopP(0.5).setMaxTokens(50);

    // Run GPT to generate hints for the game
    // Task<Void> runGptTask = new Task<Void>() {
    //   @Override
    //   protected Void call() throws Exception {
    //     
    //     runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("vase")));
    //     return null;
    //   }
    // };
    // new Thread(runGptTask, "runGpt Thread").start();
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

  @FXML
  public void clickCauldron(MouseEvent event) {
    CauldronController cauldronController = SceneManager.getCauldronControllerInstance();
if (cauldronController != null) {
    cauldronController.updateImageStates();
}
    System.out.println("cauldron clicked");
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> CAULDRON");
    cauldronRectangle.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON));
    SceneManager.setTimerScene(AppUi.CAULDRON);
    System.out.println(Items.necessary);
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleResolved) {

      showWizardChat();

      wizardFirstTime = false;
      GameState.isBookRiddleGiven = true;
      // unhighlightThis(wizardRectangle);
    } else {
      // showWizardChat();
    }
  }

  @FXML
  public void clickBookFire(MouseEvent event) {
    System.out.println("book fire clicked");
    if (book == "fire") {
      // remove the book from the scene
      bookFireRectangle.setOpacity(0);
      bookFireImage.setOpacity(0);
      bookFireImage.setDisable(true);
      bookFireRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      riddleSelectLabel.setText(
          "You've done well to solve the riddle. The rest is now up to you my apprentice, if you"
              + " wish to ask anything of me write in the book and I will respond.");
    }
  }

  @FXML
  public void clickBookWater(MouseEvent event) {
    System.out.println("book water clicked");
    if (book == "water") {
      // remove the book from the scene
      bookWaterRectangle.setOpacity(0);
      bookWaterImage.setOpacity(0);
      bookWaterImage.setDisable(true);
      bookWaterRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      riddleSelectLabel.setText(
          "You've done well to solve the riddle. The rest is now up to you my apprentice, if you"
              + " wish to ask anything of me write in the book and I will respond.");
    }
  }

  @FXML
  public void clickBookAir(MouseEvent event) {
    System.out.println("book air clicked");
    if (book == "air") {
      // remove the book from the scene
      bookAirRectangle.setOpacity(0);
      bookAirImage.setOpacity(0);
      bookAirImage.setDisable(true);
      bookAirRectangle.setDisable(true);
      GameState.isBookRiddleResolved = true;
      chooseLabel.setOpacity(0);
      riddleSelectLabel.setText(
          "You've done well to solve the riddle. The rest is now up to you my apprentice, if you"
              + " wish to ask anything of me write in the book and I will respond.");
    }
  }

  @FXML
  public void goLeft(MouseEvent event) {
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> LIBRARY_ROOM");
    cauldronRectangle.getScene().setRoot(SceneManager.getUiRoot(AppUi.LIBRARY_ROOM));
    SceneManager.setTimerScene(AppUi.LIBRARY_ROOM);
  }

  @FXML
  public void goRight(MouseEvent event) {
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
    cauldronRectangle.getScene().setRoot(SceneManager.getUiRoot(AppUi.TREASURE_ROOM));
    SceneManager.setTimerScene(AppUi.TREASURE_ROOM);
  }

  /**
   * Handling events where menus or views need to be exited by clicking anywhere
   * else on the screen
   * 
   * @param event
   */
  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    wizardChatImage.setOpacity(0);
    textRect.setDisable(true);
    // Disabling mouseTrackRegion so it doesn't interfere with other interactions
    mouseTrackRegion.setDisable(true);
    textRect.setOpacity(0);
    mouseTrackRegion.setOpacity(0);
    riddleSelectLabel.setDisable(true);
    riddleSelectLabel.setOpacity(0);
    riddleSelectLabel.setText(">Riddle");
    riddleSelectLabel.setFont(javafx.scene.text.Font.font("System", 24));
    disableBooks();
    chooseLabel.setOpacity(0);

    if (GameState.isBookRiddleResolved) {
      wizardRectangle.setDisable(true);
      wizardRectangle.setOpacity(0);
    }

    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      calItemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
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

  /**
   * Displaying wizard chat to user when prompted
   */
  private void showWizardChat() {
    // Setting approrpiate fields to be visible and interactable
    wizardChatImage.setOpacity(100);
    textRect.setDisable(false);
    mouseTrackRegion.setDisable(false);
    textRect.setOpacity(100);
    riddleSelectLabel.setDisable(false);
    riddleSelectLabel.setOpacity(100);
    mouseTrackRegion.setOpacity(0.5);
  }

  @FXML
  public void openBook() {
    System.out.println("CAULDRON_ROOM -> BOOK");
    SceneManager.currScene = AppUi.CAULDRON_ROOM;
    bookBtn.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /**
   * Dealing with the event where the bag icon is clicked
   */
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

  @FXML
  public void riddleSelect() {
    riddleSelectLabel.setFont(javafx.scene.text.Font.font("Algerian", 12));
    riddleSelectLabel.setText(riddle);
    chooseLabel.setOpacity(100);
    enableBooks();
  }

  /**
   * Handles when a key is pressed on the book scene.
   */
  @FXML
  public void onKeyPressed(KeyEvent event) throws ApiProxyException {
    System.out.println("key " + event.getCode() + " pressed");
    if (event.getCode().toString().equals("ENTER")) {
      String message = inputText.getText();
      // Using same GPT message sending logic as previous
      if (message.trim().isEmpty()) {
        return;
      }
      inputText.clear();
      ChatMessage msg = new ChatMessage("user", message);
      appendChatMessage(msg);
      

      Task<Void> runGptTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        ChatMessage lastMsg = runGpt(msg);

        if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")) {
        GameState.isBookRiddleResolved = true;
      }
        return null;
      }
    };
    new Thread(runGptTask, "runGpt Thread").start();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": ");

    Task<Void> appendTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        for (char c : msg.getContent().toCharArray()) {
          chatTextArea.appendText(String.valueOf(c));
          Thread.sleep(10);
        }
        chatTextArea.appendText("\n\n");
        return null;
      }
    };
    new Thread(appendTask, "Append Thread").start();
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      appendChatMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   * @throws IOException       if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    // Not doing anything if there is no message
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    ChatMessage lastMsg = runGpt(msg);
    // If the riddle is answered correctly, setting the GameState accordingly
    if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")) {
      GameState.isBookRiddleResolved = true;
    }
  }

  /**
   * Uses text to speech to read the game master's response to the user's
   * message.
   */
  public void readGameMasterResponse() {
    // Using concurency to prevent the system freezing
    Task<Void> speakTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        App.textToSpeech.speak(result.getChatMessage().getContent());
        return null;
      }
    };
    Thread speakThread = new Thread(speakTask, "Speak Thread");
    speakThread.start();
  }
}
