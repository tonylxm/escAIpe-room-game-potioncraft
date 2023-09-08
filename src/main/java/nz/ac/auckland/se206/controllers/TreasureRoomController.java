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

  @FXML private Rectangle textRect;
  @FXML private Rectangle mouseTrackRegion;
  @FXML private Polygon leftShpe;
  @FXML private Label textLbl;
  @FXML private Label noLbl;
  @FXML private Label yesLbl;
  @FXML private Label dashLbl;
  @FXML private ImageView bookBtn;
  @FXML private Label timerLabel;
  @FXML private ScrollPane treItemScroll;

  @FXML private ImageView itemSixImg;
  @FXML private ImageView itemSevenImg;
  @FXML private ImageView itemEightImg;
  @FXML private ImageView itemNineImg;
  @FXML private ImageView itemTenImg;

  @FXML private ShapeInteractionHandler interactionHandler;

  private CountdownTimer countdownTimer;

  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setRightTimerLabel(timerLabel);

    itemSixPicked = false;
    itemSevenPicked = false;
    itemEightPicked = false;
    itemNinePicked = false;
    itemTenPicked = false;
    readyToAdd = false;
    bagOpened = false;

    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    interactionHandler = new ShapeInteractionHandler();
    if (itemSixImg != null) {
      itemSixImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemSixImg));
      itemSixImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemSixImg));
      itemSixImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_6));
    }
    if (itemSevenImg != null) {
      itemSevenImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemSevenImg));
      itemSevenImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemSevenImg));
      itemSevenImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_7));
    }
    if (itemEightImg != null) {
      itemEightImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemEightImg));
      itemEightImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemEightImg));
      itemEightImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_8));
    }
    if (itemNineImg != null) {
      itemNineImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemNineImg));
      itemNineImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemNineImg));
      itemNineImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_9));
    }
    if (itemTenImg != null) {
      itemTenImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemTenImg));
      itemTenImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemTenImg));
      itemTenImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_10));
    }
    if (leftShpe != null) {
      leftShpe.setOnMouseEntered(event -> leftShpe.setOpacity(0.9));
      leftShpe.setOnMouseExited(event -> leftShpe.setOpacity(0.5));
    }
    // Some type of animation
    // bookBtn.setOnMouseEntered(event -> interactionHandler.handle(event));
    // bookBtn.setOnMouseExited(event -> interactionHandler.handle(event));
  }

  /** Changing scenes to the cauldron room */
  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("TREASURE_ROOM -> CAULDRON_ROOM");
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
    ImageView image = new ImageView(new Image("images/place_holder.png"));

    // Different controls are executed depending on the item
    switch (item) {
      case ITEM_6:
        image = new ImageView(new Image("images/Poké_Ball_icon.svg.png"));
        itemSixImg.setOpacity(0);
        itemSixPicked = true;
        break;
      case ITEM_7:
        image = new ImageView(new Image("images/Poké_Ball_icon.svg.png"));
        itemSevenImg.setOpacity(0);
        itemSevenPicked = true;
        break;
      case ITEM_8:
        image = new ImageView(new Image("images/Poké_Ball_icon.svg.png"));
        itemEightImg.setOpacity(0);
        itemEightPicked = true;
        break;
      case ITEM_9:
        image = new ImageView(new Image("images/Poké_Ball_icon.svg.png"));
        itemNineImg.setOpacity(0);
        itemNinePicked = true;
        break;
      case ITEM_10:
        image = new ImageView(new Image("images/Poké_Ball_icon.svg.png"));
        itemTenImg.setOpacity(0);
        itemTenPicked = true;
        break;
      default:
        break;
    }

    image.setFitHeight(133);
    image.setFitWidth(133);
    // Using the inventory instance from the MainMenuController so that images
    // added from other scenes are not lost
    MainMenuController.inventory.box.getChildren().add(image);

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
    if (MainMenuController.inventory.size() == 0) return;
    if (!bagOpened) {
      treItemScroll.setVvalue(0);
      treItemScroll.setContent(null);
      treItemScroll.setContent(MainMenuController.inventory.box);
      treItemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }

  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    textRect.setDisable(true);
    textRect.setOpacity(0);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      treItemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
