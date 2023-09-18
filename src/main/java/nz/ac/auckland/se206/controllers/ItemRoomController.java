package nz.ac.auckland.se206.controllers;

import java.util.Iterator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.ShapeInteractionHandler;

public abstract class ItemRoomController {
  protected boolean bagOpened;
  protected boolean readyToAdd;
  protected Items.Item item;

  @FXML
  protected ImageView wizardChatImage;
  @FXML
  protected ImageView wizardImg;
  @FXML
  protected Rectangle mouseTrackRegion;
  @FXML
  protected Label textLbl;
  @FXML
  protected Rectangle textRect;
  @FXML
  protected Label yesLbl;
  @FXML
  protected Label noLbl;
  @FXML
  protected Label dashLbl;
  @FXML
  protected ScrollPane itemScroll;
  @FXML
  protected ImageView bookBtn;
  @FXML
  protected ImageView bagBtn;
  @FXML
  protected Label timerLabel;
  @FXML
  protected ShapeInteractionHandler interactionHandler;

  protected CountdownTimer countdownTimer;


  /**
   * Initialising the fields that are common in both of the item
   * rooms to avoid code duplication.
   */
  @FXML
  protected void genericInitialise() {
    readyToAdd = false;
    bagOpened = false;

    // Disabling the text box and mouse track region
    setText("", false, false);
    mouseTrackRegion.setDisable(true);
    mouseTrackRegion.setOpacity(0);

    // Setting appropriate interactable features
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
    if (wizardImg != null) {
      wizardImg.setOnMouseEntered(
          event -> interactionHandler.glowThis(wizardImg));
      wizardImg.setOnMouseExited(
          event -> interactionHandler.unglowThis(wizardImg));
    }
  }
  
  /**
   * Making text box appear or dissapear with given text.
   *
   * @param text the text to be displayed
   * @param on   whether the text box should be visible or not
   */
  @FXML
  protected void setText(String text, boolean on, boolean yesNo) {
    textLbl.setText(text);
    if (on) {
      textRect.setOpacity(1);
      textRect.setDisable(false);
      textLbl.setOpacity(1);
      textLbl.setDisable(false);

      // Decision labels need to be refactored to deal with
      // different room interactions, e.g. proceed.
      yesLbl.setDisable(!yesNo);
      noLbl.setDisable(!yesNo);
      if (yesNo) {
        yesLbl.setOpacity(1);
        yesLbl.setDisable(false);
        noLbl.setOpacity(1);
        noLbl.setDisable(false);
        dashLbl.setOpacity(1);
      } else {
        yesLbl.setOpacity(0);
        yesLbl.setDisable(true);
        noLbl.setOpacity(0);
        noLbl.setDisable(true);
        dashLbl.setOpacity(0);
      }
    } else {
      textRect.setOpacity(0);
      textRect.setDisable(true);
      textLbl.setOpacity(0);
      textLbl.setDisable(true);
      yesLbl.setOpacity(0);
      yesLbl.setDisable(true);
      noLbl.setOpacity(0);
      noLbl.setDisable(true);
      dashLbl.setOpacity(0);
    }
  }

  /**
   * Dealing with the event where the bag icon is clicked
   */
  @FXML
  public void clickBag() {
    // If there are no items in the inventory, can't open the bag
    if (MainMenuController.getInventory().size() == 0) {
      return;
    }
    // If the bag isn't opened already, open it
    if (!bagOpened) {
      itemScroll.setVvalue(0);
      itemScroll.setContent(null);
      itemScroll.setContent(MainMenuController.getInventory().getBox());
      itemScroll.setOpacity(1);
      bagOpened = true;
      mouseTrackRegion.setDisable(false);
      System.out.println("Bag opened");
    }
  }

  @FXML
  public void clickWizard(MouseEvent event) {
    System.out.println("wizard clicked");
    if (!GameState.isBookRiddleResolved) {
      showWizardChat();
      GameState.isBookRiddleGiven = true;
    } else {
      // showWizardChat();
    }
  }

  /**
   * Displaying wizard chat to user when prompted
   */
  protected void showWizardChat() {
    // Setting approrpiate fields to be visible and interactable
    wizardChatImage.setDisable(false);
    wizardChatImage.setOpacity(1);
    textRect.setDisable(false);
    mouseTrackRegion.setDisable(false);
    setText("I'm keeping an eye on you", true, false);
    mouseTrackRegion.setOpacity(0.5);
  }

  /**
   * Handling the identical parts of addItem in the treasure room and
   * library room in a single method.
   * 
   * @param ratio the ratio between the image's width and height
   * @param image an image of the item to be added to the inventory
   */
  public void itemCollect(double ratio, ImageView image) {
    image.setFitHeight(133 * ratio);
    image.setFitWidth(133);
    // Using the inventory instance from the MainMenuController so that images
    // added from other scenes are not lost
    MainMenuController.getInventory().getBox().getChildren().add(image);

    mouseTrackRegion.setDisable(true);
    // To see what is in the inventory in the terminal
    // Can be removed later
    System.out.println("Item added to inventory");
    System.out.println("Current Inventory:");
    Iterator itr = new MainMenuController().getInventory().getInventory().iterator();
    while (itr.hasNext()) {
      System.out.println("  " + itr.next());
    }
  }
}
