package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Notification class for displaying a notification popup. The notification popup is a 
 * transparent black background with white text. The notification popup is displayed in 
 * the middle of the screen. The notification popup is displayed on top of all other 
 * nodes.
 */
public class Notification {

  public static SoundEffects soundEffects = new SoundEffects();

  /**
   * Displays a notification popup for 3 seconds. The notification popup is a transparent black
   * background with white text. The notification popup is displayed in the middle of the screen.
   * The notification popup is displayed on top of all other nodes.
   *
   * @param notificationBack the ImageView of the notification background.
   * @param notificationText the Label of the notification text.
   * @throws URISyntaxException if the sound effect file is not found.
   */
  public static void notifyPopup(
      ImageView notificationBack, Label notificationText) 
      throws URISyntaxException {
    notificationBack.setDisable(false);
    notificationText.setDisable(false);
    soundEffects.playSound("notification.mp3");

    // Create a FadeTransition to gradually change opacity over 3 seconds
    FadeTransition fadeTransition = new FadeTransition(
        Duration.seconds(5), notificationBack);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(1.0);
    FadeTransition fadeTransition2 = new FadeTransition(
        Duration.seconds(5), notificationText);
    fadeTransition2.setFromValue(1.0);
    fadeTransition2.setToValue(1.0);

    // Play the fade-in animation
    fadeTransition.play();
    fadeTransition2.play();

    // Schedule a task to fade out the image after 3 seconds
    fadeTransition.setOnFinished(fadeEvent -> {
      if (notificationBack.getOpacity() == 1.0) {
        FadeTransition fadeOutTransition = new FadeTransition(
            Duration.seconds(1.5), notificationBack);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.play();
      }
    });

    fadeTransition2.setOnFinished(fadeEvent -> {
      if (notificationText.getOpacity() == 1.0) {
        FadeTransition fadeOutTransition = new FadeTransition(
            Duration.seconds(1.5), notificationText);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.play();
      }
    });   
    
    notificationBack.setDisable(true);
    notificationText.setDisable(true);
  }
}
