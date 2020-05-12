package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> { // fun��o l�mbida, ela vai usa usar o loadView para carregar a tela Department e depois vamos instanciar m�todos dentro da propria chamada de fun��o, conhecida como fun��o lambida
			 controller.setSellerService(new SellerService());
			 controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> { // fun��o l�mbida, ela vai usa usar o loadView para carregar a tela Department e depois vamos instanciar m�todos dentro da propria chamada de fun��o, conhecida como fun��o lambida
			 controller.setDepartmentService(new DepartmentService());
			 controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml" , x -> {}); // vai carregar a tela about
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
	}
	
	
	private synchronized <T> void loadView(String absolutName, Consumer<T> InitializingAction) { // absolutName pq ele vai receber o caminho absoluto para abrir uma nova janela - o syncronized garante que o processo de cria��o de tela n�o seja interrompido, pq na programa��o grafica varias coisas est�o acontecendo ao mesmo tempo - <T> se trata de um tipo generico de fun��o - e o resto da instancia��o e pra permitir o uso da fun��o lambda
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // com esse comando de casting e chamada de root temos a chamada da tela principal
			Node mainMenu = mainVBox.getChildren().get(0); // atraves dessas chamadas vamos chamar o menu e os children, caso tenha sido feito algo na janela e o children recebeu algum valor temos esse conjunto de c�digos para limpar mem�ria
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren()); // com isso retonamos a janela principal limpinha como se n�o tivesse sido usada
			
			T controller = loader.getController(); // chamada da fun��o generica que permite ser utilizada por qualquer load view
			InitializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view",e.getMessage(), AlertType.ERROR);
		}
	}
	
}
	
