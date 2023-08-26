package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionAnimation {
  private double fromValue;
  private double toValue;

  // For fadeIn, set appear = true
  // For fadeOut, set appear = true
  public void fade(Node obj, boolean appear) {
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), obj);

    if (appear) {
      fromValue = 0.0;
      toValue = 1.0;
    } else {
      fromValue = 1.0;
      toValue = 0.0;
    }

    fadeOut.setFromValue(fromValue);
    fadeOut.setToValue(toValue);
    fadeOut.play();
  }
}
