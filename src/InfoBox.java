package src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoBox {

	/**
	 * display an alert message to show that the game is over
	 * 
	 * @param title   the title of the stage
	 * @param message given message to display
	 */
	public static void disply(String title, String message) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(250);

		BorderPane window = new BorderPane();
		VBox centerBox = new VBox();
		centerBox.setSpacing(10);

		HBox first = firstLine();
		HBox second = secondLine();
		HBox third = thirdLine();
		centerBox.getChildren().addAll(first, second, third);
		window.setCenter(centerBox);
		
		Label label = new Label(message);
		Button closeButton = new Button("ok");
		closeButton.setOnAction(e -> stage.close());

		VBox box = new VBox();
		box.getChildren().addAll(label, closeButton);
		box.setAlignment(Pos.CENTER);

		window.setBottom(box);

		//Label labelTroll = new Label("haha");
		//window.setBottom(labelTroll);
		
		Scene scene = new Scene(window, 550, 600);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	private static HBox firstLine() {
		HBox box = new HBox();
		box.setPadding(new Insets(15, 12, 15, 12));
	    box.setSpacing(10);
	    box.setStyle("-fx-background-color: #ADD8E6;");
		StackPane card = createPlayerArea();
		
		String cardInfo = "\nThis is your character\nYou have a max health of 30\nGo below 0 and you lose!\nNext to your health is your current gold\nspend gold to purchase levels and characters\nYour current Buffs will be displayed";
		Label label = new Label(cardInfo);
		
		box.getChildren().addAll(card, label);
		
		return box;
		
	}
	
	
	private static HBox secondLine() {
		HBox box = new HBox();
		box.setPadding(new Insets(15, 12, 15, 12));
	    box.setSpacing(10);
	    box.setStyle("-fx-background-color: #5F9EA0;");
		StackPane card = createCard();
		
		String cardInfo = "Each character you buy is a card.\nThe card depicts HP with a heart and\nATK with a sword near the bottom\nThe element type is below the card\nThe cost is above the card";
		Label label = new Label(cardInfo);
		
		box.getChildren().addAll(card, label);
		
		return box;
		
	}
	
	private static HBox thirdLine() {
		HBox box = new HBox();
		box.setPadding(new Insets(15, 12, 15, 12));
	    box.setSpacing(10);
	    box.setStyle("-fx-background-color: #7B68EE;");
	    
	    Image reroll = new Image("rerollBig.png");
		ImageView pic = new ImageView();
		pic.setPreserveRatio(true);
		pic.setImage(reroll);
		pic.setFitHeight(50);
		
		Image upgrade = new Image("upgradeBig.png");
		ImageView picUp = new ImageView();
		picUp.setPreserveRatio(true);
		picUp.setImage(upgrade);
		picUp.setFitHeight(50);
		
		
		String cardInfo = "\nYou can spend gold either to reroll or\nupgrade your shop. reroll offers a new selection\nupgrading increases your chances to find better cards\nExtra Info:\nIf you have multiple of the same\ntype of character they all get bonuses\nShop phase lasts 20 seconds\nso spend your time carefully\nThe attack phase currently only happens in back end";
		Label label = new Label(cardInfo);
		
		box.getChildren().addAll(pic, picUp, label);
		
		return box;
		
	}

	// needs to take champ class card
	private static StackPane createCard() {

		Image emptyCard = new Image("baseCard.png");
		ImageView pic = new ImageView();
		pic.setPreserveRatio(true);
		pic.setImage(emptyCard);
		pic.setFitHeight(125);
		String name = "amber";
		Image champion = new Image(name + ".png");
		ImageView champPic = new ImageView();
		champPic.setPreserveRatio(true);
		champPic.setImage(champion);
		champPic.setFitHeight(75);

		StackPane pane = new StackPane();
		String attackStr = "1";
		String hpStr = "2";
		Label hp = new Label(hpStr);
		hp.setTextFill(Color.BLACK);

		Label attack = new Label(attackStr);
		attack.setTextFill(Color.BLACK);

		String star = "3";
		Label nameLabel = new Label(name);
		nameLabel.setTextFill(Color.BLACK);

		Image money = new Image("coin.png");
		ImageView moneyView = new ImageView(money);
		moneyView.setPreserveRatio(true);
		moneyView.setFitHeight(18);

		Label moneyText = new Label("" + 3);
		moneyText.setTextFill(Color.BLACK);
		moneyText.setOpacity(100);

		Label element = new Label("Element: pyro");
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
	
	private static StackPane createPlayerArea() {

		Label trait = new Label("Buffs: X Y Z");
		trait.setTextFill(Color.SNOW);
		
		Image player = new Image("baseCard.png");
		ImageView pic = new ImageView();
		pic.setPreserveRatio(true);
		pic.setImage(player);
		pic.setFitHeight(140);
		Rectangle champ2Back = new Rectangle(140, 120, Color.SADDLEBROWN);

		Label hp = new Label("13");
		hp.setTextFill(Color.BLACK);

		// get moneys
		Image money = new Image("coin.png");
		ImageView moneyView = new ImageView(money);
		moneyView.setPreserveRatio(true);
		moneyView.setFitHeight(18);

		Label moneyText = new Label(Integer.toString(10));
		moneyText.setTextFill(Color.YELLOW);
		moneyText.setOpacity(100);

		StackPane back2 = new StackPane(champ2Back, pic, trait, hp, moneyView, moneyText);
		back2.setMargin(trait, new Insets(90, 40, 0, 0));
		back2.setMargin(hp, new Insets(55, 25, 0, 0));
		back2.setMargin(moneyView, new Insets(55, 0, 0, 20));
		back2.setMargin(moneyText, new Insets(54, 0, 0, 50));

		return back2;

	}
}
