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
        }
        if (itemTwoRect != null) {
            itemTwoRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemTwoRect.setOnMouseExited(event -> interactionHandler.handle(event));
        }
        if (itemThreeRect != null) {
            itemThreeRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemThreeRect.setOnMouseExited(event -> interactionHandler.handle(event));
        }
        if (itemFourRect != null) {
            itemFourRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemFourRect.setOnMouseExited(event -> interactionHandler.handle(event));
        }
        if (itemFiveRect != null) {
            itemFiveRect.setOnMouseEntered(event -> interactionHandler.handle(event));
            itemFiveRect.setOnMouseExited(event -> interactionHandler.handle(event));
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
        Scene currentScene = rightShpe.getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    }
    
    /** Selecting item one and prompting user to add to inventory */
    @FXML
    public void itemOneSelect() {
        if (itemOnePicked) return;
        setText("Add to inventory?", true);
        item = Items.Item.ITEM_1;
        readyToAdd = true;
    }

    /** Selecting item two and prompting user to add to inventory */
    @FXML
    public void itemTwoSelect() {
        if (itemTwoPicked) return;
        setText("Add to inventory?", true);
        item = Items.Item.ITEM_2;
        readyToAdd = true;
        System.out.println("item two clicked");
    }

    /** Selecting item three and prompting user to add to inventory */
    @FXML
    public void itemThreeSelect() {
        if (itemThreePicked) return;
        setText("Add to inventory?", true);
        item = Items.Item.ITEM_3;
        readyToAdd = true;
        System.out.println("item three clicked");
    }

    /** Selecting item four and prompting user to add to inventory */
    @FXML
    public void itemFourSelect() {
        if (itemFourPicked) return;
        setText("Add to inventory?", true);
        item = Items.Item.ITEM_4;
        readyToAdd = true;
        System.out.println("item four clicked");
    }

    /** Selecting item five and prompting user to add to inventory */
    @FXML
    public void itemFiveSelect() {
        if (itemFivePicked) return;
        setText("Add to inventory?", true);
        item = Items.Item.ITEM_5;
        readyToAdd = true;
        System.out.println("item five clicked");
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
        }
        System.out.println("Item added to inventory");
        System.out.println("Current Inventory:");
        Iterator itr = new MainMenuController().inventory.inventory.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
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

    @FXML
    private void setText(String text, boolean on) {
        textLbl.setText(text);
        if (on) {
            textRect.setOpacity(1);
            textLbl.setOpacity(1);
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
