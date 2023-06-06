package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Shop {
	private Champion[] shop;
	private ArrayList<Champion> ones = new ArrayList<>(Arrays.asList(
			new Sucrose(), new QiQi(), new Lisa(), new Noelle(), new Barbara(), new Amber()));
	private ArrayList<Champion> twos = new ArrayList<>(Arrays.asList(
			new Jean(), new Ayaka(), new Beidou(), new Ningguang(), new Kokomi(), new Xiangling()));
	private ArrayList<Champion> threes = new ArrayList<>(Arrays.asList(
			new Xiao(), new Ganyu(), new Keqing(), new Zhongli(), new Mona(), new Hutao()));

	public Shop() {
		shop = new Champion[3];
	}
	
	/**
	 * gets the current shop relative to the players level, if you want to 
	 * refresh the shop call getShop again and it will generate a new shop
	 * 
	 * @param level the level of the the current player
	 * @return
	 */
	public Champion[] getShop() {
		return shop;
	}
	
	public Champion[] rerollShop(int level) {
		createChoices(level);
		return shop;
	}
	
	/**
	 * Generates the total champion pool relative to the players current level
	 * 
	 * @param level the level of the the current player
	 */
	private void createChoices(int level) {
		// choices is total champion pool without probability for the level
		// we can change the probabilities by adding more of a star character
		//to this list
		ArrayList<Champion> choices = new ArrayList<>();
		if (level == 1) {
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(twos);
		} 
		if (level == 2) {
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(twos);
			choices.addAll(twos);
			choices.addAll(twos);
			choices.addAll(threes);
		}
		if (level == 3) {
			choices.addAll(ones);
			choices.addAll(ones);
			choices.addAll(twos);
			choices.addAll(twos);
			choices.addAll(threes);
		} 
		if (level == 4) {
			choices.addAll(ones);
			choices.addAll(twos);
			choices.addAll(threes);
		}
		if (level >= 5) {
			choices.addAll(ones);
			choices.addAll(twos);
			choices.addAll(twos);
			choices.addAll(threes);
			choices.addAll(threes);
			choices.addAll(threes);
		}
		createShop(choices);
	}
	
	/**
	 * Generates the shop that the player will see on their board in the shop phase.
	 * No duplicates, size of shop is set to 3. Also removes everything from the past shop
	 * 
	 * @param choices the Total champion pool the player can access
	 */
	private void createShop(ArrayList<Champion> choices) {
		Random rand = new Random();
		//removes all previous from previous shop
		Champion[] newShop = new Champion[3];
		for (int i = 0; i < 3; i++) {
			int toAdd = rand.nextInt(choices.size());
			int starCount = choices.get(toAdd).getStars();
			ArrayList<Champion> newToAdd = new ArrayList<Champion>();
			if (starCount == 1) {
				newToAdd = new ArrayList<>(Arrays.asList(
						new Sucrose(), new QiQi(), new Lisa(), new Noelle(), new Barbara(), new Amber()));
			} else if (starCount == 2) {
				newToAdd = new ArrayList<>(Arrays.asList(
						new Jean(), new Ayaka(), new Beidou(), new Ningguang(), new Kokomi(), new Xiangling()));
			} else if (starCount == 3) {
				newToAdd = new ArrayList<>(Arrays.asList(
						new Xiao(), new Ganyu(), new Keqing(), new Zhongli(), new Mona(), new Hutao()));
			}
			for (Champion champ: newToAdd) {
				if (choices.get(toAdd).getName().equals(champ.getName())) {
					newShop[i] = champ;
					break;
				}
			}
		}
		shop = newShop;
	}
	
	public String toString() {
		String res = "";
		for (int i = 0; i < 3; i++) {
			if (shop[i] != null) {
				res += shop[i].getName();
				res += " ";
			}
		}
		return res;
	}
}


















