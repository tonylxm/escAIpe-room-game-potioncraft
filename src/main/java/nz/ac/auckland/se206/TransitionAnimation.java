package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TransitionAnimation {
  private static double fromValue;
  private static double toValue;

  // For fadeIn, set ocpacity = 1.0
  // For fadeOut, set ocpacity = 0.0
  public static void fade(Node obj, double ocpacity) {
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), obj);

    // Fade in
    if (ocpacity == 1.0) {
      fromValue = 0.0;
      toValue = 1.0;

      // Fade in startBtn intitally as disabled and greyed out until settings have
      // been selected
    } else if (ocpacity == 0.4) {
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

  public static void fadeScene(Node obj, double ocpacity, AppUi appUi) {
    FadeTransition fadeSceneTransition = new FadeTransition(Duration.seconds(2), obj);
    // Fade in
    if (ocpacity == 1.0) {
      fromValue = 0.0;
      toValue = 1.0;
      // Fade out
    } else {
      fromValue = 1.0;
      toValue = 0.0;
    }
    fadeSceneTransition.setFromValue(fromValue);
    fadeSceneTransition.setToValue(toValue);
    fadeSceneTransition.setOnFinished(
        (ActionEvent event) -> {
          obj.getScene().setRoot(SceneManager.getUiRoot(appUi));
        });
    fadeSceneTransition.play();
  }
}
