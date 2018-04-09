package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainViewController {
	@FXML
	private Button btn;
	
	public void initialize() {
		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				((Button)event.getSource()).setText("mleko");
			}
		});
	}
	
	@FXML
	private void clickRafal(ActionEvent e) {
		((Button)e.getSource()).setText("XD");
	}
	
}
