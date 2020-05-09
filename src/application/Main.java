package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene; //tornando estatica ela só pode ser usadada com a chamada do  método getMainScene
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true); // esses dois comandos ajustam o tamanho da menu bar na scrollPane para o menu mudar as dimensões junto com a scrollPane
			scrollPane.setFitToWidth(true); 
			
			mainScene = new Scene(scrollPane);
			
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() { // método para retornar a tela principal
		return mainScene;
	}
}