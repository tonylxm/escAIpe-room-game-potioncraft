package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
  private MediaPlayer player;

  public void playSoundEffect(String fileName) throws URISyntaxException {
    Media soundEffect = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    if (player != null) {
      player.stop();
    }
    player = new MediaPlayer(soundEffect);
    player.play();
  }
}
