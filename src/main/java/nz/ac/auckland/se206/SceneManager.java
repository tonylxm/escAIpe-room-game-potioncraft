package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.BookController;
import nz.ac.auckland.se206.controllers.CauldronController;
import nz.ac.auckland.se206.controllers.CauldronRoomController;
import nz.ac.auckland.se206.controllers.ChestController;
import nz.ac.auckland.se206.controllers.GameOverController;
import nz.ac.auckland.se206.controllers.LibraryRoomController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.TreasureRoomController;

public class SceneManager {

  /**
   * Enum for the different scenes in the game.
   */
  public enum AppUi {
    MAIN_MENU,
    CAULDRON_ROOM,
    BOOK,
    BOOKSHELF,
    LIBRARY_ROOM,
    TREASURE_ROOM,
    YOU_WIN,
    YOU_LOSE,
    CAULDRON,
    CHEST
  }

  public static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  public static AppUi currScene;
  public static AppUi timerScene;
  private static CauldronController cauldronControllerInstance;
  private static BookController bookControllerInstance;
  private static LibraryRoomController  libraryRoomControllerInstance;
  private static TreasureRoomController treasureRoomControllerInstance;
  private static CauldronRoomController cauldronRoomControllerInstance;
  private static ChestController chestControllerInstance;
  private static GameOverController gameOverControllerInstance;


  /**
   * Returns the current controller based on the current scene.
   * 
   * @return
   */
  public static RoomController getCurrentController() {
    //switch case that returns the controller based on currScene
    RoomController controller = null;
    if (currScene == AppUi.LIBRARY_ROOM) {
      controller = libraryRoomControllerInstance;
    } else if (currScene == AppUi.TREASURE_ROOM) {
      controller = treasureRoomControllerInstance;
    } else if (currScene == AppUi.CAULDRON_ROOM) {
      controller = cauldronRoomControllerInstance;
    }
    return controller;
  }

  /**
   * Adds scene ui to the sceneMap.
   * 
   * @param appUi
   * @param root
   */
  public static void addAppUi(AppUi appUi, Parent root) {
    sceneMap.put(appUi, root);
  }

  /**
   * Returns the root of the scene.
   * 
   * @param appUi
   * @return
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  /**
   * Sets the current scene.
   * 
   * @param appUi
   */
  public static void setTimerScene(AppUi appUi) {
    if (sceneMap.containsKey(appUi)) {
      timerScene = appUi;
    } else {
      System.out.println("Scene not found.");
    }
  }

  /**
   * Returns the current scene.
   * 
   * @return
   */
  public static AppUi getTimerScene() {
    return timerScene;
  }
  
  /**
   * Sets the GameOverController instance.
   * 
   * @param controller
   */
  public static void setGameOverControllerInstance(GameOverController controller) {
    gameOverControllerInstance = controller;
  }

  /**
   * Returns the GameOverController instance.
   * 
   * @return
   */
  public static GameOverController getGameOverControllerInstance() {
    return gameOverControllerInstance;
  }

  /**
   * Sets the CauldronController instance.
   * 
   * @param controller
   */
  public static void setCauldronControllerInstance(CauldronController controller) {
    cauldronControllerInstance = controller;
  }

  /**
   * Returns the CauldronController instance.
   * 
   * @return
   */
  public static CauldronController getCauldronControllerInstance() {
    return cauldronControllerInstance;
  }

  /**
   * Sets the BookController instance.
   * 
   * @param controller
   */
  public static void setBookControllerInstance(BookController controller) {
    bookControllerInstance = controller;
  }

  /**
   * Returns the BookController instance.
   * 
   * @return
   */
  public static BookController getBookControllerInstance() {
    return bookControllerInstance;
  }

  /**
   * Sets the LibraryRoomController instance.
   * 
   * @param controller
   */
  public static void setLibraryRoomControllerInstance(LibraryRoomController controller) {
    libraryRoomControllerInstance = controller;
  }

  /**
   * Returns the LibraryRoomController instance.
   * 
   * @return
   */
  public static LibraryRoomController getLibraryRoomControllerInstance() {
    return libraryRoomControllerInstance;
  }

  /**
   * Sets the TreasureRoomController instance.
   * 
   * @param controller
   */
  public static void setTreasureRoomControllerInstance(TreasureRoomController controller) {
    treasureRoomControllerInstance = controller;
  }

  /**
   * Returns the TreasureRoomController instance.
   * 
   * @return
   */
  public static TreasureRoomController getTreasureRoomControllerInstance() {
    return treasureRoomControllerInstance;
  }

  /**
   * Sets the CauldronRoomController instance.
   * 
   * @param controller
   */
  public static void setCauldronRoomControllerInstance(CauldronRoomController controller) {
    cauldronRoomControllerInstance = controller;
  }

  /**
   * Returns the CauldronRoomController instance.
   * 
   * @return
   */
  public static CauldronRoomController getCauldronRoomControllerInstance() {
    return cauldronRoomControllerInstance;
  }

  /**
   * Sets the ChestController instance.
   * 
   * @param controller
   */
  public static ChestController getChestControllerInstance() {
    return chestControllerInstance;
  }

  /**
   * Returns the ChestController instance.
   * 
   * @return
   */
  public static void setChestControllerInstance(ChestController chestControllerInstance) {
    SceneManager.chestControllerInstance = chestControllerInstance;
  }
}
