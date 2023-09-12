package nz.ac.auckland.se206.controllers;

import java.util.Iterator;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;

public class TreasureRoomController extends itemRoomController {
  // Booleans to keep track of whether an item has been added to the inventory
  private boolean itemSixAdded;
  private boolean itemSevenAdded;
  private boolean itemEightAdded;
  private boolean itemNineAdded;
  private boolean itemTenAdded;
  // Booleans to keep track of if an item is clicked or selected
  private boolean sixClicked;
  private boolean sevenClicked;
  private boolean eightClicked;
  private boolean nineClicked;
  private boolean tenClicked;

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
  private ShapeInteractionHandler interactionHandler;

  private CountdownTimer countdownTimer;

  /**
   * Setting the appropriate fields and listeners when scene is initialised.
   * This includes initialising whether an item is clicked or already added
   * to the inventory, whether an item is ready to be added, whether the bag
   * is already opened, initialising the countdown timer and diabling the
   * mouseTrackRegion appropriately.
   */
  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setRightTimerLabel(timerLabel);

    // Setting appropriate boolean fields
    itemSixAdded = false;
    itemSevenAdded = false;
    itemEightAdded = false;
    itemNineAdded = false;
    itemTenAdded = false;

    readyToAdd = false;
    bagOpened = false;

    sixClicked = false;
    sevenClicked = false;
    eightClicked = false;
    nineClicked = false;
    tenClicked = false;

    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    // Setting up listeners for the various items
    interactionHandler = new ShapeInteractionHandler();
    if (itemSixImg != null) {
      itemSixImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemSixImg));
      itemSixImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemSixImg, sixClicked));
      itemSixImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.TALON));
    }
    if (itemSevenImg != null) {
      itemSevenImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemSevenImg));
      itemSevenImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemSevenImg, sevenClicked));
      itemSevenImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.CRYSTAL));
    }
    if (itemEightImg != null) {
      itemEightImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemEightImg));
      itemEightImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemEightImg, eightClicked));
      itemEightImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.BAT_WINGS));
    }
    if (itemNineImg != null) {
      itemNineImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemNineImg));
      itemNineImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemNineImg, nineClicked));
      itemNineImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.WREATH));
    }
    if (itemTenImg != null) {
      itemTenImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemTenImg));
      itemTenImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemTenImg, tenClicked));
      itemTenImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.FEATHER));
    }
    if (leftShpe != null) {
      leftShpe.setOnMouseEntered(
          event -> leftShpe.setOpacity(0.9));
      leftShpe.setOnMouseExited(
          event -> leftShpe.setOpacity(0.5));
    }
    if (bookBtn != null) {
      bookBtn.setOnMouseEntered(
          event -> interactionHandler.glowThis(bookBtn));
      bookBtn.setOnMouseExited(
          event -> interactionHandler.unglowThis(bookBtn));
    }
    if (bagBtn != null) {
      bagBtn.setOnMouseEntered(
          event -> interactionHandler.glowThis(bagBtn));
      bagBtn.setOnMouseExited(
          event -> interactionHandler.unglowThis(bagBtn));
      // ELSE NO ITEMS IN BAG MESSAGE
    }

    if (wizardRectangle != null) {
      wizardRectangle.setOnMouseEntered(
        event -> interactionHandler.handle(event));
      wizardRectangle.setOnMouseExited(
        event -> interactionHandler.handle(event));
    }
  }

  /**
   * Changing scenes to the cauldron room
   */
  @FXML
  public void goLeft(MouseEvent event) {
    System.out.println("TREASURE_ROOM -> CAULDRON_ROOM");
    // Resetting appropriate fields before changing scenes
    setText("", false, false);
    readyToAdd = false;
    itemScroll.setOpacity(0);
    bagOpened = false;
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    leftShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
  }

  /**
   * Selecting the item and prompting user and prompting user to either add or not
   * add the item to
   * their inventory. Does nothing if the item has already been added to the
   * inventory.
   *
   * @param item the item clicked by user
   */
  @FXML
  public void itemSelect(Items.Item item) {
    // Items are are clicked, so their glow remains on until item is added or not
    // added
    switch (item) {
      case TALON:
        if (itemSixAdded) {
          return;
        }
        interactionHandler.glowThis(itemSixImg);
        sixClicked = true;
        break;
      case CRYSTAL:
        if (itemSevenAdded) {
          return;
        }
        interactionHandler.glowThis(itemSevenImg);
        sevenClicked = true;
        break;
      case BAT_WINGS:
        if (itemEightAdded) {
          return;
        }
        interactionHandler.glowThis(itemEightImg);
        eightClicked = true;
        break;
      case WREATH:
        if (itemNineAdded) {
          return;
        }
        interactionHandler.glowThis(itemNineImg);
        nineClicked = true;
        break;
      case FEATHER:
        if (itemTenAdded) {
          return;
        }
        interactionHandler.glowThis(itemTenImg);
        tenClicked = true;
        break;
      default:
        break;
    }
    // Setting the appropriate text for the text box
    // and setting the item to be added and letting the
    // system know that an item is ready to be added
    this.item = item;
    setText("Add to inventory?", true, true);
    mouseTrackRegion.setDisable(false);
    readyToAdd = true;
    System.out.println(item + " clicked");
  }

  /** Adding a selected item to the inventory */
  @FXML
  public void addItem() {
    if (!readyToAdd) {
      return;
    }
    MainMenuController.inventory.add(item);
    setText("", false, false);
    readyToAdd = false;

    // Place holder image for now
    // Real item images will be initialised in switch case statment
    ImageView image = new ImageView(new Image("images/place_holder.png"));
    double ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      case TALON:
        Image six = new Image("images/talon.png");
        ratio = six.getHeight() / six.getWidth();
        image = new ImageView(six);
        itemSixImg.setOpacity(0);
        itemSixAdded = true;
        sixClicked = false;
        break;
      case CRYSTAL:
        Image seven = new Image("images/stone.png");
        ratio = seven.getHeight() / seven.getWidth();
        image = new ImageView(seven);
        itemSevenImg.setOpacity(0);
        itemSevenAdded = true;
        sevenClicked = false;
        break;
      case BAT_WINGS:
        Image eight = new Image("images/bwings.png");
        ratio = eight.getHeight() / eight.getWidth();
        image = new ImageView(eight);
        itemEightImg.setOpacity(0);
        itemEightAdded = true;
        eightClicked = false;
        break;
      case WREATH:
        Image nine = new Image("images/wreath.png");
        ratio = nine.getHeight() / nine.getWidth();
        image = new ImageView(nine);
        itemNineImg.setOpacity(0);
        itemNineAdded = true;
        nineClicked = false;
        break;
      case FEATHER:
        Image ten = new Image("images/feather.png");
        ratio = ten.getHeight() / ten.getWidth();
        image = new ImageView(ten);
        itemTenImg.setOpacity(0);
        itemTenAdded = true;
        tenClicked = false;
        break;
      default:
        break;
    }

    image.setFitHeight(133 * ratio);
    image.setFitWidth(133);
    // Using the inventory instance from the MainMenuController so that images
    // added from other scenes are not lost
    MainMenuController.inventory.box.getChildren().add(image);

    mouseTrackRegion.setDisable(true);
    // To see what is in the inventory in the terminal
    // Can be removed later
    System.out.println("Item added to inventory");
    System.out.println("Current Inventory:");
    Iterator itr = new MainMenuController().inventory.inventory.iterator();
    while (itr.hasNext()) {
      System.out.println("  " + itr.next());
    }
  }

  /**
   * Not adding a selected item to the inventory
   */
  @FXML
  public void noAdd() {
    if (!readyToAdd) {
      return;
    }
    setText("", false, false);
    readyToAdd = false;

    // Turning off the glow effect for all items
    sixClicked = false;
    interactionHandler.unglowThis(itemSixImg);
    sevenClicked = false;
    interactionHandler.unglowThis(itemSevenImg);
    eightClicked = false;
    interactionHandler.unglowThis(itemEightImg);
    nineClicked = false;
    interactionHandler.unglowThis(itemNineImg);
    tenClicked = false;
    interactionHandler.unglowThis(itemTenImg);

    // Making sure the mouseTrackRegion is disabled
    mouseTrackRegion.setDisable(true);
    System.out.println("Item not added to inventory");
  }

  /** Chaning scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("TREASURE_ROOM -> BOOK");
    SceneManager.currScene = AppUi.TREASURE_ROOM;
    leftShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /**
   * Dealing with closing the inventory or text box by clicking
   * a region outside of the inventory or text box.
   */
  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    setText("", false, false);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    wizardChatImage.setOpacity(0);
    wizardChatImage.setDisable(true);

    // Turning off the glow effect for all items
    interactionHandler.unglowThis(itemSixImg);
    interactionHandler.unglowThis(itemSevenImg);
    interactionHandler.unglowThis(itemEightImg);
    interactionHandler.unglowThis(itemNineImg);
    interactionHandler.unglowThis(itemTenImg);

    // None of the items are clicked anymore
    sixClicked = false;
    sevenClicked = false;
    eightClicked = false;
    nineClicked = false;
    tenClicked = false;

    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      itemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
