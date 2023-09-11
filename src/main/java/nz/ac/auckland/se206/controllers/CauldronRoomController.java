package nz.ac.auckland.se206.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.gpt.ChatHandler;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class CauldronRoomController {
  @FXML private Rectangle cauldronRectangle;
  @FXML private Rectangle wizardRectangle;
  @FXML private Polygon rightArrow;
  @FXML private Polygon leftArrow;
  @FXML private Rectangle bookFireRectangle;
  @FXML private Rectangle bookWaterRectangle;
  @FXML private Rectangle bookAirRectangle;
  @FXML private Rectangle bookFireImage;
  @FXML private Rectangle bookWaterImage;
  @FXML private Rectangle bookAirImage;
  @FXML private Rectangle textRect;
  @FXML private Rectangle wizardChatImage;
  @FXML private Rectangle mouseTrackRegion;
  @FXML private ImageView bookBtn;
  @FXML private Label timerLabel;
  @FXML private ScrollPane calItemScroll;
  @FXML private Label riddleSelectLabel;
  @FXML private Label chooseLabel;

  @FXML private ShapeInteractionHandler interactionHandler;
  private ChatHandler chatHandler = new ChatHandler();
  boolean wizardFirstTime = true;
  private String book;
  private String[] options = {"fire", "water", "air"};
  private String riddle;

  public boolean bagOpened;

  private CountdownTimer countdownTimer;

  @FXML
  public void initialize() {
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
    Task<Void> bookRiddleTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            // riddle = chatHandler.runGpt(GptPromptEngineering.getBookRiddle(book));
            // System.out.println(riddle);
            return null;
          }
        };
    new Thread(bookRiddleTask).start();
    System.out.println(riddle);
    // Some type of animation
    // bookBtn.setOnMouseEntered(event -> interactionHandler.handle(event));
    // bookBtn.setOnMouseExited(event -> interactionHandler.handle(event));
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
    System.out.println("cauldron clicked");
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
    cauldronRectangle.getScene().setRoot(SceneManager.getUiRoot(AppUi.SHELF_LEFT));
    SceneManager.setTimerScene(AppUi.SHELF_LEFT);
  }

  @FXML
  public void goRight(MouseEvent event) {
    calItemScroll.setOpacity(0);
    bagOpened = false;
    System.out.println("CAULDRON_ROOM -> TREASURE_ROOM");
    cauldronRectangle.getScene().setRoot(SceneManager.getUiRoot(AppUi.SHELF_RIGHT));
    SceneManager.setTimerScene(AppUi.SHELF_RIGHT);
  }

  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    wizardChatImage.setOpacity(0);
    textRect.setDisable(true);
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
  //   shape.setStroke(Color.GOLD);
  //   shape.setStrokeWidth(5);
  // }

  // @FXML
  // public void unhighlightThis(Shape shape) {
  //   shape.setStrokeWidth(0);
  //   shape.setStroke(Color.BLACK);
  // }

  private String getRandomBook() {
    int randomIndex = (int) (Math.random() * options.length);
    System.out.println(options[randomIndex]);
    return options[randomIndex];
  }

  private void showWizardChat() {
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

  /** Dealing with the event where the bag icon is clicked */
  @FXML
  public void clickBag() {
    if (MainMenuController.inventory.size() == 0) return;
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
    riddleSelectLabel.setFont(javafx.scene.text.Font.font("System", 12));
    riddleSelectLabel.setText(riddle);
    chooseLabel.setOpacity(100);
    enableBooks();
  }
}
