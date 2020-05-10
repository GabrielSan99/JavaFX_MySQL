package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String str) { // método utilizado para converter parse para int, vai ser usado em outras partes do programa
		try {
			return Integer.parseInt(str);
	}
		catch (NumberFormatException e) {
			return null;
		}
}
}
