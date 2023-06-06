package src;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;


class ModelTestCases {

	@Test
	void testIsGameWon() {
		AutoBattlerModel model = new AutoBattlerModel();
		AutoBattlerController controller = new AutoBattlerController(model);
		//assertFalse(controller.isGameOver());
	}
	
	@Test
	void testPlayerClasses() {
		AutoBattlerModel model = new AutoBattlerModel();
		AutoBattlerController controller = new AutoBattlerController(model);
		model.shopPhase();
		Champion[] testBench = new Champion[7];
		assert(Arrays.equals(model.getP1().getBench(), new Champion[7]));
		model.getP1().earnGold(10);
		model.getP1().loseHealth(5);
		assertEquals(model.getP1().getHealth(), 25);
		testBench[0] = (model.getShop(model.getP1())[0]);
		model.getP1().buyCharacter(0);
		assert(Arrays.equals(model.getP1().getBench(), testBench));
	}
	
	@Test
	void testTraits() {
		AutoBattlerModel model = new AutoBattlerModel();
		AutoBattlerController controller = new AutoBattlerController(model);
		model.getP1().earnGold(100);
		model.shopPhase();
		System.out.println(model.getP1().getShop());
		model.getP1().buyCharacter(0);
		model.getP1().buyCharacter(1);
		model.getP1().buyCharacter(2);
		System.out.println(model.getP1().getShop());
		System.out.println(model.getP1().getBench());
		controller.changePosition(new int[] {0, 0}, 1, new int[] {0, 0});
		controller.changePosition(new int[] {0, 0}, 1, new int[] {0, 1});
		controller.changePosition(new int[] {0, 0}, 1, new int[] {0, 2});
		System.out.println(model.getP1().getBattleField()[2]);
		model.getP1().getTraits();
		controller.giveTraitBonuses();
		// This is asking for getType() on null. Will need to adjust test to prevent this.
//		System.out.println(model.getP1().getBattleField()[0].getType());
	}
	
	@Test
	void testController() {
		AutoBattlerModel model = new AutoBattlerModel();
		AutoBattlerController controller = new AutoBattlerController(model);
	}
	
	@Test
	void testChampions() {
		Zhongli Zhongli = new Zhongli();
		assertTrue(Zhongli.getHp() == 13);
		Zhongli.addBonus("Geo");
		assertTrue(Zhongli.getHp() == 16);
		Zhongli.loseHp(10);
		assertEquals(Zhongli.getHp(), 6);
		Zhongli.getAtk();
		Zhongli.decreaseAtk(3);
		assertEquals(Zhongli.getInitialHp(), 13);
		assertEquals(Zhongli.getType(), "Geo");
	}
	
	@Test
	void testShop() {
		AutoBattlerModel model = new AutoBattlerModel();
		model.shopPhase();
		//System.out.println(model.getShop(model.getP1()));
	}

	@Test
	void testItems(){
		Player p = new Player();
		ArrayList<Item> testBag = new ArrayList<Item>();
		assertEquals(p.getItems(), testBag);
		Mona mona = new Mona();
		p.getBattleField()[0] = mona;
		mona.addItem(new Basic_Book());
	}
	
	@Test
	void testAI() {
		AutoBattlerModel model = new AutoBattlerModel();
		AutoBattlerController controller = new AutoBattlerController(model);
		controller.startShopPhase();
		controller.AIturn();
	
	}
}
	
