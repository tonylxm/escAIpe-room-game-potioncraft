package nz.ac.auckland.se206.controllers;

import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ShapeInteractionHandler;

public class ShelfLeftController {
    public boolean itemOnePicked, itemTwoPicked, itemThreePicked, itemFourPicked, itemFivePicked;
    public boolean readyToAdd;
    public Items.Item item;

    @FXML private Rectangle itemOneRect;
    @FXML private Rectangle itemTwoRect;
    @FXML private Rectangle itemThreeRect;
    @FXML private Rectangle itemFourRect;
    @FXML private Rectangle itemFiveRect;
    @FXML private Rectangle textRect;
    @FXML private Polygon rightShpe;
    @FXML private Label textLbl;
    @FXML private Label noLbl;
    @FXML private Label yesLbl;
    @FXML private Label dashLbl;

    @FXML private ShapeInteractionHandler interactionHandler;

    public void initialize() {
        itemOnePicked = false;
        itemTwoPicked = false;
        itemThreePicked = false;
        itemFourPicked = false;
        itemFivePicked = false;
        readyToAdd = false;

        interactionHandler = new ShapeInteractionHandler();
        if (itemOneRect != null) {
            itemOneRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemOneRect.setOnMouseExited(event -> interactionHandler.handle(event));
            itemOneRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_1));
        }
        if (itemTwoRect != null) {
            itemTwoRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemTwoRect.setOnMouseExited(event -> interactionHandler.handle(event));
            itemTwoRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_2));
        }
        if (itemThreeRect != null) {
            itemThreeRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemThreeRect.setOnMouseExited(event -> interactionHandler.handle(event));
            itemThreeRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_3));
        }
        if (itemFourRect != null) {
            itemFourRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemFourRect.setOnMouseExited(event -> interactionHandler.handle(event));
            itemFourRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_4));
        }
        if (itemFiveRect != null) {
            itemFiveRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemFiveRect.setOnMouseExited(event -> interactionHandler.handle(event));
            itemFiveRect.setOnMouseClicked(event -> itemSelect(Items.Item.ITEM_5));
        }
        if (rightShpe != null) {
            rightShpe.setOnMouseEntered(event -> interactionHandler.handle(event));
            rightShpe.setOnMouseExited(event -> interactionHandler.handle(event));
        }
    }

    /** Changing scenes to the cauldron room */
    @FXML 
    public void goRight(MouseEvent event){
        System.out.println("SHELF LEFT > CAULDRON ROOM");
        setText("", false);
        readyToAdd = false;
        Scene currentScene = rightShpe.getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    }

    /** 
     * Selecting the item and prompting user and prompting user to either 
     * add or not add the item to their inventory. Does nothing if the item
     * has already been added to the inventory.
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

        switch(item) {
            case ITEM_1:
                itemOneRect.setOpacity(0);
                itemOnePicked = true;
                break;
            case ITEM_2:
                itemTwoRect.setOpacity(0);
                itemTwoPicked = true;
                break;
            case ITEM_3:
                itemThreeRect.setOpacity(0);
                itemThreePicked = true;
                break;
            case ITEM_4:
                itemFourRect.setOpacity(0);
                itemFourPicked = true;
                break;
            case ITEM_5:
                itemFiveRect.setOpacity(0);
                itemFivePicked = true;
                break;
            default:
                break;
        }
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
            // Desicion labels need to be refactored to deal with
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
}
