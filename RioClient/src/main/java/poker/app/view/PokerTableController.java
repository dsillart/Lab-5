package poker.app.view;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javax.swing.text.html.HTMLDocument.Iterator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import poker.app.MainApp;
import pokerBase.Action;
import pokerBase.GamePlay;
import pokerBase.Player;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.ePlayerPosition;

public class PokerTableController implements Initializable {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

	@FXML
	private static ImageView imgViewDealerButtonPos1;
	@FXML
	private static ImageView imgViewDealerButtonPos2;
	@FXML
	private static ImageView imgViewDealerButtonPos3;
	@FXML
	private static ImageView imgViewDealerButtonPos4;

	@FXML
	private BorderPane OuterBorderPane;

	@FXML
	private Label lblNumberOfPlayers;
	@FXML
	private static TextArea txtPlayerArea;

	@FXML
	private Button btnStartGame;
	@FXML
	private ToggleButton btnPos1SitLeave;
	@FXML
	private ToggleButton btnPos2SitLeave;
	@FXML
	private ToggleButton btnPos3SitLeave;
	@FXML
	private ToggleButton btnPos4SitLeave;

	@FXML
	private Label lblPos1Name;
	@FXML
	private Label lblPos2Name;
	@FXML
	private Label lblPos3Name;
	@FXML
	private Label lblPos4Name;

	@FXML
	private HBox hBoxDeck;

	@FXML
	private HBox hboxP1Cards;
	@FXML
	private HBox hboxP2Cards;
	@FXML
	private HBox hboxP3Cards;
	@FXML
	private HBox hboxP4Cards;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imgViewDealerButtonPos3.setVisible(true);
		imgViewDealerButtonPos4.setVisible(true);		
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handlePlay() {
	}

	@FXML
	public void GetGameState() {
		Action act = new Action(eAction.GameState, mainApp.getPlayer());
		mainApp.messageSend(act);
	}

	@SuppressWarnings("rawtypes")
	public void setNbrOfPlayers(Table table) {
		Iterator iterate = (Iterator)table.getHashPlayers().entrySet().iterator();
		txtPlayerArea.setText("Table ID: " + table.getTableID().toString() + '\n');
		
		while (((java.util.Iterator) iterate).hasNext()) {
			Player pl = (Player) ((Entry) table).getValue();
			txtPlayerArea.appendText("Player: " + pl.getPlayerName() + " Position: " + pl.getiPlayerPosition() + " ID: " + pl.getiPokerClientID() + '\n');
		}
		
		return;
	}
	
	public void btnSitLeave_Click(ActionEvent event) {
		ToggleButton btnSitLeave = (ToggleButton) event.getSource();
		int iPlayerPosition = 0;
		if (btnSitLeave.isSelected()) {
			switch (btnSitLeave.getId().toString()) {
			case "btnPos1SitLeave":
				iPlayerPosition = ePlayerPosition.ONE.getiPlayerPosition();
				break;
			case "btnPos2SitLeave":
				iPlayerPosition = ePlayerPosition.TWO.getiPlayerPosition();
				break;
			case "btnPos3SitLeave":
				iPlayerPosition = ePlayerPosition.THREE.getiPlayerPosition();
				break;
			case "btnPos4SitLeave":
				iPlayerPosition = ePlayerPosition.FOUR.getiPlayerPosition();
				break;
			}
		} else {
			iPlayerPosition = 0;
		}

		//	Set the PlayerPosition in the Player
		mainApp.getPlayer().setiPlayerPosition(iPlayerPosition);

		//	Build an Action message
		Action act = new Action(btnSitLeave.isSelected() ? eAction.Sit : eAction.Leave, mainApp.getPlayer());

		// Send the Action to the Hub
		mainApp.messageSend(act);
	}

	public void MessageFromMainApp(String strMessage) {
		System.out.println("Message received by PokerTableController: " + strMessage);
	}

	@SuppressWarnings("rawtypes")
	public void Handle_TableState(Table HubPokerTable) {

		// If this message is called, that means there was a change to
		// the state of the Table (player probably ran 'sit' or 'leave')
		// The Table was updated, you just have to refresh the UI
		// controls to show the current state of the Table object
		
		lblPos1Name.setText("");
		btnPos1SitLeave.setVisible(true);
		btnPos1SitLeave.setText(btnPos1SitLeave.isSelected() ? "Leave" : "Sit");
		
		lblPos2Name.setText("");
		btnPos2SitLeave.setVisible(true);
		btnPos2SitLeave.setText(btnPos2SitLeave.isSelected() ? "Leave" : "Sit");
		
		lblPos3Name.setText("");
		btnPos3SitLeave.setVisible(true);
		btnPos3SitLeave.setText(btnPos3SitLeave.isSelected() ? "Leave" : "Sit");
		
		lblPos4Name.setText("");
		btnPos4SitLeave.setVisible(true);
		btnPos4SitLeave.setText(btnPos4SitLeave.isSelected() ? "Leave" : "Sit");
		
		btnStartGame.setDisable(HubPokerTable.getHashPlayers().size() > 0 ? false : true);
		FadeButton(btnStartGame);

		Iterator iterate = (Iterator) HubPokerTable.getHashPlayers().entrySet().iterator();
		
		while (((java.util.Iterator) iterate).hasNext()) {
			
			Player pl = (Player) ((Entry) HubPokerTable).getValue();
			
			switch (pl.getiPlayerPosition()) {

				case 1:
					if (pl.getPlayerID().equals(mainApp.getPlayer().getPlayerID())) {
						btnPos1SitLeave.setVisible(true);
						btnPos2SitLeave.setVisible(false);
						btnPos3SitLeave.setVisible(false);
						btnPos4SitLeave.setVisible(false);
					} 
					else {
						btnPos1SitLeave.setVisible(false);
					}
						lblPos1Name.setText(pl.getPlayerName().toString());
						break;

				case 2:
					if (pl.getPlayerID().equals(mainApp.getPlayer().getPlayerID())) {
						btnPos1SitLeave.setVisible(false);
						btnPos2SitLeave.setVisible(true);
						btnPos3SitLeave.setVisible(false);
						btnPos4SitLeave.setVisible(false);
					} 
					else {
						btnPos2SitLeave.setVisible(false);
					}
						lblPos2Name.setText(pl.getPlayerName().toString());
						break;

				case 3:
					if (pl.getPlayerID().equals(mainApp.getPlayer().getPlayerID())) {
						btnPos1SitLeave.setVisible(false);
						btnPos2SitLeave.setVisible(false);
						btnPos3SitLeave.setVisible(true);
						btnPos4SitLeave.setVisible(false);
					} 
					else {
						btnPos3SitLeave.setVisible(false);
					}
						lblPos3Name.setText(pl.getPlayerName().toString());
						break;

				case 4:
					if (pl.getPlayerID().equals(mainApp.getPlayer().getPlayerID())) {
						btnPos1SitLeave.setVisible(false);
						btnPos2SitLeave.setVisible(false);
						btnPos3SitLeave.setVisible(false);
						btnPos4SitLeave.setVisible(true);
					} 
					else {
						btnPos4SitLeave.setVisible(false);
					}
						lblPos4Name.setText(pl.getPlayerName().toString());
						break;
				}
			}
		}

	
	// A GameState method wasn't originally listed but the messageRecieved method in
	// the MainApp made us think there should be to handle "GamePlay - level action",
	// which we were unsure of what this entailed; all we found aside from the mid-round
	// functions like betting, checking, and folding was moving the dealer chip around.
	
	public void Handle_GameState(GamePlay HubGamePlay) {
		HubGamePlay.getGamePlayers().get(HubGamePlay.getGameDealer());
	}
	
	@FXML
	void btnStart_Click(ActionEvent event) {
		// Start the Game
		Action StartGame = new Action(eAction.StartGame, mainApp.getPlayer());		
		mainApp.messageSend(StartGame);
			
	}

	@FXML
	void btnDeal_Click(ActionEvent event) {
		// Example - how to set card images in the HBOX
		hboxP1Cards.getChildren().clear();

		ImageView i1 = new ImageView(new Image(getClass().getResourceAsStream("/img/26.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i1);

		ImageView i2 = new ImageView(new Image(getClass().getResourceAsStream("/img/27.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i2);

		ImageView i3 = new ImageView(new Image(getClass().getResourceAsStream("/img/28.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i3);

		ImageView i4 = new ImageView(new Image(getClass().getResourceAsStream("/img/29.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i4);

		ImageView i5 = new ImageView(new Image(getClass().getResourceAsStream("/img/30.png"), 75, 75, true, true));
		hboxP1Cards.getChildren().add(i5);

	}

	@FXML
	public void btnFold_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		switch (btnFold.getId().toString()) {
		case "btnPlayer1Fold":
			// Fold for Player 1
			break;
		case "btnPlayer2Fold":
			// Fold for Player 2
			break;
		case "btnPlayer3Fold":
			// Fold for Player 3
			break;
		case "btnPlayer4Fold":
			// Fold for Player 4
			break;

		}
	}

	@FXML
	public void btnCheck_Click(ActionEvent event) {
		Button btnCheck = (Button) event.getSource();
		switch (btnCheck.getId().toString()) {
		case "btnPlayer1Check":
			// Check for Player 1
			break;
		case "btnPlayer2Check":
			// Check for Player 2
			break;
		case "btnPlayer3Check":
			// Check for Player 3
			break;
		case "btnPlayer4Check":
			// Check for Player 4
			break;
		}
	}

	private void FadeButton(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), btn);
		ft.setFromValue(1.0);
		ft.setToValue(0.3);
		ft.setCycleCount(4);
		ft.setAutoReverse(true);
		ft.play();
		}
	}