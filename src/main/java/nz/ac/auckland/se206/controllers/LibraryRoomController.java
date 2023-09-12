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

public class LibraryRoomController {
  private boolean readyToAdd;
  private boolean bagOpened;
  private Items.Item item;

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
  private Rectangle textRect;
  @FXML
  private Rectangle mouseTrackRegion;
  @FXML
  private Polygon rightShpe;
  @FXML
  private Label textLbl;
  @FXML
  private Label noLbl;
  @FXML
  private Label yesLbl;
  @FXML
  private Label dashLbl;
  @FXML
  private ImageView bookBtn;
  @FXML
  private ImageView bagBtn;
  @FXML
  private Label timerLabel;
  @FXML
  private ScrollPane libItemScroll;

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
    countdownTimer.setLeftTimerLabel(timerLabel);

    // Setting appropriate boolean fields
    itemOneAdded = false;
    itemTwoAdded = false;
    itemThreeAdded = false;
    itemFourAdded = false;
    itemFiveAdded = false;

    readyToAdd = false;
    bagOpened = false;

    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

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
  }

  /** Changing scenes to the cauldron room */
  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("LIBRARY_ROOM -> CAULDRON_ROOM");
    // Resetting appropriate fields before changing scenes
    setText("", false);
    readyToAdd = false;
    libItemScroll.setOpacity(0);
    bagOpened = false;
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    rightShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
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
    setText("Add to inventory?", true);
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
    MainMenuController.inventory.add(item);
    setText("", false);
    readyToAdd = false;

    // If no item is selected but still added, place holder image
    ImageView image = new ImageView(new Image("images/place_holder.png"));
    double ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      case TAIL:
        Image one = new Image("images/tail.png");
        ratio = one.getHeight() / one.getWidth();
        image = new ImageView(one);
        itemOneImg.setOpacity(0);
        itemOneAdded = true;
        oneClicked = false;
        break;
      case INSECT_WINGS:
        Image two = new Image("images/iwings.png");
        ratio = two.getHeight() / two.getWidth();
        image = new ImageView(two);
        itemTwoImg.setOpacity(0);
        itemTwoAdded = true;
        twoClicked = false;
        break;
      case FLOWER:
        Image three = new Image("images/flower.png");
        ratio = three.getHeight() / three.getWidth();
        image = new ImageView(three);
        itemThreeImg.setOpacity(0);
        itemThreeAdded = true;
        threeClicked = false;
        break;
      case SCALES:
        Image four = new Image("images/scales.png");
        ratio = four.getHeight() / four.getWidth();
        image = new ImageView(four);
        itemFourImg.setOpacity(0);
        itemFourAdded = true;
        fourClicked = false;
        break;
      case POWDER:
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
    setText("", false);
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

  /**
   * Making text box appear or dissapear with given text.
   *
   * @param text the text to be displayed
   * @param on   whether the text box should be visible or not
   */
  @FXML
  private void setText(String text, boolean on) {
    textLbl.setText(text);
    if (on) {
      textRect.setOpacity(1);
      textLbl.setOpacity(1);

      // Decision labels need to be refactored to deal with
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

  /** Chaning scenes to book view */
  @FXML
  public void openBook() {
    System.out.println("LIBRARY_ROOM -> BOOK");
    // BookController.bookBackgroundImg.setImage(new
    // Image("/images/library-room.jpeg"));
    SceneManager.currScene = AppUi.LIBRARY_ROOM;
    rightShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /**
   * Dealing with the event where the bag icon is clicked
   */
  @FXML
  public void clickBag() {
    // If there are no items in the inventory, can't open the bag
    if (MainMenuController.inventory.size() == 0) {
      return;
    }
    // If the bag isn't opened already, open it
    if (!bagOpened) {
      libItemScroll.setVvalue(0);
      libItemScroll.setContent(null);
      libItemScroll.setContent(MainMenuController.inventory.box);
      libItemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }

  /**
   * Dealing with closing the inventory or text box by clicking
   * a region outside of the inventory or text box.
   */
  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    setText("", false);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

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
      libItemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
