package nz.ac.auckland.se206.controllers;

import java.util.Iterator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;

public class TreasureRoomController {
  public boolean itemSixPicked, itemSevenPicked, itemEightPicked, itemNinePicked, itemTenPicked;
  public boolean readyToAdd;
  public boolean bagOpened;
  public Items.Item item;

  @FXML private Rectangle itemSixRect;
  @FXML private Rectangle itemSevenRect;
  @FXML private Rectangle itemEightRect;
  @FXML private Rectangle itemNineRect;
  @FXML private Rectangle itemTenRect;
  @FXML private Rectangle textRect;
  @FXML private Polygon leftShpe;
  @FXML private Label textLbl;
  @FXML private Label noLbl;
  @FXML private Label yesLbl;
  @FXML private Label dashLbl;
  @FXML private ImageView bookBtn;
  @FXML private Label timerLabel;
  @FXML private ScrollPane treItemScroll;

  @FXML private ShapeInteractionHandler interactionHandler;

  private CountdownTimer countdownTimer;

  public void initialize() {
    countdownTimer = App.getCountdownTimer();
    countdownTimer.setRightTimerLabel(timerLabel);

    itemSixPicked = false;
    itemSevenPicked = false;
    itemEightPicked = false;
    itemNinePicked = false;
    itemTenPicked = false;
    readyToAdd = false;
    bagOpened = false;

    interactionHandler = new ShapeInteractionHandler();
    if (itemSixRect != null) {
      itemSixRect.setOnMouseEntered(event -> interactionHandler.handle(event));
      itemSixRect.setOnMouseExited(event -> interactionHandler.handle(event));
      itemSixRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_6));
    }
    if (itemSevenRect != null) {
      itemSevenRect.setOnMouseEntered(event -> interactionHandler.handle(event));
      itemSevenRect.setOnMouseExited(event -> interactionHandler.handle(event));
      itemSevenRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_7));
    }
    if (itemEightRect != null) {
      itemEightRect.setOnMouseEntered(event -> interactionHandler.handle(event));
      itemEightRect.setOnMouseExited(event -> interactionHandler.handle(event));
      itemEightRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_8));
    }
    if (itemNineRect != null) {
      itemNineRect.setOnMouseEntered(event -> interactionHandler.handle(event));
      itemNineRect.setOnMouseExited(event -> interactionHandler.handle(event));
      itemNineRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_9));
    }
    if (itemTenRect != null) {
      itemTenRect.setOnMouseEntered(event -> interactionHandler.handle(event));
      itemTenRect.setOnMouseExited(event -> interactionHandler.handle(event));
      itemTenRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_10));
    }
    if (leftShpe != null) {
      leftShpe.setOnMouseEntered(event -> interactionHandler.handle(event));
      leftShpe.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    // Some type of animation
    // bookBtn.setOnMouseEntered(event -> interactionHandler.handle(event));
    // bookBtn.setOnMouseExited(event -> interactionHandler.handle(event));
  }

  /** Changing scenes to the cauldron room */
  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("TREASURE_ROOM > CAULDRON_ROOM");
    setText("", false);
    readyToAdd = false;
    treItemScroll.setOpacity(0);
    bagOpened = false;
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    leftShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
  }

  /**
   * Selecting the item and prompting user and prompting user to either add or not add the item to
   * their inventory. Does nothing if the item has already been added to the inventory.
   *
   * @param item the item clicked by user
   */
  @FXML
  public void itemSelect(Items.Item item) {
    switch (item) {
      case ITEM_6:
        if (itemSixPicked) return;
        break;
      case ITEM_7:
        if (itemSevenPicked) return;
        break;
      case ITEM_8:
        if (itemEightPicked) return;
        break;
      case ITEM_9:
        if (itemNinePicked) return;
        break;
      case ITEM_10:
        if (itemTenPicked) return;
        break;
      default:
        break;
    }
    this.item = item;
    setText("Add to inventory?", true);
    readyToAdd = true;
    System.out.println(item + " clicked");
  }

  /** Adding a selected item to the inventory */
  @FXML
  public void addItem() {
    if (!readyToAdd) return;
    MainMenuController.inventory.add(item);
    setText("", false);
    readyToAdd = false;

    // Place holder image for now
    // Real item images will be initialised in switch case statment
    Image image = new Image("images/icon.png");

    // Different controls are executed depending on the item
    switch (item) {
      case ITEM_6:
        itemSixRect.setOpacity(0);
        itemSixPicked = true;
        break;
      case ITEM_7:
        itemSevenRect.setOpacity(0);
        itemSevenPicked = true;
        break;
      case ITEM_8:
        itemEightRect.setOpacity(0);
        itemEightPicked = true;
        break;
      case ITEM_9:
        itemNineRect.setOpacity(0);
        itemNinePicked = true;
        break;
      case ITEM_10:
        itemTenRect.setOpacity(0);
        itemTenPicked = true;
        break;
      default:
        break;
    }

    // Using the inventory instance from the MainMenuController so that images
    // added from other scenes are not lost
    MainMenuController.inventory.box.getChildren().add(new ImageView(image));

    // To see what is in the inventory in the terminal
    // Can be removed later
    System.out.println("Item added to inventory");
    System.out.println("Current Inventory:");
    Iterator itr = new MainMenuController().inventory.inventory.iterator();
    while (itr.hasNext()) {
      System.out.println("  " + itr.next());
    }
  }

  /** Not adding a selected item to the inventory */
  @FXML
  public void noAdd() {
    if (!readyToAdd) return;
    setText("", false);
    readyToAdd = false;
    System.out.println("Item not added to inventory");
  }

  /**
   * Making text box appear or dissapear with given text.
   *
   * @param text the text to be displayed
   * @param on whether the text box should be visible or not
   */
  @FXML
  private void setText(String text, boolean on) {
    textLbl.setText(text);
    if (on) {
      textRect.setOpacity(1);
      textLbl.setOpacity(1);
      // Desicion labels need to be refactored to deal with
      // different room interactions, e.g. proceed.
      yesLbl.setOpacity(1);
      noLbl.setOpacity(1);
      dashLbl.setOpacity(1);
    } else {
      textRect.setOpacity(0);
      textLbl.setOpacity(0);
      yesLbl.setOpacity(0);
      noLbl.setOpacity(0);
      dashLbl.setOpacity(0);
    }
  }

  @FXML
  public void openBook() {
    System.out.println("TREASURE_ROOM > BOOK");
    SceneManager.currScene = AppUi.SHELF_RIGHT;
    leftShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /** Dealing with the event where the bag icon is clicked */
  @FXML
  public void clickBag() {
    if (!bagOpened) {
      treItemScroll.setContent(null);
      treItemScroll.setContent(MainMenuController.inventory.box);
      treItemScroll.setOpacity(1);
      bagOpened = true;
      System.out.println("Bag opened");
    } else {
      treItemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
