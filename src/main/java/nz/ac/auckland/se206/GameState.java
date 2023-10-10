package nz.ac.auckland.se206;

import java.util.ArrayList;

/** 
 * Represents the state of the game. 
 */
public class GameState {

  private static ArrayList<Items.Item> recipe = new ArrayList<Items.Item>();

  /** Indicates whether the riddle has been resolved. */
  public static boolean isBookRiddleResolved = false;

  /** Indicates whether the book riddle has been given. */
  public static boolean isBookRiddleGiven = false;

  /** Indicates whether the chest had been opened or not. */
  public static boolean isChestOpen = false;

  public static boolean areItemsCollected = false;

  /**
   * Returns the recipe. Used when needing to get the recipe from the
   * RecipeBook and makiong sure everything in the cauldon is in the 
   * correct order.
   */
  public static ArrayList<Items.Item> getRecipe() {
    return recipe;
  }
}
