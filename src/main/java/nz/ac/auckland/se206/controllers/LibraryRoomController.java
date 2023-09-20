package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LibraryRoomController extends ItemRoomController {
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

  private CountdownTimer countdownTimer;

  /**
   * Setting the appropriate fields and listeners when scene is initialised.
   * This includes initialising whether an item is clicked or already added
   * to the inventory, whether an item is ready to be added, whether the bag
   * is already opened, initialising the countdown timer and diabling the
   * mouseTrackRegion appropriately.
   */
  public void initialize() {
    // Initialising everything from the superclass
    genericInitialise("Library", itemOneImg, itemTwoImg, itemThreeImg, itemFourImg, itemFiveImg, rightShpe);
    
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setLibraryTimerLabel(timerLabel);
  }

  /** 
   * Changing scenes to the cauldron room 
   */
  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("LIBRARY_ROOM -> CAULDRON_ROOM");
    setText("", false, false);
    itemScroll.setOpacity(0);
    ItemRoomController.goDirection(pane, AppUi.CAULDRON_ROOM);
  }

  /** Changing scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("LIBRARY_ROOM -> BOOK");
    ItemRoomController.openBook(AppUi.LIBRARY_ROOM, pane);
  }
}
