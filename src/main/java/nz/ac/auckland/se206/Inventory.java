package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.VBox;

/**
 * Inventory class for the inventory. Used to store items in the inventory.
 * Contains methods for adding and removing items from the inventory.
 */
public class Inventory {
  private Set<Items.Item> inventory;
  private VBox box;

  /**
   * Constructor for the inventory. Ceates a new hashset and a new
   * VBox for the inventory.
   */
  public Inventory() {
    inventory = new HashSet<Items.Item>();
    box = new VBox();
  }

  /** 
   * Adds an item to the inventory given the item. Uses a hashset
   * to ensure that there are no duplicates.
   *
   * @param item The item to add.
   */
  public void add(Items.Item item) {
    inventory.add(item);
  }

  /** 
   * Removes an item from the inventory given the item. Uses a hashset
   * to ensure that there are no duplicates and that the item is in the
   * inventory.
   *
   * @param item The item to remove.
   */
  public void remove(Items.Item item) {
    inventory.remove(item);
  }

  /** 
   * Checks if an item is in the inventory. Only returns true if the item
   * is in the inventory.
   *
   * @param item The item to check.
   * @return True if the item is in the inventory, false otherwise.
   */
  public boolean contains(Items.Item item) {
    return inventory.contains(item);
  }

  /** 
   * Returns the number of items in the inventory. Used to check if the
   * inventory is empty or not.
   *
   * @return The number of items in the inventory.
   */
  public int size() {
    return inventory.size();
  }

  /** 
   * Returns the inventory. Can be used to check what items are in the
   * inventory. Checks if the inventory is empty or not.
   *
   * @return The inventory.
   */
  public Set<Items.Item> getInventory() {
    return inventory;
  }

  /**
   * Sets the inventory. Used to set the inventory to a new inventory.
   * Used when loading a save file. Checks if the inventory is empty or not.
   *
   * @param inventory The inventory to set.
   */
  public void setInventory(Set<Items.Item> inventory) {
    this.inventory = inventory;
  }

  /**
   * Returns the box. Used to get the box for the inventory. Can add the
   * box to the scene for the scrolling bag.
   *
   * @return The box.
   */
  public VBox getBox() {
    return box;
  }

  /**
   * Sets the box. Used to set the box for the inventory. Used when loading
   * a save file. Items are added to the box in the addItem method.
   *
   * @param box The box to set.
   */
  public void setBox(VBox box) {
    this.box = box;
  }
}
