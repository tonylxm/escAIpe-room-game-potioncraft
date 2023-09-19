package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.TransitionAnimation;

public class TreasureRoomController extends ItemRoomController {
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
    genericInitialise();
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setRightTimerLabel(timerLabel);

    // Setting appropriate boolean fields
    itemSixAdded = false;
    itemSevenAdded = false;
    itemEightAdded = false;
    itemNineAdded = false;
    itemTenAdded = false;

    sixClicked = false;
    sevenClicked = false;
    eightClicked = false;
    nineClicked = false;
    tenClicked = false;

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
    TransitionAnimation.changeScene(pane, AppUi.CAULDRON_ROOM, false);
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
    MainMenuController.getInventory().add(item);
    setText("", false, false);
    readyToAdd = false;

    // If no item is selected but still added, place holder image
    ImageView image = new ImageView(new Image("images/place_holder.png"));
    double ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      case TALON:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image six = new Image("images/talon.png");
        ratio = six.getHeight() / six.getWidth();
        image = new ImageView(six);
        itemSixImg.setOpacity(0);
        itemSixAdded = true;
        sixClicked = false;
        break;
      case CRYSTAL:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image seven = new Image("images/stone.png");
        ratio = seven.getHeight() / seven.getWidth();
        image = new ImageView(seven);
        itemSevenImg.setOpacity(0);
        itemSevenAdded = true;
        sevenClicked = false;
        break;
      case BAT_WINGS:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image eight = new Image("images/bwings.png");
        ratio = eight.getHeight() / eight.getWidth();
        image = new ImageView(eight);
        itemEightImg.setOpacity(0);
        itemEightAdded = true;
        eightClicked = false;
        break;
      case WREATH:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image nine = new Image("images/wreath.png");
        ratio = nine.getHeight() / nine.getWidth();
        image = new ImageView(nine);
        itemNineImg.setOpacity(0);
        itemNineAdded = true;
        nineClicked = false;
        break;
      case FEATHER:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
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
    itemCollect(ratio, image);
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
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
      bookController.updateBackground();
    }
    // Transitioning to the book scene with the appropriate fade animation
    System.out.println("TREASURE_ROOM -> BOOK");
    SceneManager.currScene = AppUi.TREASURE_ROOM;
    TransitionAnimation.changeScene(pane, AppUi.BOOK, false);
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
