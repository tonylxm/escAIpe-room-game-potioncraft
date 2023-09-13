package nz.ac.auckland.se206.controllers;

import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private Set<Items.Item> inventory;
    private static CauldronController instance;

    public CauldronController() {
        instance = this;
    }

    @FXML
    private void initialize() {
        inventory = MainMenuController.inventory.getInventory();
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
    private void goBack() {
    System.out.println("CAULDRON -> CAULDRON_ROOM");
    returnLbl.getScene().setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    SceneManager.setTimerScene(AppUi.CAULDRON_ROOM);
    }
}
