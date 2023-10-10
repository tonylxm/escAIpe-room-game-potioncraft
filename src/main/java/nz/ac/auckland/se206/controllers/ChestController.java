package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.CountdownTimer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SoundEffects;
import nz.ac.auckland.se206.gpt.ChatMessage;

/**
 * Controller for the chest room. This class handles all the actions that 
 * can be done in the chest room. This includes changing scenes to the 
 * treasure room, opening the chest and fading in the scene.
 */
public class ChestController {
  @FXML
  private Pane pane;
  @FXML
  private ImageView keyImg;
  @FXML
  private ImageView lightImg;
  @FXML
  private ImageView backImg;
  @FXML
  private Label timerLabel;
  @FXML
  private Rectangle fadeRectangle;

  private CountdownTimer timer;
  private Timeline pulse;
  private SoundEffects soundEffects;

  private double glower;
  private double glowerTwo;
  private boolean glowUp;
  private boolean glowUpTwo;

  /**
   * Initialising the glow effect and the drag and drop functionality 
   * for the key and light images.
   */
  @FXML
  private void initialize() {
    timer = MainMenuController.getCountdownTimer();
    timer.setChestLabel(timerLabel);
    soundEffects = new SoundEffects();
    setupDragAndDrop(keyImg);
    // Using two different values for the glow to make sure the user 
    // sees both images
    glower = 0.0;
    glowerTwo = 0.5;
    glowUp = true;
    glowUpTwo = true;
    // Setting up timeline for glowing effect
    pulse = new Timeline(
        new KeyFrame(
            Duration.millis(50),
            event -> {
              setGlow();
            }));
    pulse.setCycleCount(Timeline.INDEFINITE);
    pulse.play();

    backImg.setOnMouseEntered(event -> backImg.setEffect(new Glow(1)));
    backImg.setOnMouseExited(event -> backImg.setEffect(new Glow(0)));
  }

  /**
   * Setting the glows for both the key and the light images.
   * Using two different glows to create a pulsing effect where
   * each glow is out of phase by 90 degrees.
   */
  @FXML
  private void setGlow() {
    keyImg.setEffect(new Glow(glower));
    lightImg.setEffect(new Glow(glowerTwo));
    changeGlowOne();
    changeGlowTwo();
  }

  /**
   * Changing the value for the glow for the key image. Making sure
   * the glow value is either increasing or decreasing depending on
   * the glowUp value.
   */
  private void changeGlowOne() {
    // Changing the glow value for the key image
    if (glowUp) { 
      glower += 0.1;
      if (glower >= 1) {
        glowUp = false;
      }
    // Changing the glow value for the key image
    } else {
      glower -= 0.1;
      if (glower <= 0) {
        glowUp = true;
      }
    }
  }

  /**
   * Changing the value of the glow for the light image. Making sure
   * the glow value is either increasing or decreasing depending on
   * the glowUpTwo value.
   */
  private void changeGlowTwo() {
    // Changing the glow value for the light image
    if (glowUpTwo) {
      glowerTwo += 0.0375;
      if (glowerTwo >= 0.75) {
        glowUpTwo = false;
      }
    // Changing the glow value for the light image
    } else {
      glowerTwo -= 0.0375;
      if (glowerTwo <= 0) {
        glowUpTwo = true;
      }
    }
  }

  /**
   * Setting up drag and drop functionality for the key image.
   * When dropped on the glowing light image, the key will be put into the chest, unlocking
   * the other items in the treasure room, then moving to the treasure room.
   * 
   * @param itemImageView the image needing to be dragged and dropped.
   */
  @FXML
  private void setupDragAndDrop(ImageView itemImageView) {
    final AtomicReference<Double> originalX = new AtomicReference<>(0.0);
    final AtomicReference<Double> originalY = new AtomicReference<>(0.0);

    Task<Void> chestOpenedTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        // Sending a new chat message to gpt so that wizard knows that the chest has
        // been opened
        ChatMessage msg = new ChatMessage(
            "Wizard", MainMenuController.getChatHandler().runGpt(
            MainMenuController.getOpenedChestMessage()));
        TreasureRoomController treasureController = 
            SceneManager.getTreasureRoomControllerInstance();
        // Adding congratulatory message to the chat in the treasure room
        MainMenuController.getChatHandler().appendChatMessage(
            msg, treasureController.getTextArea(), 
            treasureController.getInputText(), treasureController.getSendButton());
        return null;
      }
    };

    // Setting up draggability
    itemImageView.setOnMousePressed(
        event -> {
          originalX.set(event.getSceneX() - itemImageView.getLayoutX());
          originalY.set(event.getSceneY() - itemImageView.getLayoutY());
        });

    // Setting up draggability
    itemImageView.setOnMouseDragged(
        event -> {
          double offsetX = event.getSceneX() - originalX.get();
          double offsetY = event.getSceneY() - originalY.get();

          itemImageView.setLayoutX(offsetX);
          itemImageView.setLayoutY(offsetY);
        });

    // Setting up drop functionality    
    itemImageView.setOnMouseReleased(
        event -> {
          // Define the target position relative to the scene
          System.out.println("Dropped");
          double targetX = 530;
          double targetY = 400;

          // Calculate the distance between the drop position and the target position
          double distance =
              Math.sqrt(
                  Math.pow(event.getSceneX() - targetX, 2)
                      + Math.pow(event.getSceneY() - targetY, 2));

          // print out the coordinates of where it was dropped
          System.out.println("X: " + event.getSceneX() + " Y: " + event.getSceneY());

          // Set a threshold for the maximum allowed distance
          double maxDistanceThreshold = 100;

          // If the image is dropped close enough to the target, stopping the glowing animation,
          // making the items in the treasure room visible, and moving to the treasure room
          if (distance <= maxDistanceThreshold) {
            new Thread(chestOpenedTask).start();
            pulse.stop();
            GameState.isChestOpen = true;
            SceneManager.getTreasureRoomControllerInstance().switchItems(GameState.isChestOpen);
            SceneManager.getTreasureRoomControllerInstance().setText(
                "The chest has been unlocked! \nNew items are available \nin the treasure room!", 
                true, false);
            System.out.println("Put the key into the glowing chest");
            try {
              soundEffects.playSoundEffect("openChest.mp3");
            } catch (URISyntaxException e) {
              e.printStackTrace();
            }
            //TransitionAnimation.changeScene(pane, AppUi.TREASURE_ROOM, false);
            Scene currentScene = fadeRectangle.getScene();
            currentScene.setRoot(SceneManager.getUiRoot(AppUi.TREASURE_ROOM));
            SceneManager.getTreasureRoomControllerInstance().fadeIn();
            SceneManager.setTimerScene(AppUi.TREASURE_ROOM);
          }
        });
  }

  /**
   * Moving back to the treasure room. Only used when the chest has been opened.
   * Does not need to be called after the user has opened the chest properly.
   */
  @FXML
  public void goBack() {
    System.out.println("CHEST -> TREASURE_ROOM");
    //TransitionAnimation.changeScene(pane, AppUi.TREASURE_ROOM, false);
    Scene currentScene = fadeRectangle.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(AppUi.TREASURE_ROOM));
    SceneManager.getTreasureRoomControllerInstance().fadeIn();
    SceneManager.setTimerScene(AppUi.TREASURE_ROOM);
  }

  /**
   * Fading in the treasure room. Only used when the chest has been opened.
   * Only handling the treasure room because the chest is only opened from the treasure room.
   */
  @FXML
  public void fadeIn() {
    FadeTransition ft = new FadeTransition(Duration.seconds(0.6), fadeRectangle);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.play();
  }
}
