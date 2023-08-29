package nz.ac.auckland.se206.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.gpt.ChatHandler;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class CauldronRoomController {
  @FXML private Rectangle cauldronRectangle;
  @FXML private Rectangle wizardRectangle;
  @FXML private Polygon rightArrow;
  @FXML private Polygon leftArrow;
  @FXML private Rectangle bookFireRectangle;
  @FXML private Rectangle bookWaterRectangle;
  @FXML private Rectangle bookAirRectangle;

  @FXML private ShapeInteractionHandler interactionHandler;
  boolean wizardFirstTime = true;
  private String book;
  private String[] options = {"fire", "water", "air"};

  @FXML
  public void initialize() {
    interactionHandler = new ShapeInteractionHandler();

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
      Task<Void> bookRiddleTask =
          new Task<Void>() {

            @Override
            protected Void call() throws Exception {
              String response = chatHandler.runGpt(GptPromptEngineering.getBookRiddle(book));
              System.out.println(response);
              return null;
            }
          };
      new Thread(bookRiddleTask).start();

      wizardFirstTime = false;
    } else {

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
    System.out.println("CAULDRON ROOM > SHELF LEFT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_LEFT));
  }

  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("CAULDRON ROOM > SHELF RIGHT");
    Scene currentScene = cauldronRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.SHELF_RIGHT));
  }

  @FXML
  public void glowThis(Shape shape) {
    shape.setStrokeWidth(5);
  }

  @FXML
  private void unglowThis(Shape shape) {
    shape.setStroke(null); // Remove the stroke to "unglow"
  }

  private String getRandomBook() {
    int randomIndex = (int) (Math.random() * options.length);
    System.out.println(options[randomIndex]);
    return options[randomIndex];
  }
}
