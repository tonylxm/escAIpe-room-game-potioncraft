package nz.ac.auckland.se206.controllers;

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
  @FXML private Rectangle textRect;
  @FXML private Rectangle wizardChatImage;
  @FXML private Rectangle mouseTrackRegion;
  @FXML private ImageView bookBtn;
  @FXML private Label timerLabel;
  @FXML private ScrollPane calItemScroll;

  @FXML private ShapeInteractionHandler interactionHandler;
  boolean wizardFirstTime = true;
  private String book;
  private String[] options = {"fire", "water", "air"};

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
    mouseTrackRegion.setOpacity(0);

    if (cauldronRectangle != null) {
      cauldronRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      cauldronRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (wizardRectangle != null) {
      wizardRectangle.setOnMouseEntered(event -> interactionHandler.handle(event));
      wizardRectangle.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (rightArrow != null) {
      rightArrow.setOnMouseEntered(event -> interactionHandler.handle(event));
      rightArrow.setOnMouseExited(event -> interactionHandler.handle(event));
    }

    if (leftArrow != null) {
      leftArrow.setOnMouseEntered(event -> interactionHandler.handle(event));
      leftArrow.setOnMouseExited(event -> interactionHandler.handle(event));
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
    // Some type of animation
    // bookBtn.setOnMouseEntered(event -> interactionHandler.handle(event));
    // bookBtn.setOnMouseExited(event -> interactionHandler.handle(event));
  }

  @FXML
  public void clickCauldron(MouseEvent event) {
    System.out.println("cauldron clicked");
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
    if (wizardFirstTime) {

      book = getRandomBook();

      ChatHandler chatHandler = new ChatHandler();
      try {
        chatHandler.initialize();
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
      // Task<Void> bookRiddleTask =
      //     new Task<Void>() {

      //       @Override
      //       protected Void call() throws Exception {
      //         String response = chatHandler.runGpt(GptPromptEngineering.getBookRiddle(book));
      //         System.out.println(response);
      //         return null;
      //       }
      //     };
      showWizardChat();
      // new Thread(bookRiddleTask).start();

      wizardFirstTime = false;
      GameState.isBookRiddleGiven = true;
      // unhighlightThis(wizardRectangle);
    } else {
      showWizardChat();
    }
  }

  @FXML
  public void clickBookFire(MouseEvent event) {
    System.out.println("book fire clicked");
  }

  @FXML
  public void clickBookWater(MouseEvent event) {
    System.out.println("book water clicked");
  }

  @FXML
  public void clickBookAir(MouseEvent event) {
    System.out.println("book air clicked");
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
    System.out.println("CAULDRON_ROOM > LIBRARY_ROOM");
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
    mouseTrackRegion.setOpacity(0.5);
  }

  @FXML
  public void openBook() {
    System.out.println("CAULDRON_ROOM > BOOK");
    SceneManager.currScene = AppUi.CAULDRON_ROOM;
    bookBtn.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /** Dealing with the event where the bag icon is clicked */
  @FXML
  public void clickBag() {
    if (MainMenuController.inventory.size() == 0) return;
    if (!bagOpened) {
      calItemScroll.setContent(null);
      calItemScroll.setContent(MainMenuController.inventory.box);
      calItemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }
}
