package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LibraryRoomController extends RoomController {
  @FXML 
  private Pane pane;
  @FXML 
  private Polygon rightShpe;
  @FXML 
  private ImageView itemOneImg;
  @FXML
  private ImageView itemTwoImg;
  @FXML
  private ImageView itemThreeImg;
  @FXML
  private ImageView itemFourImg;
  @FXML
  private ImageView itemFiveImg;
  @FXML 
  private Label timerLabel;
  @FXML
  private Rectangle fadeRectangle;

  /**
   * Setting the appropriate fields and listeners when scene is initialised. This includes
   * initialising whether an item is clicked or already added to the inventory, whether an item is
   * ready to be added, whether the bag is already opened, initialising the countdown timer and
   * diabling the mouseTrackRegion appropriately.
   */
  public void initialize() {
    // Initialising everything from the superclass
    genericInitialise("Library", itemOneImg, itemTwoImg, itemThreeImg, itemFourImg, itemFiveImg);
    countdownTimer.setLibraryTimerLabel(timerLabel);
    countdownTimer.setLibraryHintLabel(hintLabel);
    arrowMouseActions(rightShpe);
  }

  /** Changing scenes to the cauldron room */
  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("LIBRARY_ROOM -> CAULDRON_ROOM");
    setText("", false, false);
    itemScroll.setOpacity(0);
    //goDirection(pane, AppUi.CAULDRON_ROOM);
    Scene currentScene = rightShpe.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.getCauldronRoomControllerInstance().fadeIn();
  }

  /** Changing scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("LIBRARY_ROOM -> BOOK");
    //openBook(AppUi.LIBRARY_ROOM, pane);
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.BOOK));
    SceneManager.getBookControllerInstance().fadeIn();
    SceneManager.currScene = AppUi.LIBRARY_ROOM;
    SceneManager.getBookControllerInstance().updateBackground();
  }

  @FXML
  public void fadeIn(){
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }
}
