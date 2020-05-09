package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml"); // vai carregar a tela about
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
	}
	
	
	private synchronized void loadView(String absolutName) { // absolutName pq ele vai receber o caminho absoluto para abrir uma nova janela - o syncronized garante que o processo de criação de tela não seja interrompido, pq na programação grafica varias coisas estão acontecendo ao mesmo tempo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // com esse comando de casting e chamada de root temos a chamada da tela principal
			Node mainMenu = mainVBox.getChildren().get(0); // atraves dessas chamadas vamos chamar o menu e os children, caso tenha sido feito algo na janela e o children recebeu algum valor temos esse conjunto de códigos para limpar memória
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren()); // com isso retonamos a janela principal limpinha como se não tivesse sido usada
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view",e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absolutName) { // absolutName pq ele vai receber o caminho absoluto para abrir uma nova janela - o syncronized garante que o processo de criação de tela não seja interrompido, pq na programação grafica varias coisas estão acontecendo ao mesmo tempo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // com esse comando de casting e chamada de root temos a chamada da tela principal
			Node mainMenu = mainVBox.getChildren().get(0); // atraves dessas chamadas vamos chamar o menu e os children, caso tenha sido feito algo na janela e o children recebeu algum valor temos esse conjunto de códigos para limpar memória
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren()); // com isso retonamos a janela principal limpinha como se não tivesse sido usada
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view",e.getMessage(), AlertType.ERROR);
		}
	}
}
	
