package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a player object that hasA battleField and bench that contain all the champions
 *      owned by the player.
 * Also tracks health and gold count.
 */
public class Player {
    private final Champion[] battleField;
    private final Champion[] bench;
    private int health;
    private int gold;
    private int level;
    private Shop shop;
    private Item[] items;
    private int itemCount;

    /**
     * Creates player object with default stats and zero champions.
     * Initializes battleField array to only allow 7 champions, located by index.
     */
    public Player() {
        battleField = new Champion[7];
        bench       = new Champion[7];
        health      = 30;
        gold        = 10;
        level 		= 1;
        shop 		= new Shop();
        items 		= new Item[6];
        itemCount  	= 0;
    }

    /**
     * Reduces the players health by given decrement.
     * @param decrement integer amount by which health is decremented.
     */
    public void loseHealth(int decrement) {
        health -= decrement;
    }

    /**
     * Increases the player's health by given increment.
     * @param increment integer amount by which health is incremented.
     */
    public void gainHealth(int increment) {
        health += increment;
    }

    /**
     * Decreases the player's gold by the given amount.
     * @param amount integer amount by which gold is decremented.
     */
    public void spendGold(int amount) {
        gold -= amount;
    }

    /**
     * Increases the player's gold by the given amount.
     * @param amount
     */
    public void earnGold(int amount) {
        gold += amount;
    }
    
    /**
     * returns how much gold the player has
     * @return
     */
    public int getGold() {
    	return gold;
    }

    /**
     * Allows access to player's champions in the bench.
     * @return Arraylist of champions.
     */
    public Champion[] getBench() {
        return bench;
    }
    
    /**
     * Returns all traits on the battlefield regardless of if they give the 
     * trait bonus or not
     * @return a hashmap containing all the traits and how many there are
     */
    public HashMap<String, Integer> getTraits() {
    	HashMap<String, Integer> map = new HashMap<>();
    	ArrayList<String> namesAlreadyAdded = new ArrayList<String>();
    	for (int i = 0; i < 7; i++) {
    		if (battleField[i] == null) {
    			continue;
    			// checks if the map contains the type already and if a champion
    			// with the same name is already in the map

    		} else if(map.containsKey(battleField[i].getType()) &&
    				namesAlreadyAdded.contains(battleField[i].getName())){

    			int alreadyIn = map.get(battleField[i].getType());
        		map.put(battleField[i].getType(), alreadyIn += 1);
        		namesAlreadyAdded.add(battleField[i].getName());
    		} else {
    			map.put(battleField[i].getType(), 1);
    			namesAlreadyAdded.add(battleField[i].getName());
    		}
    	}
    	return map;
    }
    
    /**
     * Returns all the traits that can give bonuses on the board
     * @return a hashmap containing all the traits and how many there are
     */
    public HashMap<String, Integer> getActiveTraits(){
    	HashMap<String, Integer> map = getTraits();
    	HashMap<String, Integer> newMap = new HashMap<>();
    	// Only gives trait bonus if 3 or more different champions
    	// of the same typeon battlefield
    	for (Map.Entry mapElement: map.entrySet()) {
    		int timesIn = (int) mapElement.getValue();
    		if (timesIn == 3) {
    			newMap.put((String) mapElement.getKey(),(int) mapElement.getValue());
    		}
    	}
		return newMap;
    }
    
    /**
     * Allows access to player's champions on the battlefield.
     * @return Array of champions.
     */
    public Champion[] getBattleField() {
        return battleField;
    }
    
    /**
     * returns the players current level
     * @return
     */
    public int getLevel() {
    	return level;
    }
    
    /**
     * returns the players current health
     * @return
     */
    public int getHealth() {
    	return health;
    }
    
    /**
     * levels up the player by one level with gold
     * @param goldRequired  the gold required to upgrade that level
     * @return              the level after the levelUp
     */
    public int levelup(int goldRequired) {
    	if (goldRequired <= gold && level <= 5) {
    		spendGold(goldRequired);
        	level += 1;
    	}
    	return level;
    }
    
    /**
     * Gets the shop that the player is using
     * @return
     */
    public Shop getShop() {
    	return shop;
    }
    
    /**
     * Buys the character denoted by the index in the shop, removes champion from
     * shop if player buys it
     * @param index
     */
    public Champion buyCharacter(int index) {
    	try {
    		Champion toBuy = shop.getShop()[index];
    		int i = 0;
            while (bench[i] != null)
                i++;
        	if (i <= 7 && toBuy != null) {
        		if (gold >= toBuy.getStars()) {
        			bench[i] = toBuy;
        			spendGold(toBuy.getStars());
        			shop.getShop()[index] = null;
        		}
        	}
        	return toBuy;
    	} catch (Exception ArrayIndexOutOfBoundsException){
    		return new Hutao();
    	}
    }

    /**
     * returns the items the player has
     * @return an ArrayList of Items
     */
    public Item[] getItems() {
    	return items;
    }

    /**
     * adds the item to the pool of the players items
     * @param item
     */
    public void addItem(Item item) {
    	if (itemCount >= 6) {
    		return;
    	}
    	items[itemCount] = item;
    	itemCount += 1;
    }

    /**
     * Places the item on the champion, removes that item from the list
     * @param item
     * @param champion
     */
    public void useItem(Item item, Champion champion) {
    	if (item == null || champion == null) {
    		return;
    	}
    	String itemType = item.getType();
    	if (champion.getWeaponType().equals(itemType)) {
    		if (item.getRarity() == 1) {
    			item.setAtk(2);
    		}
    		if (item.getRarity() == 2) {
    			item.setAtk(4);
    		}
    		if (item.getRarity() == 3) {
    			item.setAtk(6);
    		}
        	champion.addItem(item);
        	for (int i = 0; i < 6; i++) {
        		if (items[i].equals(item)) {
        			items[i] = null;
        		}
        	}
        	itemCount -= 1;
    	}
    }
    
    public int getItemCount() {
    	return itemCount;
    }
    
}