package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Items {
    /** Possible items the user can interact with in the room */
    public enum Item {
        ITEM_1,
        ITEM_2,
        ITEM_3,
        ITEM_4,
        ITEM_5,
        ITEM_6,
        ITEM_7,
        ITEM_8,
        ITEM_9,
        ITEM_10
    }

    public Set<Item> available;
    public Set<Item> necessary;
    private Item[] list = new Item[]{
        Item.ITEM_1, Item.ITEM_2, Item.ITEM_3, Item.ITEM_4, 
        Item.ITEM_5, Item.ITEM_6, Item.ITEM_7, Item.ITEM_8, 
        Item.ITEM_9, Item.ITEM_10
    };

    /**
     * Constructor that adds an appropriate number of random unique items to
     * the available and necessary sets. The available set contains all the 
     * items you can interact with within the room. The necessary set contains
     * the items that are needed to complete the room.
     * 
     * @param n is the number of items needed to complete the room. Note that 
     * the number of items available is double those required to complete it.
     * Currently, the maximum value for n is 5, given the number of items.
     */
    public Items(int n) {
        Random random = new Random();
        available = new HashSet<Item>();
        necessary = new HashSet<Item>();
        int i = 0;
        while (i < 2*n) {
            // Method for getting a random item could be improved
            Item item = list[random.nextInt(10)];
            if (!available.contains(item)) {
                available.add(item);
                if (necessary.size() < n) {
                    necessary.add(item);
                }
                i++;
            }
        }
    }
}
