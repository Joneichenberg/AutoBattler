package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;


/**
 * Model represents the board on which the game state is changed and updated.
 * Contains two player objects, and allows interaction between them. Thus simulating
 * the state and progression of the game.
 */
public class AutoBattlerModel extends Observable {
    private final Player p1;
    private final Player p2;
    private int round;


    /**
     * constructor for board object.
     * Instantiates player objects.
     */
    public AutoBattlerModel() {
        p1 = new Player();
        p2 = new Player();
        round = 0;
    }

    /**
     * Executes the attackPhase. Each player takes turns attack with their champions
     * from right to left. Defending champions are chosen at random. Starting player
     * is chosen at random.
     * @throws InterruptedException 
     * 
     */
    public void attackPhase()  {
    	round += 1;
        Random rng = new Random();
        int attackRound = rng.nextInt(2);
        boolean roundOver = false;
        while (isRoundOver() == 0) {

            if (attackRound % 2 == 0) { 
                // p1 attacks
                findChamps(rng, p1, p2);
            }
            else {
                // p2 attacks
                findChamps(rng, p2, p1);
            }
            attackRound++;
            setChanged();
        	notifyObservers(null);

        }
        //p1 won the round
        if (isRoundOver() == 1) {
        	p1.earnGold(2*round);
        	p2.earnGold(1*round);
        	p2.loseHealth(round);
        //p2 won the round
        } else if (isRoundOver() == 2) {
        	p1.earnGold(1*round);
        	p2.earnGold(2*round);
        	p1.loseHealth(round);
        }
        resetChampStats();
        setChanged();
    	notifyObservers(null);
       
    }
    
    /**
     * Gives out Trait bonuses on the players battlefield
     * @param player the current player
     */
    public void giveOutTraitBonuses(Player player) {
    	HashMap<String, Integer> traits = player.getActiveTraits();
    	for (String bonus: traits.keySet()) {
    		for (int i = 0; i < 7; i++) {
    			if (player.getBattleField()[i] == null) {
    				continue;
    			// if the champions type is in the traits keyset, give that champion a bonus
    			} else if (player.getBattleField()[i].getType().equals(bonus)) {
    				player.getBattleField()[i].addBonus(bonus);
    			}
    		}
    	}
    	setChanged();
    	notifyObservers(player);
    }
    
    /**
     * Begins the shop phase by setting a new shop for both players
     */
    public void shopPhase() {
		// at the beginning of the shop phase this gives the players 
		// all new shops based on their level
		p1.getShop().rerollShop(p1.getLevel());
		p2.getShop().rerollShop(p2.getLevel());
    }
    
    /**
     * Returns the current shop of the player
     * @param player the player whos shop is chosen
     * @return
     */
    public Champion[] getShop(Player player){
    	return player.getShop().getShop();
    }
   
    /**
     * Re-rolls the current shop into a new shop, costs the player 1 gold, if the 
     * player doesnt have 1 gold, returns the old shop they have, no change.
     * @param player the player who wants to re-roll
     * @return
     */
    public Champion[] rerollShop(Player player){
    	if (player.getGold() > 0) {
    		player.spendGold(1);
    		setChanged();
        	notifyObservers(player);
    		return player.getShop().rerollShop(player.getLevel());
    	}
    	//if the player doesn't have 1 gold, return the current shop
    	setChanged();
    	notifyObservers(player);
		return player.getShop().getShop();
    }
    
    /**
     * Levels up the player using gold. The amount of gold required is 5 times their
     * current level
     * @param player player the player who wants to re-roll
     * @return
     */
    public int playerLevelUp(Player player) {
    	int level = player.getLevel();
    	int goldReq = level * 5;
    	return player.levelup(goldReq);
    }
    /**
     * Finds champions to attack with, and executes one single attack and respective defender's
     * singular defense.
     * Assumes that both player's battlefields are NOT EMPTY.
     * @param rng       A Random object for choosing a defending champion to attack.
     * @param attacking Player that is attacking.
     * @param defending Player that is defending.
     */
    private void findChamps(Random rng, Player attacking, Player defending) { 
        int i = 0;
        int j;
        Champion attacker = null;
        int attackerLocation = -1;
        Champion defender = null;
        int defenderLocation = -1;
        while (attacker == null || attacker.getHp() <= 0) {
            attacker = attacking.getBattleField()[i];
            attackerLocation = i;
            if (i > 5)
                i = 0;
            else
                i++;
        }
        while (defender == null || defender.getHp() <= 0) {
            j = rng.nextInt(7);
            defender = defending.getBattleField()[j];
            defenderLocation = j;
        }
        int result = executeAttack(attacker, defender);
        // TODO pass attacking and defending players and indices for their battlefield to Observer.
        if (result == 0) {
        	defending.earnGold(2);
        	giveItem(defending);
        } else if (result == 1) {
        	attacking.earnGold(2);
        	giveItem(attacking);
        }else {
        	attacking.earnGold(2);
        	defending.earnGold(2);
        	giveItem(defending);
        	giveItem(attacking);
        }
    }
    
    /**
     * resets traits of champions on battlefield
     */
    public void resetChampStats() {
    		Champion[] p1BattleField = p1.getBattleField();
    		Champion[] p2BattleField = p2.getBattleField();
		for (int i = 0; i < 7; i++) {
			if (p1BattleField[i] != null) {
				p1BattleField[i].setHp(p1BattleField[i].getInitialHp());
				p1BattleField[i].setAtk(p1BattleField[i].getInitialAtk());
			}
			if (p2BattleField[i] != null) {
				p2BattleField[i].setHp(p2BattleField[i].getInitialHp());
				p2BattleField[i].setAtk(p2BattleField[i].getInitialAtk());
			}
		}
		setChanged();
    	notifyObservers(null);
    }
    
    /**
     * gives a chance for an item to drop for the player, if the players level is 
     * higher, better items. 7% chance of getting an item when a champion dies
     * 
     * @param player
     * @return the Item that the player is rewarded with
     */
    private void giveItem(Player player) {
    	//idk why but it makes me add all of the items one by one
    	Random rand = new Random();
    	ArrayList<Item> oneStars = new ArrayList<Item>(Arrays.asList(
    			new Dull_Blade(), new Great_Sword(), new Polearm(),
    			new Basic_Book(), new Regular_Bow()));
    	ArrayList<Item> twoStars = new ArrayList<Item>(Arrays.asList(
    			new Festering_Desire(), new Dragon_Pike(), new Rain_Slasher(),
    			new Favonius_Book(), new Moonbow()));
    	ArrayList<Item> threeStars = new ArrayList<Item>(Arrays.asList(
    			new Skyward_Blade(), new Homa(), new Grave_Stone(),
    			new Electro_Book(), new Polarstar()));
    	int chance = rand.nextInt(100); 
    	int weaponSelection = rand.nextInt(5);
    	// 3% chance for 3 star
    	if (chance <= 2 + player.getLevel()) {
    		player.addItem(threeStars.get(weaponSelection));
    	} else if (chance <= 5 + player.getLevel()) {
    		player.addItem(twoStars.get(weaponSelection));
    	} else if (chance <= 7 + player.getLevel()) {
    		player.addItem(oneStars.get(weaponSelection));
    	}
    	setChanged();
    	notifyObservers(player);
    }
    
    /**
     * Executes one attack. Subtracts each champions HP by the attack of the other Champion.
     * @param attacker Champion attacking.
     * @param defender Champion defending.
     * @return returns 0 if the defending champ killed the attacker, 1 if other way around, 
     * and 3 if they both died
     */
    private int executeAttack(Champion attacker, Champion defender) {
        defender.loseHp(attacker.getAtk());
        attacker.loseHp(defender.getAtk());
        // both defending and attacking champ die
        if (defender.getHp() <= 0 && attacker.getHp() <= 0) {
        	return 3;
        } else if (defender.getHp() <= 0) {
        	return 1;
        // if defender killed attacker
        } else {
        	return 0;
        }
        
    }

    /**
     * Checks both player's battlefields. If either only contains champions with hp <= 0, the
     * round is over.
     * @return if neither won return 0, if p1 wins, return 1, if p2 wins return 2,
     * if everythings dead, return 3
     */
    private int isRoundOver() {
        int p1Alive = 0;
        int p2Alive = 0;
        //if p1 has alive champs, stillAlive == 1
        for (int i = 0; i < 7; i++) {
        	if(p1.getBattleField()[i] == null) {
        		continue;
        	}
            if (p1.getBattleField()[i].getHp() > 0)
                p1Alive = 1;
        }
        for (int i = 0; i < 7; i++) {
        	if(p2.getBattleField()[i] == null) {
        		continue;
        	}
            if (p2.getBattleField()[i].getHp() > 0)
                p2Alive = 1;
        } 
        //both alive
        if (p1Alive == 1 && p2Alive == 1) {
        	return 0;
        //p1 alive, p2 dead
        } else if (p1Alive == 1 && p2Alive == 0) {
        	return 1;
        //p2 alive, p1 dead
        } else if (p1Alive == 0 && p2Alive == 1) {
        	return 2;
        //both dead
        } else {
        	return 3;
        }
    }

    /**
     * Moves given champion to given destination on battlefield to the bench.
     * A value of -1 for destination indicates that a champion should be moved to the bench.
     * If a swap is desired, simply pass in one champion object to the champion argument
     * and the location of the other one to the destination argument.
     * @param origin        The original location of the champion to be moved. First element indicates
     *                          whether it is in the bench or battlefield.
     * @param owner         The player whose champion is being moved.
     * @param destination   The desired final location of the champion. First element indicates
     *                          whether it is in the bench or battlefield.
     * @return              true if the move was successful.
     */
    public boolean moveChampion(int[] origin, int owner, int[] destination) {
        Player player;
        if (owner == 1)
            player = p1;
        else
            player = p2;
        if (destination[0] == 0 && origin[0] == 1) 
            return battleToBench(origin[1], player, destination[1]);
        else if (destination[0] == 1 && origin[0] == 0) 
            return benchToBattle(origin[1], player, destination[1]);
         else 
            return champSwap(origin, player, destination[1]);
        
        // TODO send update to observer with foundAt and location indices and player object.
    }

    /**
     * Moves champion from battlefield to bench.
     * @param destination destination index of champion on the battlefield.
     * @param player   Integer representing the player whose champion is being moved.
     * @return         true if a champion exists at the selected origin on the battlefield.
     */
    private boolean battleToBench(int origin, Player player, int destination) {
    	if (origin < 0 || destination >= 7 || destination < 0) {
    		return false;
    	}
        if (player.getBattleField()[origin] == null) {
        	return false;
        }
        Champion temp = player.getBench()[destination];
        player.getBench()[destination] = player.getBattleField()[origin];
        player.getBattleField()[origin] = temp;
        setChanged();
    	notifyObservers(player);
        return true;
    }

    /**
     * Moves champion from bench to the battlefield.
     * If desired location already contains a champion, moves this champion to the bench before
     * moving the next one to the battlefield.
     * @param origin        The original location on the bench of the champion to be moved.
     * @param player        The player wishing to move their champion.
     * @param destination   The location on the battlefield the player wishes to move the champion to.
     * @return              true if the champion exists at the selected origin on the bench.
     */
    private boolean benchToBattle(int origin, Player player, int destination) {
    	if (origin < 0 || destination == -1) {
    		return false;
    	}
        if (player.getBench()[origin] == null)
            return false;
        Champion temp = player.getBattleField()[destination];
        player.getBattleField()[destination] = player.getBench()[origin];
        player.getBench()[origin] = temp;
        setChanged();
    	notifyObservers(player);
        return true;
    }

    /**
     * Swaps champions between locations on the same space (bench or battlefield).
     * @param origin        The location of the champion to be moved.
     * @param player        The player whose champion will be moved.
     * @param destination   The desired destination of the champion to be moved.
     * @return              true if a champion exists in at least one of the two slots.
     */
    private boolean champSwap(int[] origin, Player player, int destination) {
    	if (origin[0] < 0|| origin[1] < 0 || destination < 0 || destination > 7) {
    		return false;
    	}
        if (origin[0] == 0) {
            if (player.getBench()[origin[1]] == null && player.getBench()[destination] == null)
                return false;
            Champion temp = player.getBench()[origin[1]];
            player.getBench()[origin[1]] = player.getBench()[destination];
            player.getBench()[destination] = temp;
        }
        else {
            if (player.getBattleField()[origin[1]] == null && player.getBattleField()[destination] == null)
                return false;
            Champion temp = player.getBattleField()[origin[1]];
            player.getBattleField()[origin[1]] = player.getBattleField()[destination];
            player.getBattleField()[destination] = temp;
        }
        setChanged();
    	notifyObservers(player);
        return true;
    }

    /**
     * Looks for a champion on the battlefield at a certain index.
     * @param location  The index for the champion to look for.
     * @param player    The player whose battlefield we're searching.
     * @return          true if found, false otherwise.
     */
    private boolean isOnBattleField(int location, Player player) {
        return player.getBattleField()[location] != null;
    }
    
	/**
	 * sells the champion at the index, adds the gold back to your bank
	 * @param player the current player who wants to sell
	 * @param benchOrBattleField 0 means that the champion sold comes from the bench,
	 *  	  1 means champion comes from battlefield
	 * @param index the current index of the champion that we want to sell
	 */
    public void sellChampion(Player player, int benchOrBattleField, int index) {
    	if (benchOrBattleField == 0) {
    		Champion toRemove = player.getBench()[index];
    		if (toRemove == null) {
    			return;
    		}
    		player.getBench()[index] = null;
    		player.earnGold(toRemove.getStars());
    	} else if (benchOrBattleField == 1) {
    		Champion toRemove = player.getBattleField()[index];
    		if (toRemove == null) {
    			return;
    		}
    		player.getBattleField()[index] = null;
    		player.earnGold(toRemove.getStars());
    	}
    	setChanged();
    	notifyObservers(player);
    }
    
    public void buyCharacter(Player player,int location) {
    	player.buyCharacter(location);
    	setChanged();
    	notifyObservers(player);
    }
    
    /**
     * makes an AI turn
     */
    public void AIturn() {
    	while (p2.getGold() >= 1) {
    		playerLevelUp(p2);
    		p2.buyCharacter(0);
    		p2.buyCharacter(1);
    		p2.buyCharacter(2);
    		rerollShop(p2);
    	}
    
    	// if AI has chanpions on bench and spaces on the battlefield
    	while (p2.getBattleField()[6] == null) {
    		// gets the index of the leftmost champ on the bench
    		int firstChampLocation = getFirstOnBench(p2);
    		//if no champions on bench, breaks
    		if (firstChampLocation == -1) {
    			break;
    		}
    		int i = 0;
    		// puts champion from bench to battlefield at farthest left position
    		while (p2.getBattleField()[i] != null) {
    			i += 1;
    		}
    		benchToBattle(firstChampLocation, p2, i);
    	}
    	for (int i = 0; i < 6; i++) {
    		p2.useItem(p2.getItems()[0], p2.getBattleField()[0]);
    	}
    	setChanged();
    	notifyObservers(null);
    }
    
    /**
     * returns the index of the first Champion on a players bench, if 
     * no champions on bench return -1
     * @param player
     */
    private int getFirstOnBench(Player player) {
    	for (int i = 0; i < player.getBench().length; i++) {
    		if (player.getBench()[i] != null) {
    			return i;
    		}
    	}
		return -1;
    }
    
    /**
     * returns player 1
     * @return
     */
    public Player getP1() {
    	return p1;
    }
    
    /**
     * returns player 2
     * @return
     */
    public Player getP2() {
    	return p2;
    }
}
