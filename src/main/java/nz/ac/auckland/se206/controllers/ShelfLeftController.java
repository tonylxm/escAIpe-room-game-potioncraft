package nz.ac.auckland.se206.controllers;

import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

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

    public void initialize() {
        itemOnePicked = false;
        itemTwoPicked = false;
        itemThreePicked = false;
        itemFourPicked = false;
        itemFivePicked = false;
        readyToAdd = false;
    }

    /** Changing scenes to the cauldron room */
    @FXML 
    public void goRight(MouseEvent event){
        System.out.println("SHELF LEFT > CAULDRON ROOM");
        Scene currentScene = rightShpe.getScene();
        currentScene.setRoot(SceneManager.getUiRoot(AppUi.CAULDRON_ROOM));
    }

    /** Highlighting when hovered over */
    @FXML
    public void goRightGlow() {
        rightShpe.setStrokeWidth(5);
    }

    @FXML
    public void goRightGlowExit() {
        rightShpe.setStrokeWidth(0);
    }

    /** Highlighting when hovered over */
    @FXML
    public void itemOneGlow() {
        if (itemOnePicked) return;
        itemOneRect.setStrokeWidth(5);
    }

    @FXML void itemOneGlowExit() {
        if (itemOnePicked) return;
        itemOneRect.setStrokeWidth(0);
    }
    
    /** Selecting item one and prompting user to add to inventory */
    @FXML
    public void itemOneSelect() {
        if (itemOnePicked) return;
        textLbl.setText("Add to inventory?");
        textRect.setOpacity(1);
        textLbl.setOpacity(1);
        yesLbl.setOpacity(1);
        noLbl.setOpacity(1);
        dashLbl.setOpacity(1);
        item = Items.Item.ITEM_1;
        readyToAdd = true;
    }

    /** Highlighting when hovered over */
    @FXML
    public void itemTwoGlow() {
        if (itemTwoPicked) return;
        itemTwoRect.setStrokeWidth(5);
    }

    @FXML
    public void itemTwoGlowExit() {
        if (itemTwoPicked) return;
        itemTwoRect.setStrokeWidth(0);
    }

    /** Selecting item two and prompting user to add to inventory */
    @FXML
    public void itemTwoSelect() {
        if (itemTwoPicked) return;
        textLbl.setText("Add to inventory?");
        textRect.setOpacity(1);
        textLbl.setOpacity(1);
        yesLbl.setOpacity(1);
        noLbl.setOpacity(1);
        dashLbl.setOpacity(1);
        item = Items.Item.ITEM_2;
        readyToAdd = true;
    }

    /** Highlighting when hovered over */
    @FXML
    public void itemThreeGlow() {
        if (itemThreePicked) return;
        itemThreeRect.setStrokeWidth(5);
    }

    @FXML
    public void itemThreeGlowExit() {
        if (itemThreePicked) return;
        itemThreeRect.setStrokeWidth(0);
    }

    /** Selecting item three and prompting user to add to inventory */
    @FXML
    public void itemThreeSelect() {
        if (itemThreePicked) return;
        textLbl.setText("Add to inventory?");
        textRect.setOpacity(1);
        textLbl.setOpacity(1);
        yesLbl.setOpacity(1);
        noLbl.setOpacity(1);
        dashLbl.setOpacity(1);
        item = Items.Item.ITEM_3;
        readyToAdd = true;
    }

    /** Highlighting when hovered over */
    @FXML
    public void itemFourGlow() {
        if (itemFourPicked) return;
        itemFourRect.setStrokeWidth(5);
    }

    @FXML
    public void itemFourGlowExit() {
        if (itemFourPicked) return;
        itemFourRect.setStrokeWidth(0);
    }

    /** Selecting item four and prompting user to add to inventory */
    @FXML
    public void itemFourSelect() {
        if (itemFourPicked) return;
        textLbl.setText("Add to inventory?");
        textRect.setOpacity(1);
        textLbl.setOpacity(1);
        yesLbl.setOpacity(1);
        noLbl.setOpacity(1);
        dashLbl.setOpacity(1);
        item = Items.Item.ITEM_4;
        readyToAdd = true;
    }

    /** Highlighting when hovered over */
    @FXML
    public void itemFiveGlow() {
        if (itemFivePicked) return;
        itemFiveRect.setStrokeWidth(5);
    }

    @FXML
    public void itemFiveGlowExit() {
        if (itemFivePicked) return;
        itemFiveRect.setStrokeWidth(0);
    }

    /** Selecting item five and prompting user to add to inventory */
    @FXML
    public void itemFiveSelect() {
        if (itemFivePicked) return;
        textLbl.setText("Add to inventory?");
        textRect.setOpacity(1);
        textLbl.setOpacity(1);
        yesLbl.setOpacity(1);
        noLbl.setOpacity(1);
        dashLbl.setOpacity(1);
        item = Items.Item.ITEM_5;
        readyToAdd = true;
    }

    /** Adding item to inventory if an item is selected */
    @FXML
    public void addItem() {
        if (!readyToAdd) return;
        MainMenuController.inventory.add(item);
        textLbl.setText("");
        textRect.setOpacity(0);
        textLbl.setOpacity(0);
        yesLbl.setOpacity(0);
        noLbl.setOpacity(0);
        dashLbl.setOpacity(0);
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

        System.out.println();
        Iterator itr = new MainMenuController().inventory.inventory.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    /** Not adding a selected item to the inventory */
    @FXML
    public void noAdd() {
        if (!readyToAdd) return;
        textLbl.setText("");
        textRect.setOpacity(0);
        textLbl.setOpacity(0);
        yesLbl.setOpacity(0);
        noLbl.setOpacity(0);
        dashLbl.setOpacity(0);
        readyToAdd = false;
    }
}
