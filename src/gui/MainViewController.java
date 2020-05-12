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
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> { // função lâmbida, ela vai usa usar o loadView para carregar a tela Department e depois vamos instanciar métodos dentro da propria chamada de função, conhecida como função lambida
			 controller.setSellerService(new SellerService());
			 controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> { // função lâmbida, ela vai usa usar o loadView para carregar a tela Department e depois vamos instanciar métodos dentro da propria chamada de função, conhecida como função lambida
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
	
	
	private synchronized <T> void loadView(String absolutName, Consumer<T> InitializingAction) { // absolutName pq ele vai receber o caminho absoluto para abrir uma nova janela - o syncronized garante que o processo de criação de tela não seja interrompido, pq na programação grafica varias coisas estão acontecendo ao mesmo tempo - <T> se trata de um tipo generico de função - e o resto da instanciação e pra permitir o uso da função lambda
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent(); // com esse comando de casting e chamada de root temos a chamada da tela principal
			Node mainMenu = mainVBox.getChildren().get(0); // atraves dessas chamadas vamos chamar o menu e os children, caso tenha sido feito algo na janela e o children recebeu algum valor temos esse conjunto de códigos para limpar memória
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren()); // com isso retonamos a janela principal limpinha como se não tivesse sido usada
			
			T controller = loader.getController(); // chamada da função generica que permite ser utilizada por qualquer load view
			InitializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view",e.getMessage(), AlertType.ERROR);
		}
	}
	
}
	
