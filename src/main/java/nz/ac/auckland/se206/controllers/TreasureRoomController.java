package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TreasureRoomController extends RoomController {
  @FXML
  private Pane pane;
  @FXML
  private Polygon leftShpe;
  @FXML
  private ImageView itemSixImg, itemSevenImg, itemEightImg, itemNineImg, itemTenImg;
  @FXML
  private Label timerLabel;

  /**
   * Setting the appropriate fields and listeners when scene is initialised.
   * This includes initialising whether an item is clicked or already added
   * to the inventory, whether an item is ready to be added, whether the bag
   * is already opened, initialising the countdown timer and diabling the
   * mouseTrackRegion appropriately.
   */
  public void initialize() {
    // Initialising everything from the superclass
    genericInitialise("Treasure", itemSixImg, itemSevenImg, itemEightImg, itemNineImg, itemTenImg, leftShpe);
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setTreasureTimerLabel(timerLabel);
  }

  /**
   * Changing scenes to the cauldron room
   */
  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("TREASURE_ROOM -> CAULDRON_ROOM");
    setText("", false, false);
    itemScroll.setOpacity(0);
    RoomController.goDirection(pane, AppUi.CAULDRON_ROOM);
  }

  /** Changing scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("TREASURE_ROOM -> BOOK");
    RoomController.openBook(AppUi.TREASURE_ROOM, pane);
  }
}
