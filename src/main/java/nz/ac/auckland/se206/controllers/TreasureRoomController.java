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
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TreasureRoomController extends RoomController {
  @FXML
  private Pane pane;
  @FXML
  private Polygon leftShpe;
  @FXML
  private ImageView itemSixImg;
  @FXML
  private ImageView itemSevenImg;
  @FXML
  private ImageView itemEightImg;
  @FXML
  private ImageView itemNineImg;
  @FXML
  private ImageView itemTenImg;
  @FXML
  private Label timerLabel;
  @FXML
  private Rectangle fadeRectangle;
  @FXML
  private ImageView chestImg;

  /**
   * Setting the appropriate fields and listeners when scene is initialised.
   * This includes initialising whether an item is clicked or already added
   * to the inventory, whether an item is ready to be added, whether the bag
   * is already opened, initialising the countdown timer and diabling the
   * mouseTrackRegion appropriately.
   */
  public void initialize() {
    // Initialising everything from the superclass
    genericInitialise("Treasure", itemSixImg, itemSevenImg, itemEightImg, itemNineImg, itemTenImg);
    switchItems(GameState.isChestOpen);
    countdownTimer.setTreasureTimerLabel(timerLabel);
    countdownTimer.setTreasureHintLabel(hintLabel);
    arrowMouseActions(leftShpe);
    chestImg.setOnMouseEntered(event -> interactionHandler.glowThis(chestImg));
    chestImg.setOnMouseExited(event -> interactionHandler.unglowThis(chestImg));
  }

  /**
   * Changing scenes to the cauldron room.
   */
  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("TREASURE_ROOM -> CAULDRON_ROOM");
    setText("", false, false);
    itemScroll.setOpacity(0);
    //goDirection(pane, AppUi.CAULDRON_ROOM);
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.getCauldronRoomControllerInstance().fadeIn();
  }

  /**
   * Changing scenes to the chest room.
   * 
   * @param event
   */
  @FXML
  public void enterChest(MouseEvent event) {
    System.out.println("TREASURE_ROOM -> CHEST");
    //TransitionAnimation.changeScene(pane, AppUi.CHEST, false);
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.CHEST));
    SceneManager.getChestControllerInstance().fadeIn();
    SceneManager.setTimerScene(AppUi.CHEST);
  }
  
  /** 
   * Changing scenes to book view .
   */
  @FXML
  public void openBook() {
    System.out.println("TREASURE_ROOM -> BOOK");
    //openBook(AppUi.TREASURE_ROOM, pane);
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.BOOK));
    SceneManager.getBookControllerInstance().fadeIn();
    SceneManager.currScene = AppUi.TREASURE_ROOM;
    SceneManager.getBookControllerInstance().updateBackground();
  }

  /**
   * Changing scenes to the inventory room.
   */
  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }

  /**
   * Switching the items in the treasure room when the chest is opened.
   * 
   * @param chestOpened
   */
  @FXML
  public void switchItems(boolean chestOpened) {
    // Setting the integer value for the opacity depending on whether the
    // chest has been opened or not
    int opacity = chestOpened ? 1 : 0;
    // Setting various item's to be visible or not depending on whether the
    // chest has been opened
    chestImg.setOpacity(1 - opacity);
    chestImg.setDisable(chestOpened);
    itemSixImg.setOpacity(opacity);
    itemSevenImg.setOpacity(opacity);
    itemEightImg.setOpacity(opacity);
    itemNineImg.setOpacity(opacity);
    itemTenImg.setOpacity(opacity);
  }
}
