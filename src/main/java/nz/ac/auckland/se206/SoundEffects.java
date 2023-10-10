package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import java.time.Duration;

import javafx.concurrent.Task;
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

  public void playTheme(String themeName) throws URISyntaxException {
    Media music = new Media(App.class.getResource("/sounds/" + themeName).toURI().toString());
    Task<Void> musicTask =
        new Task<Void>() {
          @Override
          public Void call() throws Exception {
            player = new MediaPlayer(music);
            player.play();
            return null;
          }
        };
    new Thread(musicTask, "Game Theme Thread").start();
  }

  public void stop() {
    for (int i = 0; i < 10; i++) {
      player.setVolume(player.getVolume() - 0.1);
    }
    player.stop();
  }
}
