package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.Notification;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;
import nz.ac.auckland.se206.TransitionAnimation;

public class CauldronController {
  private static CauldronController instance;

  @FXML 
  private Pane pane;
  @FXML
  private ImageView batWingImage,
      crystalImage,
      insectWingImage,
      talonImage,
      powderImage,
      tailImage,
      featherImage,
      scalesImage,
      flowerImage,
      wreathImage,
      boneImage,
      fireImage,
      rootImage,
      beetleImage,
      unicornHornImage;

  @FXML 
  private ImageView cauldronImageView;
  @FXML 
  private Rectangle cauldronOverlay;
  @FXML 
  private Button brewBtn;
  @FXML 
  private Button emptyBtn;
  @FXML 
  private Label timerLabel;
  @FXML 
  private ImageView backImg;
  @FXML 
  private ImageView notificationBack;
  @FXML 
  private Label notificationText;

  private Map<String, Items.Item> imageViewToItemMap = new HashMap<>();
  private Set<Items.Item> inventory;

  // array to store the items dropped into the cauldron
  private ArrayList<Items.Item> cauldronItems = new ArrayList<Items.Item>();

  private ImageView draggedItem;

  private CountdownTimer countdownTimer;

  private ShapeInteractionHandler interactionHandler;

  public CauldronController() {
    instance = this;
  }

  @FXML
  private void initialize() {
    // Set up the timer
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setBrewingLabel(timerLabel);
    inventory = MainMenuController.getInventory().getInventory();
    // set up drag and drop for all images
    setupDragAndDrop(batWingImage, "batWingImage");
    setupDragAndDrop(crystalImage, "crystalImage");
    setupDragAndDrop(insectWingImage, "insectWingImage");
    setupDragAndDrop(talonImage, "talonImage");
    setupDragAndDrop(powderImage, "powderImage");
    setupDragAndDrop(tailImage, "tailImage");
    setupDragAndDrop(featherImage, "featherImage");
    setupDragAndDrop(scalesImage, "scalesImage");
    setupDragAndDrop(flowerImage, "flowerImage");
    setupDragAndDrop(wreathImage, "wreathImage");
    setupDragAndDrop(boneImage, "boneImage");
    setupDragAndDrop(fireImage, "fireImage");
    setupDragAndDrop(rootImage, "rootImage");
    setupDragAndDrop(beetleImage, "beetleImage");
    setupDragAndDrop(unicornHornImage, "unicornHornImage");

    // defining mapping
    imageViewToItemMap.put("batWingImage", Items.Item.BAT_WINGS);
    imageViewToItemMap.put("crystalImage", Items.Item.CRYSTAL);
    imageViewToItemMap.put("insectWingImage", Items.Item.INSECT_WINGS);
    imageViewToItemMap.put("talonImage", Items.Item.TALON);
    imageViewToItemMap.put("powderImage", Items.Item.POWDER);
    imageViewToItemMap.put("tailImage", Items.Item.TAIL);
    imageViewToItemMap.put("featherImage", Items.Item.FEATHER);
    imageViewToItemMap.put("scalesImage", Items.Item.SCALES);
    imageViewToItemMap.put("flowerImage", Items.Item.FLOWER);
    imageViewToItemMap.put("wreathImage", Items.Item.WREATH);
    imageViewToItemMap.put("boneImage", Items.Item.BONE);
    imageViewToItemMap.put("fireImage", Items.Item.FIRE);
    imageViewToItemMap.put("rootImage", Items.Item.ROOT);
    imageViewToItemMap.put("beetleImage", Items.Item.BEETLE);
    imageViewToItemMap.put("unicornHornImage", Items.Item.UNICORN_HORN);

    // disable all image
    batWingImage.setDisable(true);
    crystalImage.setDisable(true);
    insectWingImage.setDisable(true);
    talonImage.setDisable(true);
    powderImage.setDisable(true);
    tailImage.setDisable(true);
    featherImage.setDisable(true);
    scalesImage.setDisable(true);
    flowerImage.setDisable(true);
    wreathImage.setDisable(true);
    boneImage.setDisable(true);
    fireImage.setDisable(true);
    rootImage.setDisable(true);
    beetleImage.setDisable(true);
    unicornHornImage.setDisable(true);

    // Set up glow for all images by adding images to an array then going
    // in a loop
    ArrayList<ImageView> images = new ArrayList<ImageView>();
    images.add(batWingImage);
    images.add(crystalImage);
    images.add(insectWingImage);
    images.add(talonImage);
    images.add(powderImage);
    images.add(tailImage);
    images.add(featherImage);
    images.add(scalesImage);
    images.add(flowerImage);
    images.add(wreathImage);
    images.add(boneImage);
    images.add(fireImage);
    images.add(rootImage);
    images.add(beetleImage);
    images.add(unicornHornImage);

    // setup glow for all images
    for (ImageView image : images) {
      image.setOnMouseEntered(event -> interactionHandler.glowThis(image));
      image.setOnMouseExited(event -> interactionHandler.unglowThis(image));
    }

    // Set up drag and drop for cauldronImageView
    cauldronImageView.setOnDragOver(
        event -> {
          if (event.getGestureSource() != cauldronImageView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
          }
          event.consume();
        });

    cauldronImageView.setOnDragDropped(
        event -> {
          Dragboard db = event.getDragboard();
          boolean success = false;
          System.out.println("Dropped");
          if (db.hasString()) {
            // Get the identifier of the dropped item
            String itemId = db.getString();

            if (draggedItem != null) {
              // Check if the item was dropped within the cauldron's bounds
              Bounds cauldronBounds =
                  cauldronImageView.localToScene(cauldronImageView.getBoundsInLocal());
              double itemX = event.getSceneX();
              double itemY = event.getSceneY();

              if (cauldronBounds.contains(itemX, itemY)) {
                // If dropped within cauldron bounds, make it disappear
                System.out.println("Dropped within cauldron bounds");
                draggedItem.setVisible(false);
                success = true;
                if (db.hasString()) {
                  String imageViewName = itemId;
                  Items.Item item = imageViewToItemMap.get(imageViewName);

                  if (item != null) {
                    // Add the item to the cauldronItems ArrayList
                    cauldronItems.add(item);

                    // You can also update the UI or perform other actions here
                  }
                }
              }
            }
          }

          event.setDropCompleted(success);
          event.consume();
        });

    interactionHandler = new ShapeInteractionHandler();
    // set glow for back image
    if (backImg != null) {
      backImg.setOnMouseEntered(event -> interactionHandler.glowThis(backImg));
      backImg.setOnMouseExited(event -> interactionHandler.unglowThis(backImg));
    }
  }

  // Method to update the image states based on the player's inventory
  public void updateImageStates() {
    // Enable or disable images based on the presence of items in the inventory
    System.out.println("Updating image states");
    if (inventory.contains(Items.Item.BAT_WINGS)) {
      batWingImage.setDisable(false);
      System.out.println("Bat wings found");
    }
    batWingImage.setDisable(!inventory.contains(Items.Item.BAT_WINGS));
    crystalImage.setDisable(!inventory.contains(Items.Item.CRYSTAL));
    insectWingImage.setDisable(!inventory.contains(Items.Item.INSECT_WINGS));
    talonImage.setDisable(!inventory.contains(Items.Item.TALON));
    powderImage.setDisable(!inventory.contains(Items.Item.POWDER));
    tailImage.setDisable(!inventory.contains(Items.Item.TAIL));
    featherImage.setDisable(!inventory.contains(Items.Item.FEATHER));
    scalesImage.setDisable(!inventory.contains(Items.Item.SCALES));
    flowerImage.setDisable(!inventory.contains(Items.Item.FLOWER));
    wreathImage.setDisable(!inventory.contains(Items.Item.WREATH));
    boneImage.setDisable(!inventory.contains(Items.Item.BONE));
    fireImage.setDisable(!inventory.contains(Items.Item.FIRE));
    rootImage.setDisable(!inventory.contains(Items.Item.ROOT));
    beetleImage.setDisable(!inventory.contains(Items.Item.BEETLE));
    unicornHornImage.setDisable(!inventory.contains(Items.Item.UNICORN_HORN));
    // enabling and setting opacity to 1 if item is in inventory
    if (inventory.contains(Items.Item.BAT_WINGS)) {
      batWingImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.CRYSTAL)) {
      crystalImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.INSECT_WINGS)) {
      insectWingImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.TALON)) {
      talonImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.POWDER)) {
      powderImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.TAIL)) {
      tailImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.FEATHER)) {
      featherImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.SCALES)) {
      scalesImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.FLOWER)) {
      flowerImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.WREATH)) {
      wreathImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.BONE)) {
      boneImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.FIRE)) {
      fireImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.ROOT)) {
      rootImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.BEETLE)) {
      beetleImage.setOpacity(1);
    }
    if (inventory.contains(Items.Item.UNICORN_HORN)) {
      unicornHornImage.setOpacity(1);
    }
  }

  @FXML
  private void setupDragAndDrop(ImageView itemImageView, String itemId) {
    AtomicReference<Double> originalX = new AtomicReference<>(0.0);
    AtomicReference<Double> originalY = new AtomicReference<>(0.0);

    itemImageView.setOnDragDetected(
        event -> {
          System.out.println("Drag detected");
          // Store the itemImageView as the draggedItem
          draggedItem = itemImageView;
          // Start the drag and drop operation
          Dragboard db = itemImageView.startDragAndDrop(TransferMode.MOVE);

          ClipboardContent content = new ClipboardContent();
          content.putString(itemId);
          db.setContent(content);

          originalX.set(event.getSceneX());
          originalY.set(event.getSceneY());

          event.consume();
        });

    itemImageView.setOnDragDone(
        event -> {
          System.out.println("Drag done");
          if (event.getTransferMode() == TransferMode.MOVE) {
            // Handle the end of the drag operation
            // The item was successfully dropped
            // You can perform any additional actions here
          }
          event.consume();
        });

    itemImageView.setOnDragOver(
        dragOverEvent -> {
          dragOverEvent.acceptTransferModes(TransferMode.MOVE);
          dragOverEvent.consume();
        });
  }

  @FXML
  private void goBack() {
    System.out.println("CAULDRON -> CAULDRON_ROOM");
    TransitionAnimation.changeScene(pane, AppUi.CAULDRON_ROOM, false);
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    System.out.println(cauldronItems);
  }

  @FXML
  private void brewPotion() {
    System.out.println(Items.necessary);
    System.out.println(cauldronItems);
    // if more or less than 6 items have been dropped into the cauldron then the
    // potion is not brewed
    if (cauldronItems.size() < 5 || cauldronItems.size() > 5) {
      System.out.println("Potion not brewed");
      if (inventory.isEmpty()) {
        notificationText.setText("Find some ingredients!");
      }
      if (cauldronItems.size() < 5) {
        notificationText.setText("Add more ingredients!");
      } else if (cauldronItems.size() > 5) {
        notificationText.setText("Too many ingredients!");
      }
      Notification.notifyPopup(notificationBack, notificationText);
      return;
    }

    if (cauldronItems.size() == 5) {
      // check if the order of the items is correct by comparing cauldronItems with
      // Items.necessary
      if (cauldronItems.equals(Items.necessary)) {
        System.out.println("Potion brewed");
        // set scene to you win
        System.out.println("CAULDRON -> YOU_WIN");
        TransitionAnimation.changeScene(pane, AppUi.YOU_WIN, false);
        SceneManager.setTimerScene(AppUi.YOU_WIN);

      } else {
        System.out.println("Potion not brewed");
        notificationText.setText("Wrong recipe!");
        Notification.notifyPopup(notificationBack, notificationText);
      }
    }
  }

  @FXML
  private void resetItems() {
    // returning the items to the original position.
    // I KNOW I DID IT REALLY CANCER WAY LMAO but i ceebs using brain rn
    // batWingImage.setX(84);  it might not even be necessary nvm but keeping
    // just in case
    // batWingImage.setY(54);

    // resetting the items in the cauldron
    cauldronItems.clear();
    // resetting the images
    batWingImage.setVisible(true);
    crystalImage.setVisible(true);
    insectWingImage.setVisible(true);
    talonImage.setVisible(true);
    powderImage.setVisible(true);
    tailImage.setVisible(true);
    featherImage.setVisible(true);
    scalesImage.setVisible(true);
    flowerImage.setVisible(true);
    wreathImage.setVisible(true);
    boneImage.setVisible(true);
    fireImage.setVisible(true);
    rootImage.setVisible(true);
    beetleImage.setVisible(true);
    unicornHornImage.setVisible(true);
  }

  @FXML
  private void emptyCauldron() {
    resetItems();
  }
}
