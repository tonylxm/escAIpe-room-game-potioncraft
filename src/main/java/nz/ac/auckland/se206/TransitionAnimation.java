package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionAnimation {
  private double fromValue;
  private double toValue;

  // For fadeIn, set ocpacity = 1.0
  // For fadeOut, set ocpacity = 0.0
  public void fade(Node obj, double ocpacity) {
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), obj);

    if (ocpacity == 1.0) {
      fromValue = 0.0;
      toValue = 1.0;

      // Fade in startBtn intitally as disabled and greyed out until settings have been selected
    } else if (ocpacity == 0.4) {
      fromValue = 0.0;
      toValue = 0.4;
    } else {
      fromValue = 1.0;
      toValue = 0.0;
    }

    fadeOut.setFromValue(fromValue);
    fadeOut.setToValue(toValue);
    fadeOut.play();
  }
}
