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
  public boolean itemOnePicked, itemTwoPicked, itemThreePicked, itemFourPicked, itemFivePicked;
  public boolean readyToAdd;
  public boolean bagOpened;
  public Items.Item item;

  @FXML private Rectangle textRect;
  @FXML private Rectangle mouseTrackRegion;
  @FXML private Polygon rightShpe;
  @FXML private Label textLbl;
  @FXML private Label noLbl;
  @FXML private Label yesLbl;
  @FXML private Label dashLbl;
  @FXML private ImageView bookBtn;
  @FXML private Label timerLabel;
  @FXML private ScrollPane libItemScroll;

  @FXML private ImageView itemOneImg;
  @FXML private ImageView itemTwoImg;
  @FXML private ImageView itemThreeImg;
  @FXML private ImageView itemFourImg;
  @FXML private ImageView itemFiveImg;

  @FXML private ShapeInteractionHandler interactionHandler;

  private CountdownTimer countdownTimer;

  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setLeftTimerLabel(timerLabel);

    itemOnePicked = false;
    itemTwoPicked = false;
    itemThreePicked = false;
    itemFourPicked = false;
    itemFivePicked = false;
    readyToAdd = false;
    bagOpened = false;

    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    interactionHandler = new ShapeInteractionHandler();
    if (itemOneImg != null) {
      itemOneImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemOneImg));
      itemOneImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemOneImg));
      itemOneImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_1));
    }
    if (itemTwoImg != null) {
      itemTwoImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemTwoImg));
      itemTwoImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemTwoImg));
      itemTwoImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_2));
    }
    if (itemThreeImg != null) {
      itemThreeImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemThreeImg));
      itemThreeImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemThreeImg));
      itemThreeImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_3));
    }
    if (itemFourImg != null) {
      itemFourImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemFourImg));
      itemFourImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemFourImg));
      itemFourImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_4));
    }
    if (itemFiveImg != null) {
      itemFiveImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemFiveImg));
      itemFiveImg.setOnMouseExited(event -> interactionHandler.unglowThis(itemFiveImg));
      itemFiveImg.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_5));
    }
    if (rightShpe != null) {
      rightShpe.setOnMouseEntered(event -> interactionHandler.handle(event));
      rightShpe.setOnMouseExited(event -> interactionHandler.handle(event));
    }
    // Some type of animation
    // bookBtn.setOnMouseEntered(event -> interactionHandler.handle(event));
    // bookBtn.setOnMouseExited(event -> interactionHandler.handle(event));
  }

  /** Changing scenes to the cauldron room */
  @FXML
  public void goRight(MouseEvent event) {
    System.out.println("LIBRARY_ROOM > CAULDRON_ROOM");
    setText("", false);
    readyToAdd = false;
    libItemScroll.setOpacity(0);
    bagOpened = false;
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    rightShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
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
      case ITEM_1:
        if (itemOnePicked) return;
        break;
      case ITEM_2:
        if (itemTwoPicked) return;
        break;
      case ITEM_3:
        if (itemThreePicked) return;
        break;
      case ITEM_4:
        if (itemFourPicked) return;
        break;
      case ITEM_5:
        if (itemFivePicked) return;
        break;
      default:
        break;
    }
    this.item = item;
    setText("Add to inventory?", true);
    readyToAdd = true;
    System.out.println(item + " clicked");
  }

  /** Adding item to inventory if an item is selected */
  @FXML
  public void addItem() {
    if (!readyToAdd) return;
    MainMenuController.inventory.add(item);
    setText("", false);
    readyToAdd = false;

    // If no item is selected but still added, place holder image
    ImageView image = new ImageView(new Image("images/place_holder.png"));
    double ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      case ITEM_1:
        Image one = new Image("images/tail.png");
        ratio = one.getHeight() / one.getWidth();
        image = new ImageView(one);
        itemOneImg.setOpacity(0);
        itemOnePicked = true;
        break;
      case ITEM_2:
        Image two = new Image("images/iwings.png");
        ratio = two.getHeight() / two.getWidth();
        image = new ImageView(two);
        itemTwoImg.setOpacity(0);
        itemTwoPicked = true;
        break;
      case ITEM_3:
        Image three = new Image("images/flower.png");
        ratio = three.getHeight() / three.getWidth();
        image = new ImageView(three);
        itemThreeImg.setOpacity(0);
        itemThreePicked = true;
        break;
      case ITEM_4:
        Image four = new Image("images/scales.png");
        ratio = four.getHeight() / four.getWidth();
        image = new ImageView(four);
        itemFourImg.setOpacity(0);
        itemFourPicked = true;
        break;
      case ITEM_5:
        Image five = new Image("images/powder.png");
        ratio = five.getHeight() / five.getWidth();
        image = new ImageView(five);
        itemFiveImg.setOpacity(0);
        itemFivePicked = true;
        break;
      default:
        break;
    }

    image.setFitHeight(133 * ratio);
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

  @FXML
  public void openBook() {
    System.out.println("LIBRARY_ROOM > BOOK");
    SceneManager.currScene = AppUi.SHELF_LEFT;
    rightShpe.getScene().setRoot(SceneManager.getUiRoot(AppUi.BOOK));
  }

  /** Dealing with the event where the bag icon is clicked */
  @FXML
  public void clickBag() {
    if (MainMenuController.inventory.size() == 0) return;
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

  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    textRect.setDisable(true);
    textRect.setOpacity(0);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);
    
    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      libItemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }
}
