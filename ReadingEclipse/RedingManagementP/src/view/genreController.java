package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class genreController {
	@FXML
	ImageView back;
	
	private boolean image = false;
	
	public void next() {
		if(image == false) {
			back.setVisible(true);
			image = true;
		} else {
			back.setVisible(false);
			image = false;
		}
	}
	
	@FXML
	Button closeBtn;
	
	public void close() {
		Stage stage = (Stage) closeBtn.getScene().getWindow();
		stage.close();
		MainController.inforpopData();
	}
}
