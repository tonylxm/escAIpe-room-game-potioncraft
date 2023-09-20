package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.Items;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the book view. */
public class BookController {
  @FXML 
  private Pane pane;
  @FXML 
  private ImageView cauldronRoomBackground;
  @FXML 
  private ImageView treasureBackground;
  @FXML 
  private ImageView libraryBackground;
  @FXML 
  private Rectangle backgroundShade;

  @FXML 
  private ImageView backBtn;
  @FXML 
  private ListView<String> ingredientList;
  @FXML 
  private ImageView ttsBtn1;
  @FXML 
  private Label timerLabel;

  @FXML
  private ImageView placeholderImg;
  @FXML
  private ImageView itemOneImg;
  @FXML
  private ImageView itemTwoImg;
  @FXML
  private ImageView itemThreeImg;
  @FXML
  private ImageView itemFourImg;
  @FXML
  private ImageView itemFiveImg;
  @FXML
  private ImageView itemSixImg;
  @FXML
  private ImageView itemSevenImg;
  @FXML
  private ImageView itemEightImg;
  @FXML
  private ImageView itemNineImg;
  @FXML
  private ImageView itemTenImg;
  @FXML
  private ImageView itemElevenImg;
  @FXML
  private ImageView itemTwelveImg;
  @FXML
  private ImageView itemThirteenImg;
  @FXML
  private ImageView itemFourteenImg;
  @FXML
  private ImageView itemFifteenImg;

  @FXML 
  private CountdownTimer countdownTimer;

  /**
   * Initializes the chat view, loading the riddle. Also initialises ways to view the appropriate
   * images for the required items.
   *
   * @param <T>
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() {
    countdownTimer = MainMenuController.getCountdownTimer();
    countdownTimer.setBookTimerLabel(timerLabel);

    writeRecipeIngredients(Items.necessary);

    RoomController.btnMouseActions(backBtn);

    ingredientList.setStyle("-fx-font-size: 1.5em ;");
    ingredientList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<String>() {

              // Overrideing appropriate method in ChangeListener to swap between images
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String item = ingredientList.getSelectionModel().getSelectedItem();
                // Removing the number and colon from the string
                item = item.substring(item.indexOf(".") + 2);

                // Setting all the images to be transparent initially
                placeholderImg.setOpacity(0);
                itemOneImg.setOpacity(0);
                itemTwoImg.setOpacity(0);
                itemThreeImg.setOpacity(0);
                itemFourImg.setOpacity(0);
                itemFiveImg.setOpacity(0);
                itemSixImg.setOpacity(0);
                itemSevenImg.setOpacity(0);
                itemEightImg.setOpacity(0);
                itemNineImg.setOpacity(0);
                itemTenImg.setOpacity(0);
                itemElevenImg.setOpacity(0);
                itemTwelveImg.setOpacity(0);
                itemThirteenImg.setOpacity(0);
                itemFourteenImg.setOpacity(0);
                itemFifteenImg.setOpacity(0);

                // Setting the appropriate image to be visible based on the selected item
                switch (item) {
                  case "TAIL":
                    itemOneImg.setOpacity(1);
                    break;
                  case "INSECT_WINGS":
                    itemTwoImg.setOpacity(1);
                    break;
                  case "FLOWER":
                    itemThreeImg.setOpacity(1);
                    break;
                  case "SCALES":
                    itemFourImg.setOpacity(1);
                    break;
                  case "POWDER":
                    itemFiveImg.setOpacity(1);
                    break;
                  case "TALON":
                    itemSixImg.setOpacity(1);
                    break;
                  case "CRYSTAL":
                    itemSevenImg.setOpacity(1);
                    break;
                  case "BAT_WINGS":
                    itemEightImg.setOpacity(1);
                    break;
                  case "WREATH":
                    itemNineImg.setOpacity(1);
                    break;
                  case "FEATHER":
                    itemTenImg.setOpacity(1);
                    break;
                  case "BONE":
                    itemElevenImg.setOpacity(1);
                    break;
                  case "FIRE":
                    itemTwelveImg.setOpacity(1);
                    break;
                  case "ROOT":
                    itemThirteenImg.setOpacity(1);
                    break;
                  case "BEETLE":
                    itemFourteenImg.setOpacity(1);
                    break;
                  case "UNICORN_HORN":
                    itemFifteenImg.setOpacity(1);
                    break;
                }
              }
            });
  }

  private void writeRecipeIngredients(List<Items.Item> necessary) {
    for (int i = 0; i < necessary.size(); i++) {
      ingredientList.getItems().add(Integer.toString(i + 1) + ". " + necessary.get(i).toString());
    }
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack() {
    System.out.println("BOOK -> " + SceneManager.currScene);
    TransitionAnimation.changeScene(pane, SceneManager.currScene, false);
  }

  /** Uses text to speech to read the required items in the book. */
  public void readIngredientList() {
    // Using concurency to prevent the system freezing
    Task<Void> speakTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        App.textToSpeech.speak("Potion Recipe");
        for (int i = 0; i < Items.necessary.size(); i++) {
          App.textToSpeech.speak(ingredientList.getItems().get(i));
        }
        return null;
      }
    };
    Thread speakThread = new Thread(speakTask, "Speak Thread");
    speakThread.start();
  }

  /** Setting the appropriate scene when transitioning to the book view. */
  public void updateBackground() {
    AppUi scene = SceneManager.getTimerScene();
    // Going from the cauldron room to the book
    if (scene == AppUi.CAULDRON_ROOM) {
      cauldronRoomBackground.setOpacity(1);
      treasureBackground.setOpacity(0);
      libraryBackground.setOpacity(0);
    }

    // Going from the library to the book
    if (scene == AppUi.LIBRARY_ROOM) {
      cauldronRoomBackground.setOpacity(0);
      treasureBackground.setOpacity(0);
      libraryBackground.setOpacity(1);
    }

    // Going from the treasure room to the book
    if (scene == AppUi.TREASURE_ROOM) {
      cauldronRoomBackground.setOpacity(0);
      treasureBackground.setOpacity(1);
      libraryBackground.setOpacity(0);
    }
  }
}
