package src;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AutoBattlerGUIView extends Application implements Observer {

	private AutoBattlerModel model;
	private AutoBattlerController controller;
	private BorderPane gameBoard;
	private VBox topPlayer;
	private VBox bottomPlayer;
	private HBox topChampions;
	private HBox bottomChampions;
	private HBox cardsForSale;
	private HBox bottomBench;
	private HBox topBench;
	private VBox shop;
	private Pair[] moveCards;
	private StackPane topStats;
	private StackPane bottomStats;
	private Label timer;
	private int time;
	private boolean attackPhase;

	public AutoBattlerGUIView() {
		model = new AutoBattlerModel();
		model.addObserver(this);
		controller = new AutoBattlerController(model);
		gameBoard = new BorderPane();
		moveCards = new Pair[2];
		attackPhase = false;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Genshin Auto Battler");
		Image background = new Image("Background.png");
		gameBoard.setBackground(new Background(new BackgroundImage(background, null, null, null, null)));
		startGame();
		createTopChamp();
		createBottomChamp();
		createBottomPlayer();
		createTopPlayer();
		createShop();

		gameBoard.setTop(shop);
		gameBoard.setBottom(bottomPlayer);
		gameBoard.setMargin(bottomPlayer, new Insets(10,10,10,10));
		gameBoard.setMargin(shop, new Insets(10,10,10,10));


		// if in shop phase make another thing

		// if in shop phase make another thing
		Scene scene = new Scene(gameBoard);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				switch(event.getCode()) {
				case ESCAPE:
					InfoBox.disply("Info Menu", "Press Ok To Go Back");
				}
			}
			
		});
		
		stage.setScene(scene);
		stage.show();
	}


	public void startGame() {
		timer = new Label("");
		timer.setTextFill(Color.BLACK);
		Timer timer1 = new Timer();
		TimerTask task = new TimerTask() {
			int seconds = 30;
			int i = 0;

			@Override
			public void run() {
				i++;
				if (i % seconds == 0) {
					// start attack phase
					
					timer1.cancel();
					attackPhase = true;
					Platform.runLater(() -> attackStart());
					

				} else {
					time = (seconds - (i % seconds));
					Platform.runLater(() -> timer.setText("TIMER: " + time));
				}

			}
		};

		timer1.schedule(task, 0, 1000);

	}

	private void attackStart() {
		gameBoard.setTop(topPlayer);
		gameBoard.setMargin(topPlayer, new Insets(10,10,10,10));
		controller.AIturn();
		controller.giveTraitBonuses();
		controller.startAttackPhase();
		attackPhase = false;
		
		controller.resetChampionStats();
		startGame();
		createShop();
		gameBoard.setTop(shop);
		gameBoard.setMargin(shop, new Insets(10,10,10,10));

	}

	/**
	 * helper method that creates the top players empty card slots and contains an
	 * event handler for clicking when players swap cards. player cards by default
	 * are stackpanes with a transparent rectangle and when a card is placed on top
	 * of card it is just added to stackpane
	 */
	private void createTopChamp() {
		topChampions = createChampSlots();
		topChampions.setOnMouseClicked((event) -> {
			int childIndex = findChild(event.getX());
			if (moveCards[0] == null) {
				Pair pair = new Pair(topChampions, 1, childIndex);
				moveCards[0] = pair;
			} else {
				Pair pair = new Pair(topChampions, 1, childIndex);
				moveCards[1] = pair;
				controller.changePosition(moveCards[0].indices, 1, moveCards[1].indices);
				moveCards = new Pair[2];
			}
		});
	}

	/**
	 * helper method that creates the bottom players empty card slots and contains
	 * an event handler for clicking when players swap cards. player cards by
	 * default are stackpanes with a transparent rectangle and when a card is placed
	 * on top of card it is just added to stackpane
	 */
	private void createBottomChamp() {
		bottomChampions = createChampSlots();
		bottomChampions.setOnMouseClicked((event) -> {
			int childIndex = findChild(event.getX());
			if (moveCards[0] == null) {
				Pair pair = new Pair(bottomChampions, 1, childIndex);
				moveCards[0] = pair;
			} else {
				Pair pair = new Pair(bottomChampions, 1, childIndex);
				moveCards[1] = pair;
				controller.changePosition(moveCards[0].indices, 1, moveCards[1].indices);
				moveCards = new Pair[2];
			}

		});
	}

	/**
	 * helper method that creates champions cardslots.
	 * 
	 * @return tempBox, HBox cardslots for champions
	 */

	// TODO create labels near player side where it displays the current traits on
	// their board
	// change player to flowplane and set player in center and traits on
	// top,bottom,left,right
	private StackPane createPlayerArea(Player p1) {
		// controller get traits
		Label trait = new Label("Buffs");
		trait.setTextFill(Color.SNOW);

		Image player = new Image("baseCard.png");
		ImageView pic = new ImageView();
		pic.setPreserveRatio(true);
		pic.setImage(player);
		pic.setFitHeight(140);
		Rectangle champ2Back = new Rectangle(125, 120, Color.SADDLEBROWN);

		Label hp = new Label("" + p1.getHealth());
		hp.setTextFill(Color.BLACK);

		// get moneys
		Image money = new Image("coin.png");
		ImageView moneyView = new ImageView(money);
		moneyView.setPreserveRatio(true);
		moneyView.setFitHeight(18);

		Label moneyText = new Label(Integer.toString(p1.getGold()));
		moneyText.setTextFill(Color.YELLOW);
		moneyText.setOpacity(100);

		StackPane back2 = new StackPane(champ2Back, pic, trait, hp, moneyView, moneyText);
		back2.setMargin(trait, new Insets(110, 40, 0, 0));
		back2.setMargin(hp, new Insets(55, 25, 0, 0));
		back2.setMargin(moneyView, new Insets(55, 0, 0, 20));
		back2.setMargin(moneyText, new Insets(54, 0, 0, 50));

		return back2;

	}

	/**
	 * helper method that creates the empty champion spots in front of the player
	 * 
	 * @return tempBox, HBox holds empty spaces for the champion slots
	 */
	private HBox createChampSlots() {
		HBox tempBox = new HBox(8);
		for (int col = 0; col < 7; col++) {
			Rectangle placeHolder = new Rectangle(125, 125, Color.TRANSPARENT);
			StackPane backgroundCard = new StackPane(placeHolder);
			backgroundCard.setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, null, null)));
			tempBox.getChildren().add(backgroundCard);
		}
		return tempBox;
	}

	/**
	 * helper method for event that takes where the user clicked and finds the index
	 * of the child node of where the the user clicked
	 * 
	 * @param click, double that is the x-coordinate of where user clicked
	 * @return index, int that is the index of what node was clicked.
	 */
	private int findChild(double click) {
		double start = 0;
		double end = 125;
		for (int index = 0; index < 7; index++) {
			if (click >= start && click <= end) {
				return index;
			}
			start += 125 + 8;
			end += 125 + 8;
		}
		return -1;

	}

	/**
	 * helper method that creates the to player slot and creates the top player
	 * bench. be more descriptive soon and make it look nice
	 */
	private void createTopPlayer() {
		topPlayer = new VBox(10);
		topBench = createBench();
		topBench.setAlignment(Pos.CENTER);
		topBench.setOnMouseClicked((event) -> {
			int childIndex = findChild(event.getX());
			if (moveCards[0] == null) {
				Pair pair = new Pair(topBench, 0, childIndex);
				moveCards[0] = pair;
			} else {
				Pair pair = new Pair(topBench, 0, childIndex);
				moveCards[1] = pair;
				controller.changePosition(moveCards[0].indices, 1, moveCards[1].indices);
				moveCards = new Pair[2];
			}
		});

		topStats = createPlayerArea(controller.getP2());

		topPlayer.getChildren().addAll(topBench, playerAndItems(topStats), topChampions);

	}

	/**
	 * helper method that creates the to player slot and creates the top player
	 * bench. be more descriptive soon and make it look nice
	 */
	private void createBottomPlayer() {
		bottomPlayer = new VBox(10);
		bottomBench = createBench();
		bottomBench.setAlignment(Pos.CENTER);
		bottomBench.setOnMouseClicked((event) -> {
			int childIndex = findChild(event.getX());
			if (moveCards[0] == null) {
				Pair pair = new Pair(bottomBench, 0, childIndex);
				moveCards[0] = pair;
			} else {
				Pair pair = new Pair(bottomBench, 0, childIndex);
				moveCards[1] = pair;
				controller.changePosition(moveCards[0].indices, 1, moveCards[1].indices);
				moveCards = new Pair[2];
			}
		});

		bottomStats = createPlayerArea(controller.getP1());

		bottomPlayer.getChildren().addAll(bottomChampions, playerAndItems(bottomStats), bottomBench);

	}

	private FlowPane playerAndItems(StackPane stats) {
		FlowPane flow = new FlowPane();
		flow.setHgap(8);
		flow.setPadding(new Insets(7));
		for (int i = 0; i < 7; i++) {
			if (i == 3)
				flow.getChildren().add(stats);
			else {
				Rectangle rec = new Rectangle(120, 110, Color.TRANSPARENT);
				StackPane backgroundSpace = new StackPane(rec);
				backgroundSpace.setBackground(new Background(new BackgroundFill(Color.AQUA, null, null)));
				flow.getChildren().add(backgroundSpace);
			}
		}
		return flow;
	}
	
	private ImageView createItemImage(Item item, StackPane sPane) {
		Image reroll = new Image(item.getName() + ".png");
        ImageView pic = new ImageView();
        pic.setPreserveRatio(true);
        pic.setImage(reroll);
        pic.setFitHeight(70);
        pic.setOnMouseClicked((event) -> {
        	if(moveCards[0] != null) {
        		Player p1 = controller.getP1();
        		
        		int index = moveCards[0].indices[1];
        		Champion champ = p1.getBattleField()[index];
        		sPane.getChildren().remove(1);
        		controller.useItem(p1, item, champ);
        		moveCards = new Pair[2];
        		
        	}
        });
        return pic;
		
	}

	/**
	 * helper method that creates an Hbox of how the bench will look like
	 * 
	 * @return tempBox, Hbox that will be used as a bench for one of the players
	 */
	private HBox createBench() {
		HBox tempBox = new HBox();
		for (int col = 0; col < 7; col++) {
			Rectangle placeHolder = new Rectangle(125, 125, Color.TRANSPARENT);
			StackPane backgroundCard = new StackPane(placeHolder);
			backgroundCard.setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, null, null)));

			tempBox.getChildren().add(backgroundCard);
		}
		return tempBox;

	}

	// needs to take champ class card
		private StackPane createCard(Champion champ) {
			Image emptyCard = new Image("baseCard.png");
			ImageView pic = new ImageView();
			pic.setPreserveRatio(true);
			pic.setImage(emptyCard);
			pic.setFitHeight(125);
			String name = champ.getName();
			Image champion = new Image(name + ".png");
			ImageView champPic = new ImageView();
			champPic.setPreserveRatio(true);
			champPic.setImage(champion);
			if (name.toLowerCase().equals("kokomi") || name.toLowerCase().equals("amber")
					|| name.toLowerCase().equals("ningguang") || name.toLowerCase().equals("ayaka")) {
				champPic.setFitHeight(50);
			} else {
				champPic.setFitHeight(75);
			}
			StackPane pane = new StackPane();
			String attackStr = Integer.toString(champ.getAtk());
			String hpStr = Integer.toString(champ.getHp());
			Label hp = new Label(hpStr);
			hp.setTextFill(Color.BLACK);

			Label attack = new Label(attackStr);
			attack.setTextFill(Color.BLACK);

			String star = Integer.toString(champ.getStars());
			Label nameLabel = new Label(name);
			nameLabel.setTextFill(Color.BLACK);

			Image money = new Image("coin.png");
			ImageView moneyView = new ImageView(money);
			moneyView.setPreserveRatio(true);
			moneyView.setFitHeight(18);

			Label moneyText = new Label("" + champ.getStars());
			moneyText.setTextFill(Color.YELLOW);
			moneyText.setOpacity(100);

			Label element = new Label("Element: " + champ.getType());
			element.setTextFill(Color.BLACK);

			pane.setAlignment(Pos.CENTER);
			pane.getChildren().addAll(pic, champPic, hp, attack, nameLabel, moneyView, moneyText, element);
			pane.setMargin(attack, new Insets(50, 0, 0, 40));
			pane.setMargin(hp, new Insets(50, 25, 0, 0));
			pane.setMargin(nameLabel, new Insets(0, 0, 80, 0));
			pane.setMargin(moneyView, new Insets(0, 50, 110, 0));
			pane.setMargin(moneyText, new Insets(0, 25, 110, 0));
			pane.setMargin(element, new Insets(80, 0, 0, 10));
			return pane;
		}

	// how shop is created
	private void createShop() {
		shop = new VBox(8);
		controller.startShopPhase();
		Player player = controller.getP1();
		Champion[] shopArray = controller.getShop(player);

		cardsForSale = createChampSlots();
		for (int index = 0; index < shopArray.length; index++) {
			StackPane card = createCard(shopArray[index]);
			StackPane emptySlot = (StackPane) cardsForSale.getChildren().get(index);
			emptySlot.getChildren().add(card);
		}
		// TODO fix this for sure
		cardsForSale.setOnMouseClicked((event) -> {
			int childIndex = findChild(event.getX());
			Player p1 = controller.getP1();
			controller.buyCharacter(p1, childIndex);
			Pair playerCards = new Pair(bottomBench, 0, findEmptySpot(bottomBench));
			moveCards[1] = playerCards;
			Pair shopCards = new Pair(cardsForSale, 1, childIndex);
			moveCards[0] = shopCards;
			controller.changePosition(moveCards[0].indices, 2, moveCards[1].indices);
			moveCards = new Pair[2];

		});
		HBox shopArea = createShopArea();
		HBox sellArea = createBench();
		sellArea.setAlignment(Pos.CENTER);
		sellArea.setOnMouseClicked((event) -> {
			if (moveCards[0] != null) {
				Player p1 = controller.getP1();
				controller.sellChampion(p1, moveCards[0].indices[0], moveCards[0].indices[1]);
				moveCards = new Pair[2];
			}
		});
		shop.getChildren().addAll(sellArea, shopArea, cardsForSale);


		// get model shop and read array characters

	}

	private int findEmptySpot(HBox area) {
		int index = 0;
		for (Node node : area.getChildren()) {
			StackPane pane = (StackPane) node;
			if (pane.getChildren().size() == 1) {
				return index;
			}
			index++;
		}
		return index;
	}

	private HBox createShopArea() {
		HBox playerArea = new HBox(1);

		// controller get traits
		Image reroll = new Image("rerollBig.png");
		ImageView viewReroll = new ImageView(reroll);
		viewReroll.setPreserveRatio(true);
		viewReroll.setFitHeight(50);
		playerArea.getChildren().add(viewReroll);
		// reroll event
		viewReroll.setOnMouseClicked((event) -> {
			Player p1 = controller.getP1();
			controller.rerollShop(p1);
			update(null,p1);
		});

		Image player = new Image("shopKeeper.jpg");
		ImageView pic = new ImageView();
		pic.setPreserveRatio(true);
		pic.setImage(player);
		pic.setFitHeight(140);
		Rectangle champ2Back = new Rectangle(140, 120, Color.SADDLEBROWN);
		StackPane back2 = new StackPane(champ2Back, pic);
		playerArea.getChildren().add(back2);

		Image upgrade = new Image("upgradeBig.png");
		ImageView viewUpgrade = new ImageView(upgrade);
		viewUpgrade.setPreserveRatio(true);
		viewUpgrade.setFitHeight(50);
		playerArea.getChildren().add(viewUpgrade);
		playerArea.getChildren().add(timer);
		playerArea.setAlignment(Pos.CENTER);
		// upgrade handler
		// TODO leveling up rerolls shop
		viewUpgrade.setOnMouseClicked((event) -> {
			Player p1 = controller.getP1();
			controller.levelup(p1);
			update(null, p1);

		});

		playerArea.setAlignment(Pos.CENTER);
		return playerArea;

	}

	@Override
	public void update(Observable o, Object arg) {
		// should add if arg is player 1
		Player p1 = controller.getP1();
		Champion[] champSlots = p1.getBattleField();
		remakeHbox(bottomChampions, champSlots);

		Champion[] bench = p1.getBench();
		remakeHbox(bottomBench, bench);
		changeStats(1);
		FlowPane fPane = (FlowPane) bottomPlayer.getChildren().get(1);
		updateItems(fPane);
		if (!attackPhase) {
			Champion[] shopCards = controller.getShop(p1);

			remakeHbox(cardsForSale, shopCards);
				
		}else {

			Player p2 = controller.getP2();
			Champion[] champSlots2 = p2.getBattleField();
			remakeHbox(topChampions, champSlots2);
			changeStats(2);

		}
		
		if(controller.isGameOver()) {
			System.out.println("its over");
			System.exit(0);
		}

	}
	
	private void updateItems(FlowPane pane) {
		Player p1 = controller.getP1();
		Item[] itemArray = p1.getItems();
		int itemIndex = 0;
		for(int index = 0; index < pane.getChildren().size() ; index++) {
			if(index == 3) {
				index++;
			}
			if(itemArray[itemIndex] == null) {
				itemIndex++;
				continue;
			}
			StackPane sPane = (StackPane) pane.getChildren().get(index);
			ImageView item = createItemImage(itemArray[itemIndex], sPane);
			sPane.getChildren().add(item);
			itemIndex++;
			}
		}

	private void remakeHbox(HBox cardArea, Champion[] champSlots) {

		for (Node node : cardArea.getChildren()) {
			StackPane pane = (StackPane) node;
			if (pane.getChildren().size() > 1) {
				pane.getChildren().remove(1);
			}
		}
		for (int index = 0; index < champSlots.length; index++) {
			if (champSlots[index] == null) {
				continue;
			} else {
				StackPane card = createCard(champSlots[index]);
				StackPane slot = (StackPane) cardArea.getChildren().get(index);
				slot.getChildren().add(card);
			}
		}
	}

	/**
	 * bottomStats or topStats : [2] = buff label, [3] = hp Label, [5] = money label
	 * champcard stack pane [2] = hp possibly update hp during fight?
	 * 
	 * @param player
	 */
	private void changeStats(int player) {
		if (player == 1) {
			Player p1 = controller.getP1();
			Label money = (Label) bottomStats.getChildren().get(5);
			money.setText("" + p1.getGold());
			
			Label buffs = (Label) bottomStats.getChildren().get(2);

			buffs.setTextFill(Color.SNOW);
			buffs.setText(controller.getActiveTraits());

			buffs.setText("Buffs: " + controller.getActiveTraits());

			Label hp = (Label) bottomStats.getChildren().get(3);
			hp.setText("" + p1.getHealth());
		}else if(player == 2) {
			Player p2 = controller.getP2();
			Label money = (Label) bottomStats.getChildren().get(5);
			money.setText("" + p2.getGold());
			Label buffs = (Label) bottomStats.getChildren().get(2);
			buffs.setText(controller.getActiveTraits());
			Label hp = (Label) bottomStats.getChildren().get(3);
			hp.setText("" + p2.getHealth());
		}

	}

	/**
	 * This class was made for the purpose of helping swap cards so an object will
	 * hold two types one being an HBox and the other is an integer that is the
	 * index to a specific child.
	 *
	 */
	private class Pair {
		public HBox cards;
		public int[] indices;

		/**
		 * constructor method for object Pair
		 * 
		 * @param newCards, an Hbox containing cards
		 * @param num,      an index num to find a specific child node.
		 */
		public Pair(HBox newCards, int isField, int num) {
			cards = newCards;
			indices = new int[] { isField, num };
		}
	}

}