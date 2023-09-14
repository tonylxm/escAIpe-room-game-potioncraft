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
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CauldronController {
  private static CauldronController instance;

  @FXML private Label returnLbl;
  @FXML private ImageView batWingImage;
  @FXML private ImageView crystalImage;
  @FXML private ImageView insectWingImage;
  @FXML private ImageView talonImage;
  @FXML private ImageView powderImage;
  @FXML private ImageView tailImage;
  @FXML private ImageView featherImage;
  @FXML private ImageView scalesImage;
  @FXML private ImageView flowerImage;
  @FXML private ImageView wreathImage;
  @FXML private ImageView cauldronImageView;
  @FXML private Rectangle cauldronOverlay;
  @FXML private Button brewBtn;

  private Map<String, Items.Item> imageViewToItemMap = new HashMap<>();
  private Set<Items.Item> inventory;
  
  //array to store the items dropped into the cauldron
  private ArrayList<Items.Item> cauldronItems = new ArrayList<Items.Item>();

  private ImageView draggedItem;

  public CauldronController() {
    instance = this;
  }

  @FXML
  private void initialize() {
    inventory = MainMenuController.inventory.getInventory();
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

    //defining mapping
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
    returnLbl.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    System.out.println(cauldronItems);
  }

  @FXML
  private void brewPotion() {
    System.out.println(Items.necessary);
    System.out.println(cauldronItems);
    //if less than 6 items have been dropped into the cauldron then the potion is not brewed
    if (cauldronItems.size() < 5) {
      System.out.println("Potion not brewed");
      resetItems();
      return;
    }

    //if more than 6 items have been dropped into the cauldron then the potion is not brewed
    if (cauldronItems.size() > 5) {
      System.out.println("Potion not brewed");
        resetItems();
      
      return;
    }

    if (cauldronItems.size() == 5) {
        //check if the order of the items is correct by comparing cauldronItems with Items.necessary
        if (cauldronItems.equals(Items.necessary)) {
            System.out.println("Potion brewed");
            //set scene to you win
            System.out.println("CAULDRON -> YOU_WIN");
            returnLbl.getScene().setRoot(SceneManager.getUiRoot(AppUi.YOU_WIN));
            SceneManager.setTimerScene(AppUi.YOU_WIN);
            
            
        } else {
            System.out.println("Potion not brewed");
            resetItems();
        }
    }


  }

  @FXML 
  private void resetItems() {
    //returning the items to the original position. I KNOW I DID IT REALLY CANCER WAY LMAO but i ceebs using brain rn
    // batWingImage.setX(84);  it might not even be necessary nvm but keeping just in case
    // batWingImage.setY(54);

    //resetting the items in the cauldron
    cauldronItems.clear();
    //resetting the images
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
  }
}