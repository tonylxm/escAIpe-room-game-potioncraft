package nz.ac.auckland.se206.controllers;

import java.util.concurrent.atomic.AtomicReference;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.TransitionAnimation;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ChestController {
  @FXML
  private Pane pane;
  @FXML
  private ImageView keyImg;

  @FXML
  private void initialize() {
    setupDragAndDrop(keyImg);
  }

  @FXML
  private void setupDragAndDrop(ImageView itemImageView) {
    final AtomicReference<Double> originalX = new AtomicReference<>(0.0);
    final AtomicReference<Double> originalY = new AtomicReference<>(0.0);

    itemImageView.setOnMousePressed(
        event -> {
          originalX.set(event.getSceneX() - itemImageView.getLayoutX());
          originalY.set(event.getSceneY() - itemImageView.getLayoutY());
        });

    itemImageView.setOnMouseDragged(
        event -> {
          double offsetX = event.getSceneX() - originalX.get();
          double offsetY = event.getSceneY() - originalY.get();

          itemImageView.setLayoutX(offsetX);
          itemImageView.setLayoutY(offsetY);
        });

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

          if (distance <= maxDistanceThreshold) {
            GameState.isChestOpen = true;
            SceneManager.getTreasureRoomControllerInstance().switchItems(GameState.isChestOpen);
            System.out.println("Put the key into the glowing chest");
            TransitionAnimation.changeScene(pane, AppUi.TREASURE_ROOM, false);
            SceneManager.setTimerScene(AppUi.TREASURE_ROOM);
          }
        });
  }
}
