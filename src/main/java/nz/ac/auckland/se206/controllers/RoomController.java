package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Inventory;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.Items.Item;
import nz.ac.auckland.se206.Notification;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.SoundEffects;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public abstract class RoomController {
  protected static boolean bagOpened;
  protected static boolean readyToAdd;
  protected static ShapeInteractionHandler interactionHandler;

  /**
   * Handling the event where a button is hovered over
   */
  protected static void btnMouseActions(ImageView btn) {
    btn.setOnMouseEntered(event -> interactionHandler.glowThis(btn));
    btn.setOnMouseExited(event -> interactionHandler.unglowThis(btn));
  }

  protected static void arrowMouseActions(Polygon arrowShpe) {
    arrowShpe.setOnMouseEntered(event -> arrowShpe.setOpacity(0.9));
    arrowShpe.setOnMouseExited(event -> arrowShpe.setOpacity(0.6));
  }

  public static void goDirection(Pane pane, AppUi room) {
    // Resetting appropriate fields before changing scenes
    readyToAdd = false;
    bagOpened = false;
    SceneManager.setTimerScene(room);
    TransitionAnimation.changeScene(pane, room, false);
  }
  
  public static void openBook(AppUi currScene, Pane pane) throws URISyntaxException {
    BookController bookController = SceneManager.getBookControllerInstance();
    if (bookController != null) {
      bookController.updateBackground();
    }
    SceneManager.currScene = currScene;
    // TODO - fix this does not work
    // soundEffects.playSoundEffect("openBook.wav");
    // Transitioning to the book scene with the appropriate fade animation
    TransitionAnimation.changeScene(pane, AppUi.BOOK, false);
  }

  protected Items.Item item;

  @FXML
  private Pane pane;
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
  protected Label hintLabel;

  @FXML 
  protected TextArea chatTextArea;
  @FXML 
  protected TextField inputText;
  @FXML 
  protected Button sendButton;
  @FXML 
  protected ImageView ttsBtn2;
  @FXML
  protected ImageView cancelTtsBtn;

  @FXML
  protected ImageView notificationBack;
  @FXML
  protected Label notificationText;
  @FXML
  protected ImageView thoughtImage;
  @FXML
  private Rectangle fadeRectangle;
  
  protected CountdownTimer countdownTimer;
  protected static SoundEffects soundEffects;

  protected ImageView itemOneImg;
  protected ImageView itemTwoImg;
  protected ImageView itemThreeImg;
  protected ImageView itemFourImg;
  protected ImageView itemFiveImg;

  // Booleans to keep track of whether an item has been added to the inventory
  private boolean oneAdded;
  private boolean twoAdded;
  private boolean threeAdded;
  private boolean fourAdded;
  private boolean fiveAdded;
  // Booleans to keep track of if an item is clicked or selected
  private boolean oneClicked;
  private boolean twoClicked; 
  private boolean threeClicked;
  private boolean fourClicked;
  private boolean fiveClicked;

  private Item itemOne;
  private Item itemTwo;
  private Item itemThree;
  private Item itemFour;
  private Item itemFive;

  private Image one;
  private Image two;
  private Image three;
  private Image four;
  private Image five;

  private ImageView image;
  private double ratio;
  private boolean ttsOn;

  /**
   * Initialising the fields that are common in all of the item
   * rooms to avoid code duplication.
   */
  protected void genericInitialise(
      String roomName, ImageView itemOneImg, ImageView itemTwoImg, 
      ImageView itemThreeImg, ImageView itemFourImg, ImageView itemFiveImg) {
    this.itemOneImg = itemOneImg;
    this.itemTwoImg = itemTwoImg;
    this.itemThreeImg = itemThreeImg;
    this.itemFourImg = itemFourImg;
    this.itemFiveImg = itemFiveImg;

    // Setting appropriate boolean fields
    oneAdded = false;
    twoAdded = false;
    threeAdded = false;
    fourAdded = false;
    fiveAdded = false;

    oneClicked = false;
    twoClicked = false;
    threeClicked = false;
    fourClicked = false;
    fiveClicked = false;

    if (roomName.equals("Library")) {
      itemOne = Items.Item.TAIL;
      itemTwo = Items.Item.INSECT_WINGS;
      itemThree = Items.Item.FLOWER;
      itemFour = Items.Item.SCALES;
      itemFive = Items.Item.POWDER;
    } else if (roomName.equals("Treasure")) {
      itemOne = Items.Item.TALON;
      itemTwo = Items.Item.CRYSTAL;
      itemThree = Items.Item.BAT_WINGS;
      itemFour = Items.Item.WREATH;
      itemFive = Items.Item.FEATHER;
    } else if (roomName.equals("Cauldron")) {
      itemOne = Items.Item.BONE;
      itemTwo = Items.Item.FIRE;
      itemThree = Items.Item.ROOT;
      itemFour = Items.Item.BEETLE;
      itemFive = Items.Item.UNICORN_HORN;
    }

    readyToAdd = false;
    bagOpened = false;
    ttsOn = false;

    interactionHandler = new ShapeInteractionHandler();
    countdownTimer = MainMenuController.getCountdownTimer();
    soundEffects = new SoundEffects();

    // Disabling the text box and mouse track region
    setText("", false, false);
    toggleChat(true, 0);
    thoughtImage.setDisable(true);

    // Setting appropriate interactable features for the buttons
    btnMouseActions(bookBtn);
    btnMouseActions(bagBtn);
    btnMouseActions(wizardImg);

    // Setting appropriate interactable features for the items
    itemMouseActions(itemOneImg, oneClicked, itemOne);
    itemOneImg.setOnMouseExited(
        event -> interactionHandler.unglowThis(itemOneImg, oneClicked));
    itemMouseActions(itemTwoImg, twoClicked, itemTwo);
    itemTwoImg.setOnMouseExited(
        event -> interactionHandler.unglowThis(itemTwoImg, twoClicked));
    itemMouseActions(itemThreeImg, threeClicked, itemThree);
    itemThreeImg.setOnMouseExited(
        event -> interactionHandler.unglowThis(itemThreeImg, threeClicked));
    itemMouseActions(itemFourImg, fourClicked, itemFour);
    itemFourImg.setOnMouseExited(
        event -> interactionHandler.unglowThis(itemFourImg, fourClicked));
    itemMouseActions(itemFiveImg, fiveClicked, itemFive);
    itemFiveImg.setOnMouseExited(
        event -> interactionHandler.unglowThis(itemFiveImg, fiveClicked));
  }

  /**
   * Handling the event where an item is hovered over and clicked
   */
  protected void itemMouseActions(ImageView itemImg, boolean itemClicked, Items.Item item) {
    itemImg.setOnMouseEntered(event -> interactionHandler.glowThis(itemImg));
    itemImg.setOnMouseClicked(event -> itemSelect(item));
  }

  /**
   * Selecting the item and prompting user and prompting user to either add or not
   * add the item to their inventory. Does nothing if the item has already been 
   * added to the inventory.
   *
   * @param item the item clicked by user
   */
  @FXML
  public void itemSelect(Items.Item item) {
    // Items are are clicked, so their glow remains on until item is added or not
    // added
    switch (item) {
      case TAIL:
        handleSelect(oneAdded, itemOneImg);
        oneClicked = true;
        break;
      case INSECT_WINGS:
        handleSelect(twoAdded, itemTwoImg);
        twoClicked = true;
        break;
      case FLOWER:
        handleSelect(threeAdded, itemThreeImg);
        threeClicked = true;
        break;
      case SCALES:
        handleSelect(fourAdded, itemFourImg);
        fourClicked = true;
        break;
      case POWDER:
        handleSelect(fiveAdded, itemFiveImg);
        fiveClicked = true;
        break;
      case TALON:
        handleSelect(oneAdded, itemOneImg);
        oneClicked = true;
        break;
      case CRYSTAL:
        handleSelect(twoAdded, itemTwoImg);
        twoClicked = true;
        break;
      case BAT_WINGS:
        handleSelect(threeAdded, itemThreeImg);
        threeClicked = true;
        break;
      case WREATH:
        handleSelect(fourAdded, itemFourImg);
        fourClicked = true;
        break;
      case FEATHER:
        handleSelect(fiveAdded, itemFiveImg);
        fiveClicked = true;
        break;
      case BONE:
        handleSelect(oneAdded, itemOneImg);
        oneClicked = true;
        break;
      case FIRE:
        handleSelect(twoAdded, itemTwoImg);
        twoClicked = true;
        break;
      case ROOT:
        handleSelect(threeAdded, itemThreeImg);
        threeClicked = true;
        break;
      case BEETLE:
        handleSelect(fourAdded, itemFourImg);
        fourClicked = true;
        break;
      case UNICORN_HORN:
        handleSelect(fiveAdded, itemFiveImg);
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
    yesLbl.setFocusTraversable(true);
    yesLbl.requestFocus();
  }

  /**
   * Handling the event where an item is selected and prompting user and 
   * prompting user to either add or not add the item to their inventory. 
   * Does nothing if the item has already been added to the inventory.
   *
   * @param itemAdded whether the item has already been added to the inventory
   * @param itemImg the image of the item clicked by user
   * @param itemClicked whether the item has already been clicked by user
   */
  private void handleSelect(boolean itemAdded, ImageView itemImg) {
    if (itemAdded) {
      return;
    }
    interactionHandler.glowThis(itemImg);
  }

  /** Adding item to inventory if an item is selected */
  @FXML
  public void addItem() throws URISyntaxException {
    if (!readyToAdd) {
      return;
    }
    MainMenuController.getInventory().add(item);
    if (!GameState.areItemsCollected) {
      MainMenuController.getInventory().add(item);
      soundEffects.playSoundEffect("itemCollected.wav");
      Task<Void> collectedItemsTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          ChatMessage msg = new ChatMessage(
              "Wizard", MainMenuController.getChatHandler().runGpt(
              MainMenuController.getCollectedItemsMessage()));
          MainMenuController.getChatHandler().appendChatMessage(
              msg, chatTextArea, inputText, sendButton);
          return null;
        }
      };

      if (checkCorrectItems()) {
        GameState.areItemsCollected = true;
        new Thread(collectedItemsTask).start();
      }
    }

    setText("", false, false);
    readyToAdd = false;

    // If no item is selected but still added, place holder image
    image = new ImageView(new Image("images/place_holder.png"));
    ratio = 1;

    // Different controls are executed depending on the item
    switch (item) {
      // Selecting the tail to be added to the inventory
      case TAIL:
        one = new Image("images/tail.png");
        handleAddImg(one, itemOneImg);
        oneAdded = true;
        oneClicked = false;
        break;
      // Selecting the insect wings to be added to the inventory
      case INSECT_WINGS:
        two = new Image("images/insect_wings.png");
        handleAddImg(two, itemTwoImg);
        twoAdded = true;
        twoClicked = false;
        break;
      // Selecting the flower to be added to the inventory
      case FLOWER:
        three = new Image("images/flower.png");
        handleAddImg(three, itemThreeImg);
        threeAdded = true;
        threeClicked = false;
        break;
      // Selecting the scales to be added to the inventory
      case SCALES:
        four = new Image("images/scales.png");
        handleAddImg(four, itemFourImg);
        fourAdded = true;
        fourClicked = false;
        break;
      // Selecting the powder to be added to the inventory
      case POWDER:
        five = new Image("images/powder.png");
        handleAddImg(five, itemFiveImg);
        fiveAdded = true;
        fiveClicked = false;
        break;
      // Selecting the talon to be added to the inventory
      case TALON:
        one = new Image("images/talon.png");
        handleAddImg(one, itemOneImg);
        oneAdded = true;
        oneClicked = false;
        break;
      // Selecting the crystal to be added to the inventory
      case CRYSTAL:
        two = new Image("images/crystal.png");
        handleAddImg(two, itemTwoImg);
        twoAdded = true;
        twoClicked = false;
        break;
      // Selecting the bat wings to be added to the inventory
      case BAT_WINGS:
        three = new Image("images/bat_wings.png");
        handleAddImg(three, itemThreeImg);
        threeAdded = true;
        threeClicked = false;
        break;
      // Selecting the wreath to be added to the inventory
      case WREATH:
        four = new Image("images/wreath.png");
        handleAddImg(four, itemFourImg);
        fourAdded = true;
        fourClicked = false;
        break;
      // Selecting the feather to be added to the inventory  
      case FEATHER:
        five = new Image("images/feather.png");
        handleAddImg(five, itemFiveImg);
        fiveAdded = true;
        fiveClicked = false;
        break;
      // Selecting the bone to be added to the inventory
      case BONE:
        one = new Image("images/bone.png");
        handleAddImg(one, itemOneImg);
        oneAdded = true;
        oneClicked = false;
        break;
      // Selecting the fire to be added to the inventory
      case FIRE:
        two = new Image("images/fire.png");
        handleAddImg(two, itemTwoImg);
        twoAdded = true;
        twoClicked = false;
        break;
      // Selecting the root to be added to the inventory
      case ROOT:
        three = new Image("images/root.png");
        handleAddImg(three, itemThreeImg);
        threeAdded = true;
        threeClicked = false;
        break;
      // Selecting the beetle to be added to the inventory
      case BEETLE:
        four = new Image("images/beetle.png");
        handleAddImg(four, itemFourImg);
        fourAdded = true;
        fourClicked = false;
        break;
      // Selecting the unicorn horn to be added to the inventory
      case UNICORN_HORN:
        five = new Image("images/unicorn_horn.png");
        handleAddImg(five, itemFiveImg);
        fiveAdded = true;
        fiveClicked = false;
        break;
      default:
        break;
    }
    itemCollect(ratio, image);
  }

  /**
   * Handling the event where an item is added to bag and formatting image size ratio
   */
  protected void handleAddImg(Image img, ImageView itemImg) {
    // Setting up appropriate image and ratio and appropriate click fields to be added
    ratio = img.getHeight() / img.getWidth();
    image = new ImageView(img);
    itemImg.setOpacity(0);
    itemImg.setDisable(true);
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
    itemDefault();

    // Making sure the mouseTrackRegion is disabled
    mouseTrackRegion.setDisable(true);
    System.out.println("Item not added to inventory");
  }

  /**
   * Dealing with closing the inventory or text box by clicking
   * a region outside of the inventory or text box.
   */
  @FXML
  public void clickOff(MouseEvent event) {
    System.out.println("click off");
    setText("", false, false);
    toggleChat(true, 0);
    TransitionAnimation.fade(cancelTtsBtn, 0.0);
    cancelTtsBtn.setDisable(true);
    itemDefault();
    // Handling closing the "bag" when clicking off inventory
    if (bagOpened) {
      itemScroll.setOpacity(0);
      bagOpened = false;
      System.out.println("Bag closed");
    }
  }

  public void itemDefault() {
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
    mouseTrackRegion.setDisable(!on);
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
   * @throws URISyntaxException
   */
  @FXML
  public void clickBag() throws URISyntaxException {
    // If there are no items in the inventory, can't open the bag
    if (MainMenuController.inventory.size() == 0) {
      notificationText.setText("You have no ingredients in your bag!");
      Notification.notifyPopup(notificationBack, notificationText);
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

  /**
   * Dealing with the event where the wizard icon is clicked
   * @throws ApiProxyException
   */
  @FXML
  public void clickWizard(MouseEvent event) throws ApiProxyException {
    System.out.println("wizard clicked");
    toggleChat(false, 1);
    chatTextArea.setText("How can I help young apprentice?");
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
    new MainMenuController();
    Iterator<Item> itr = MainMenuController.getInventory().getInventory().iterator();
    while (itr.hasNext()) {
      System.out.println("  " + itr.next());
    }
  }

  
  /**
   * Enabling/disabling the chat functionality for the user to be able to talk to the wizard.
   */
  protected void toggleChat(boolean disable, int opacity) {
    // Enabling/disabling the approrpiate fields and making everything invisible/visible
    chatTextArea.setDisable(disable);
    TransitionAnimation.fade(chatTextArea, opacity);
    inputText.setDisable(disable);
    TransitionAnimation.fade(inputText, opacity);
    sendButton.setDisable(disable);
    TransitionAnimation.fade(sendButton, opacity);
    ttsBtn2.setDisable(disable);
    TransitionAnimation.fade(ttsBtn2, opacity);
    textRect.setDisable(disable);
    TransitionAnimation.fade(textRect, opacity);
    wizardChatImage.setDisable(disable);
    TransitionAnimation.fade(wizardChatImage, opacity);

    mouseTrackRegion.setDisable(disable);
    if (opacity == 0) {
      mouseTrackRegion.setDisable(true);
      TransitionAnimation.fade(mouseTrackRegion, 0);
    } else {
      mouseTrackRegion.setDisable(false);
      TransitionAnimation.fade(mouseTrackRegion, 0.4);
    }
  }

  protected void disableChat(boolean disable, double opacity) {
    inputText.setDisable(disable);
    inputText.setOpacity(opacity);
    sendButton.setDisable(disable);
    sendButton.setOpacity(opacity);
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = inputText.getText();
    // Not doing anything if there is no message
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    disableChat(true, 0.5);
    ChatMessage msg = new ChatMessage("user", message);
    MainMenuController.getChatHandler().appendChatMessage(
        msg, chatTextArea, inputText, sendButton);

    thoughtImage.setDisable(false);
    thoughtImage.setOpacity(1);

    Task<Void> runGptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage response = new ChatMessage(
                "assistant", MainMenuController.getChatHandler().runGpt(message));
            MainMenuController.getChatHandler().appendChatMessage(
                response, chatTextArea, inputText, sendButton);
            // Updating the number of hints for medium mode after GPT has given a hint
            // Checking if the correct role has given the hint, rather than the user
            if (response.getContent().startsWith("HINT")) {
              int hints = MainMenuController.getHints();
              hints--;
              MainMenuController.setHints(hints);
              System.out.println(MainMenuController.getHints());
            }
            return null;
          }
        };
    // Updating the number of hints in each room's labels after the GPT model has run
    runGptTask.setOnSucceeded(
        e -> {
          thoughtImage.setOpacity(0);
          thoughtImage.setDisable(true);
          if (MainMenuController.getHints() >= 0) {
            countdownTimer.updateHintLabel(MainMenuController.getHints());
          }
        });
    new Thread(runGptTask).start();
  }

  /**
   * Handles when ENTER is pressed on the input text area.
   *
   * @throws IOException
   */
  @FXML
  public void onEnterPressed(KeyEvent event) throws ApiProxyException, IOException {
    System.out.println("any key pressed");
    if (event.getCode().toString().equals("ENTER")) {
      System.out.println("key " + event.getCode() + " pressed");
      onSendMessage(new ActionEvent());
    }
  }

  /**
   * Handles when Y or N is pressed on the input text area.
   * @param event
   * @throws ApiProxyException
   * @throws IOException
   * @throws URISyntaxException
   */
  @FXML
  public void onYesPressed(KeyEvent event) throws ApiProxyException, IOException, URISyntaxException {
    // If Y us pressed, adding the item
    if (event.getCode().toString().equals("Y")) {
      System.out.println("key " + event.getCode() + " pressed");
      addItem();
    }
    // If N is pressed, not adding the item
    if (event.getCode().toString().equals("N")) {
      System.out.println("key " + event.getCode() + " pressed");
      noAdd();
    }
  }

  /**
   * Handles when N is pressed on the input text area.
   * @param event
   * @throws ApiProxyException
   * @throws IOException
   */
  @FXML
  public void onNoPressed(KeyEvent event) throws ApiProxyException, IOException {
    // Not adding the item if N is pressed
    if (event.getCode().toString().equals("N")) {
      System.out.println("key " + event.getCode() + " pressed");
      noAdd();
    }
  }

  /** 
   * Uses text to speech to read the game master's response to the user's message. 
   */
  @FXML
  private void onReadGameMasterResponse() {
    // Using concurency to prevent the system freezing
    if (!ttsOn) {
      ttsOn = true;
      cancelTtsBtn.setDisable(false);
      cancelTtsBtn.setOpacity(1);
      Task<Void> speakTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          App.textToSpeech.speak(chatTextArea.getText());
          return null;
        }
      };
      new Thread(speakTask).start();
      speakTask.setOnSucceeded(e -> {
        ttsOn = false;
        cancelTtsBtn.setDisable(true);
        cancelTtsBtn.setOpacity(0);
      });
    }
  }

  @FXML
  public void onCancelTts() {
    ttsOn = false;
    cancelTtsBtn.setDisable(true);
    cancelTtsBtn.setOpacity(0);
    App.textToSpeech.stop();
  }

  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }

  public TextArea getTextArea() {
    return chatTextArea;
  }

  public TextField getInputText() {
    return inputText;
  }

  public Button getSendButton() {
    return sendButton;
  }

  /**
   * Function to check if the user has collected all the required items
   * to be able to brew the potion.
   * 
   * @return boolean whether the user has collected all the required items
   */
  private boolean checkCorrectItems() {
    Inventory inventory = MainMenuController.getInventory();
    int count = 0;
    for (int i = 0; i < 5; i++) {
      if (inventory.contains(Items.necessary.get(i))) {
        count++;
      }
    }
    return count == 5;
  }
}
