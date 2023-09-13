package nz.ac.auckland.se206.controllers;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class CauldronController {
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

    private Set<Items.Item> inventory;
    private static CauldronController instance;

    public CauldronController() {
        instance = this;
    }

    @FXML
    private void initialize() {
        inventory = MainMenuController.inventory.getInventory();
        //set up drag and drop for all images
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
        
        //disable all image
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

        cauldronImageView.setOnDragOver(event -> {
            if (event.getGestureSource() != cauldronImageView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        
        cauldronImageView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
        
            if (db.hasString()) {
                // Get the identifier of the dropped item
                String itemId = db.getString();
        
                // // Check if the dropped item is valid (e.g., based on the identifier)
                // if (isValidItem(itemId)) {
                //     // Perform actions to add the item to the cauldron (e.g., update the inventory)
                //     // You can also update the UI to reflect the item being added to the cauldron
                //     success = true;
                // }
            }
        
            event.setDropCompleted(success);
            event.consume();
        });
    
    }

    // Method to update the image states based on the player's inventory
    public void updateImageStates() {
        // Enable or disable images based on the presence of items in the inventory
        System.out.println("Updating image states");
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
        //enabling and setting opacity to 1 if item is in inventory
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

    itemImageView.setOnDragDetected(event -> {
        Dragboard db = itemImageView.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.putString(itemId); // Use the item's unique identifier
        db.setContent(content);

        // Store the original position of the item
        originalX.set(event.getSceneX());
        originalY.set(event.getSceneY());

        event.consume();
    });

    itemImageView.setOnDragDone(event -> {
        if (event.getTransferMode() == TransferMode.MOVE) {
            // Handle the end of the drag operation
            // The item was successfully dropped
            // You can perform any additional actions here
        }
        event.consume();
    });

    // Add an event handler to update the item's position while dragging
    itemImageView.setOnDragOver(dragOverEvent -> {
        // Calculate the new position based on the mouse cursor's position
        double offsetX = dragOverEvent.getSceneX() - originalX.get();
        double offsetY = dragOverEvent.getSceneY() - originalY.get();

        // Update the item's position
        itemImageView.setLayoutX(itemImageView.getLayoutX() + offsetX);
        itemImageView.setLayoutY(itemImageView.getLayoutY() + offsetY);

        // Store the new position as the original position for the next drag event
        originalX.set(dragOverEvent.getSceneX());
        originalY.set(dragOverEvent.getSceneY());

        dragOverEvent.acceptTransferModes(TransferMode.MOVE);
        dragOverEvent.consume();
    });
}

    


    

    
    



    @FXML 
    private void goBack() {
    System.out.println("CAULDRON -> CAULDRON_ROOM");
    returnLbl.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    }
}
