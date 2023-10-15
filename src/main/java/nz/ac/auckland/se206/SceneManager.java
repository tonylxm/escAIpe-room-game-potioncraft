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

/**
 * SceneManager class for the scene manager. Used to manage the scenes in the game.
 * Contains methods for adding scenes to the sceneMap, getting the root of the scene,
 * setting the current scene, getting the current scene, getting the current controller,
 * setting the GameOverController instance, getting the GameOverController instance,
 * setting the CauldronController instance, getting the CauldronController instance,
 * setting the BookController instance, getting the BookController instance,
 * setting the LibraryRoomController instance, getting the LibraryRoomController instance,
 * setting the TreasureRoomController instance, getting the TreasureRoomController instance,
 * setting the CauldronRoomController instance, getting the CauldronRoomController instance,
 * setting the ChestController instance, getting the ChestController instance.
 */
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
   * Returns the current controller based on the current scene. Used to get the current controller.
   * Doesn't return anything. Returns the current controller.
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
   * Adds scene ui to the sceneMap. Used to add the scene ui to the sceneMap.
   * Doesn't return anything. Only adds the scene ui to the sceneMap.
   *
   * @param appUi the scene ui.
   * @param root the root of the scene.
   */
  public static void addAppUi(AppUi appUi, Parent root) {
    sceneMap.put(appUi, root);
  }

  /**
   * Returns the root of the scene. Used to get the root of the scene. Doesn't return anything.
   * Returns the root of the scene.
   *
   * @param appUi the root of the scene.
   * @return the root of the scene.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  /**
   * Sets the current scene. Used to set the current scene. Doesn't return anything.
   * Only sets the current scene. 
   *
   * @param appUi the current scene.
   */
  public static void setTimerScene(AppUi appUi) {
    if (sceneMap.containsKey(appUi)) {
      timerScene = appUi;
    } else {
      System.out.println("Scene not found.");
    }
  }

  /**
   * Returns the current scene. Used to get the current scene. Doesn't return anything.
   * Returns the current scene.
   *
   * @return the current scene.
   */
  public static AppUi getTimerScene() {
    return timerScene;
  }
  
  /**
   * Sets the GameOverController instance. Used to set the GameOverController instance.
   * Doesn't return anything. Only sets the GameOverController instance.
   *
   * @param controller the GameOverController instance.
   */
  public static void setGameOverControllerInstance(GameOverController controller) {
    gameOverControllerInstance = controller;
  }

  /**
   * Returns the GameOverController instance. Used to get the GameOverController instance.
   * Doesn't return anything. Returns the GameOverController instance.
   *
   * @return the GameOverController instance.
   */
  public static GameOverController getGameOverControllerInstance() {
    return gameOverControllerInstance;
  }

  /**
   * Sets the CauldronController instance. Used to set the CauldronController instance.
   * Doesn't return anything. Only sets the CauldronController instance.
   *
   * @param controller the CauldronController instance.
   */
  public static void setCauldronControllerInstance(CauldronController controller) {
    cauldronControllerInstance = controller;
  }

  /**
   * Returns the CauldronController instance. Used to get the CauldronController instance.
   * Doesn't return anything. Returns the CauldronController instance.
   *
   * @return the CauldronController instance.
   */
  public static CauldronController getCauldronControllerInstance() {
    return cauldronControllerInstance;
  }

  /**
   * Sets the BookController instance. Used to set the BookController instance.
   * Doesn't return anything. Only sets the BookController instance.
   *
   * @param controller the BookController instance.
   */
  public static void setBookControllerInstance(BookController controller) {
    bookControllerInstance = controller;
  }

  /**
   * Returns the BookController instance. Only returns the BookController instance.
   * Doesn't return anythingelse.
   *
   * @return the BookController instance.
   */
  public static BookController getBookControllerInstance() {
    return bookControllerInstance;
  }

  /**
   * Sets the LibraryRoomController instance. Used to set the LibraryRoomController instance.
   * Doesn't return anything. 
   *
   * @param controller the LibraryRoomController instance.
   */
  public static void setLibraryRoomControllerInstance(LibraryRoomController controller) {
    libraryRoomControllerInstance = controller;
  }

  /**
   * Returns the LibraryRoomController instance. Used to set the LibraryRoomController instance.
   * Doesn't return anything. Returns the LibraryRoomController instance.
   *
   * @return the LibraryRoomController instance.
   */
  public static LibraryRoomController getLibraryRoomControllerInstance() {
    return libraryRoomControllerInstance;
  }

  /**
   * Sets the TreasureRoomController instance. Used to set the TreasureRoomController instance.
   * Doesn't return anything.
   *
   * @param controller the TreasureRoomController instance.
   */
  public static void setTreasureRoomControllerInstance(TreasureRoomController controller) {
    treasureRoomControllerInstance = controller;
  }

  /**
   * Returns the TreasureRoomController instance. Used to set the TreasureRoomController instance.
   * Doesn't return anything. Returns the TreasureRoomController instance.
   *
   * @return the TreasureRoomController instance.
   */
  public static TreasureRoomController getTreasureRoomControllerInstance() {
    return treasureRoomControllerInstance;
  }

  /**
   * Sets the CauldronRoomController instance. Used to set the CauldronRoomController instance.
   * Doesn't return anything.
   *
   * @param controller the CauldronRoomController instance.
   */
  public static void setCauldronRoomControllerInstance(CauldronRoomController controller) {
    cauldronRoomControllerInstance = controller;
  }

  /**
   * Returns the CauldronRoomController instance. Used to set the CauldronRoomController instance.
   * Doesn't return anything. Returns the CauldronRoomController instance.
   *
   * @return the CauldronRoomController instance.
   */
  public static CauldronRoomController getCauldronRoomControllerInstance() {
    return cauldronRoomControllerInstance;
  }

  /**
   * Sets the ChestController instance. Used to set the ChestController instance.
   * Returns the ChestController instance.
   *
   * @return the ChestController instance.
   */
  public static ChestController getChestControllerInstance() {
    return chestControllerInstance;
  }

  /**
   * Returns the ChestController instance. Used to set the ChestController instance.
   * Doesn't return anything.
   *
   * @param chestControllerInstance the ChestController instance.
   */
  public static void setChestControllerInstance(ChestController chestControllerInstance) {
    SceneManager.chestControllerInstance = chestControllerInstance;
  }
}
