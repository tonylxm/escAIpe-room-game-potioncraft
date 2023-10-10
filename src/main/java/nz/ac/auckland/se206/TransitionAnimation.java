package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TransitionAnimation {
  private static double fromValue;
  private static double toValue;
  private static Pane masterPane;

  /**
   * Changing scenes to the appropriate scene. Used to change scenes.
   * Only changes scenes if the current scene is not the same as the
   * scene to change to.
   * 
   * @param pane the pane to change to.
   */
  public static void setMasterPane(Pane pane) {
    masterPane = pane;
  }

  /**
   * Fading in and out of the appropriate scene.
   * For fadeIn, set ocpacity = 1.0.
   * For fadeOut, set ocpacity = 0.0.
   * 
   * @param obj the node to fade.
   * @param opacity the opacity to fade to.
   */
  public static void fade(Node obj, double opacity) {
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), obj);

    // Fade in
    if (opacity == 1.0) {
      fromValue = 0.0;
      toValue = 1.0;

      // Fade in intitally as disabled and greyed out until settings have been selected
    } else if (opacity == 0.4) {
      fromValue = 0.0;
      toValue = 0.4;
      // Fade out
    } else {
      fromValue = obj.getOpacity();
      toValue = 0.0;
    }
    fadeTransition.setFromValue(fromValue);
    fadeTransition.setToValue(toValue);
    fadeTransition.play();
  }

  /**
   * Fading to black inbetween scenes to have a smooth transition. Used to change scenes.
   * Only changes scenes if the current scene is not the same as the scene to change to.
   * 
   * @param obj the node to fade.
   * @param appUi the scene to change to.
   * @param mainMenu whether the scene to change to is the main menu or not.
   */
  public static void changeScene(Node obj, AppUi appUi, boolean mainMenu) {
    double s1;
    double s2;

    if (mainMenu) {
      s1 = 2;
      s2 = 0.2;
    } else {
      s1 = 0.1;
      s2 = 0.1;
    }

    // Setting the appropriate roots and scenes for the fade transitions
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(s1), obj);
    // Setting values to fade from and to
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);
    fadeOut.setOnFinished(
        (ActionEvent event) -> {
          Parent root = SceneManager.getUiRoot(appUi);
          root.setOpacity(0);
          masterPane.getChildren().remove(obj);
          masterPane.getChildren().add(1, root);
          // Fading in to the next scene after the fade out has finished
          FadeTransition fadeIn = new FadeTransition(Duration.seconds(s2), root);
          fadeIn.setFromValue(0.0);
          fadeIn.setToValue(1.0);
          fadeIn.setNode(root);
          fadeIn.play();
        });
    fadeOut.play();
  }
}
