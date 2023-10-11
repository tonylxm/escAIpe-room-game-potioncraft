package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class for playing sound effects and music in the game. Only one sound effect
 * can be played at a time. The music is played in the background and can be
 * played at the same time as a sound effect.
 */
public class SoundEffects {
  private MediaPlayer player;

  /**
   * Plays a sound effect give the string for the file name that the 
   * sound needs to be played from.
   * 
   * @param fileName the name of the sound file to be played.
   * @throws URISyntaxException if the file is not found.
   */
  public void playSoundEffect(String fileName) throws URISyntaxException {
    // Playing the sound effect
    Media soundEffect = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    if (player != null) {
      player.stop();
    }
    // Using concurrency to play sound effect in the background
    player = new MediaPlayer(soundEffect);
    player.play();
  }

  /**
   * Plays the harry potter theme song. Making sure the theme is only played
   * in the background so that is is only in the background.
   * 
   * @throws URISyntaxException if the file is not found.
   */
  public void playGameTheme() throws URISyntaxException {
    // Playing the harry potter theme
    Media music = new Media(App.class.getResource("/sounds/gameTheme.mp3").toURI().toString());
    // Using concurrency to play music in the background
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
}
