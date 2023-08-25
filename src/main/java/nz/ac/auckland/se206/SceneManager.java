package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    MAIN_MENU,
    ROOM,
    BOOKSHELF,
    CAULDRON,
    SHELF_LEFT,
    SHELF_RIGHT,
    GAME_OVER
  }

  public static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addAppUi(AppUi appUi, Parent root) {
    sceneMap.put(appUi, root);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
