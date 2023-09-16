package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TransitionAnimation {
  private static double fromValue;
  private static double toValue;
  private static boolean isFadeIn = false;

  // For fadeIn, set ocpacity = 1.0
  // For fadeOut, set ocpacity = 0.0
  public static void fade(Node obj, double opacity) {
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), obj);

    // Fade in
    if (opacity == 1.0) {
      fromValue = 0.0;
      toValue = 1.0;

      // Fade in startBtn intitally as disabled and greyed out until settings have
      // been selected
    } else if (opacity == 0.4) {
      fromValue = 0.0;
      toValue = 0.4;
      // Fade out
    } else {
      fromValue = 1.0;
      toValue = 0.0;
    }
    fadeTransition.setFromValue(fromValue);
    fadeTransition.setToValue(toValue);
    fadeTransition.play();
  }

  public static void changeScene(Node obj, AppUi appUi, double s) {
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(s), obj);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);
    fadeOut.setOnFinished(
        (ActionEvent event) -> {
            Parent root = SceneManager.getUiRoot(appUi);
            root.setOpacity(0);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            obj.getScene().setRoot(root);
            fadeIn.play();
        });
    fadeOut.play();
  }
}
