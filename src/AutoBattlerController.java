package src;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoBattlerController{
	AutoBattlerModel model;

	public AutoBattlerController(AutoBattlerModel game) {
		model = game;
	}
	
	public void startAttackPhase() {
		model.attackPhase();
	}
	
	public void startShopPhase() {
		model.shopPhase();
	}
	
	public int levelup(Player player) {
		return model.playerLevelUp(player);
	} 
	
	/**
     * call this right before shop phase or after attack phase. Resets all the 
     * champion stats to their original hp and atk
     */
    public void resetChampionStats() {
        model.resetChampStats();
    }
	
	public Champion[] getShop(Player player){
		return model.getShop(player);
	}
	
	/**
	 * adds to characters to the AI's battlefield, AI is always designated
	 * as P2
	 */
	public void AIturn() {
		model.AIturn();
	}
	
	/**
	 * Call this right before we call attack Phase
	 */
	public void giveTraitBonuses() {
		model.giveOutTraitBonuses(model.getP1());
		model.giveOutTraitBonuses(model.getP2());
	}
	
	/**
	 * call whenever
	 * @return
	 */
	public String getActiveTraits() { 
		String res = "";
		HashMap<String, Integer> traits = model.getP1().getTraits();
		for (String bonus: traits.keySet()) {
			res += bonus;
			res += ": ";
			res += traits.get(bonus);
			res += ",\n";
		}
		return res;
	}
	
	public Champion[] rerollShop(Player player){
		return model.rerollShop(player);
	}

	/**
	 * sells the champion, adds the gold back to you
	 * @param player the current player who wants to sell
	 * @param benchOrBattleField 0 means that the champion sold comes from the bench,
	 *  	  1 means champion comes from battlefield
	 * @param index the current index of the champion that we want to sell
	 */
	public void sellChampion(Player player, int benchOrBattleField, int index) {
		model.sellChampion(player, benchOrBattleField, index);
	}
	
	/**
     * Moves given champion to given destination on battlefield.
     * If a swap is desired, simply pass in one champion object to the champion argument
     * and the location of the other one to the destination argument.
     * @param origin		The initial location of the champion to be moved.
     * @param player      	The player whose champion is being moved.
     * @param destination 	The desired final location of the champion.
     * @return            	true if the move was successful.
     */
	public boolean changePosition(int[] origin, int player, int[] destination) {
		return model.moveChampion(origin, player, destination);
	}
	
	/**
	 * buys the character the player wants to buy at the location they chose.
	 * @param player   The player who wishes to buy the champion.
	 * @param location the index where the player clicked on.
	 */
	public void buyCharacter(Player player, int location) {
		model.buyCharacter(player,location);
	}
	
	/**
	 * Uses the item that the player wants on the champion that they selected
	 * @param player the player with the item
	 * @param item the item that they want to use
	 * @param champion the champion that they want to use the item on
	 */
	public void useItem(Player player, Item item, Champion champion) { 
		player.useItem(item, champion);
	}
	
	/**
	 * returns an arrayList of all the items the player has in inventory
	 * @param player the player we're looking at
	 * @return an ArrayList of items
	 */
	public Item[] getItems(Player player){
		return player.getItems();
	}
	
	public Player getP1() {
		return model.getP1();
	}
	
	public Player getP2() {
		return model.getP2();
	}
	
	public boolean isGameOver() {
		if (getP1().getHealth() <= 0 || getP2().getHealth() <= 0) {
			return true;
		}
		return false;
	}
}



