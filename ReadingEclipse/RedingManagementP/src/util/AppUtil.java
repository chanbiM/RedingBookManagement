package util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class AppUtil {
	public static void inforAlert(String msg, String header){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("알림");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		
		alert.show();
	}
	
	public static void errorAlert(String msg, String header){
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("알림");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		
		alert.show();
	}
	
	public static void conAlert(String msg, String header){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("알림");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		
		alert.show();
	}
	
	public static void warAlert(String msg, String header){
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("알림");
		alert.setHeaderText(header);
		alert.setContentText(msg);
		
		alert.show();
	}
	
	public static boolean DelUserAlert(){
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("알림");
		alert.setHeaderText("삭제하시겠습니까?");
		alert.setContentText("삭제된 계정은 복구되지 않습니다.");
		
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		
		Button yesBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		yesBtn.setDefaultButton(false);
		
		Button noBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		noBtn.setDefaultButton(true);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES){
		    return true;
		} else {
			alert.close();
		    // ... user chose CANCEL or closed the dialog
		}
		return false;
	}

}
