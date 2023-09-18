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

public class LibraryRoomController extends ItemRoomController {
  // Booleans to keep track of whether an item has been added to the inventory
  private boolean itemOneAdded;
  private boolean itemTwoAdded;
  private boolean itemThreeAdded;
  private boolean itemFourAdded;
  private boolean itemFiveAdded;
  // Booleans to keep track of if an item is clicked or selected
  private boolean oneClicked;
  private boolean twoClicked;
  private boolean threeClicked;
  private boolean fourClicked;
  private boolean fiveClicked;

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
    genericInitialise();
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setLibraryTimerLabel(timerLabel);


    // Setting appropriate boolean fields
    itemOneAdded = false;
    itemTwoAdded = false;
    itemThreeAdded = false;
    itemFourAdded = false;
    itemFiveAdded = false;

    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    // Setting up listeners for the various items
    interactionHandler = new ShapeInteractionHandler();
    if (itemOneImg != null) {
      itemOneImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemOneImg));
      itemOneImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemOneImg, oneClicked));
      itemOneImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.TAIL));
    }
    if (itemTwoImg != null) {
      itemTwoImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemTwoImg));
      itemTwoImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemTwoImg, twoClicked));
      itemTwoImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.INSECT_WINGS));
    }
    if (itemThreeImg != null) {
      itemThreeImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemThreeImg));
      itemThreeImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemThreeImg, threeClicked));
      itemThreeImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.FLOWER));
    }
    if (itemFourImg != null) {
      itemFourImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemFourImg));
      itemFourImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemFourImg, fourClicked));
      itemFourImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.SCALES));
    }
    if (itemFiveImg != null) {
      itemFiveImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(itemFiveImg));
      itemFiveImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(itemFiveImg, fiveClicked));
      itemFiveImg.setOnMouseClicked(
          event -> itemSelect(Items.Item.POWDER));
    }
    if (rightShpe != null) {
      rightShpe.setOnMouseEntered(
          event -> rightShpe.setOpacity(0.9));
      rightShpe.setOnMouseExited(
          event -> rightShpe.setOpacity(0.5));
    }
  }

  /** 
   * Changing scenes to the cauldron room 
   */
  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("LIBRARY_ROOM -> CAULDRON_ROOM");
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
      case TAIL:
        if (itemOneAdded) {
          return;
        }
        interactionHandler.glowThis(itemOneImg);
        oneClicked = true;
        break;
      case INSECT_WINGS:
        if (itemTwoAdded) {
          return;
        }
        interactionHandler.glowThis(itemTwoImg);
        twoClicked = true;
        break;
      case FLOWER:
        if (itemThreeAdded) {
          return;
        }
        interactionHandler.glowThis(itemThreeImg);
        threeClicked = true;
        break;
      case SCALES:
        if (itemFourAdded) {
          return;
        }
        interactionHandler.glowThis(itemFourImg);
        fourClicked = true;
        break;
      case POWDER:
        if (itemFiveAdded) {
          return;
        }
        interactionHandler.glowThis(itemFiveImg);
        fiveClicked = true;
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

  /** Adding item to inventory if an item is selected */
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
      case TAIL:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image one = new Image("images/tail.png");
        ratio = one.getHeight() / one.getWidth();
        image = new ImageView(one);
        itemOneImg.setOpacity(0);
        itemOneAdded = true;
        oneClicked = false;
        break;
      case INSECT_WINGS:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image two = new Image("images/iwings.png");
        ratio = two.getHeight() / two.getWidth();
        image = new ImageView(two);
        itemTwoImg.setOpacity(0);
        itemTwoAdded = true;
        twoClicked = false;
        break;
      case FLOWER:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image three = new Image("images/flower.png");
        ratio = three.getHeight() / three.getWidth();
        image = new ImageView(three);
        itemThreeImg.setOpacity(0);
        itemThreeAdded = true;
        threeClicked = false;
        break;
      case SCALES:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image four = new Image("images/scales.png");
        ratio = four.getHeight() / four.getWidth();
        image = new ImageView(four);
        itemFourImg.setOpacity(0);
        itemFourAdded = true;
        fourClicked = false;
        break;
      case POWDER:
        // Setting up appropriate image and ratio and appropriate
        // click fields to be added
        Image five = new Image("images/powder.png");
        ratio = five.getHeight() / five.getWidth();
        image = new ImageView(five);
        itemFiveImg.setOpacity(0);
        itemFiveAdded = true;
        fiveClicked = false;
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
    oneClicked = false;
    interactionHandler.unglowThis(itemOneImg);
    twoClicked = false;
    interactionHandler.unglowThis(itemTwoImg);
    threeClicked = false;
    interactionHandler.unglowThis(itemThreeImg);
    fourClicked = false;
    interactionHandler.unglowThis(itemFourImg);
    fiveClicked = false;
    interactionHandler.unglowThis(itemFiveImg);

    // Making sure the mouseTrackRegion is disabled
    mouseTrackRegion.setDisable(true);
    System.out.println("Item not added to inventory");
  }

  /** Chaning scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("LIBRARY_ROOM -> BOOK");
    // BookController.bookBackgroundImg.setImage(new
    // Image("/images/library-room.jpeg"));
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
      bookController.updateBackground();
    }
    SceneManager.currScene = AppUi.LIBRARY_ROOM;
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
    interactionHandler.unglowThis(itemOneImg);
    interactionHandler.unglowThis(itemTwoImg);
    interactionHandler.unglowThis(itemThreeImg);
    interactionHandler.unglowThis(itemFourImg);
    interactionHandler.unglowThis(itemFiveImg);

    // None of the items are clicked anymore
    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      itemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
