package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller(); // criação de um novo departamento assim que clicar no botão new na janela
											// departamento
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // esses comandos permitem establecer o
																				// comportamento de tabela realmente
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getMainScene().getWindow(); // fazendo casting para stage para ser usado na linha de
																// baixo
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); // ajusta o tamanho da tabela para a
																				// janela
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null"); // como se fosse uma exceção normal caso a tabaela
																	// esteja vazia
		}

		List<Seller> list = service.findAll(); // referencia dos dados
		obsList = FXCollections.observableArrayList(list); // ambos comando abaixo servem para coletar os itens e depois
															// jogar na tableview
		tableViewSeller.setItems(obsList);
		initEditButtons(); // isso aqui acrescenta os botões edit em cada linha da tabela
		initRemoveButtons(); // idem acima,  so que remove
	}

	private void createDialogForm(Seller obj, String absolutName, Stage parentStage) { // chamada da tela para
//																							// inserir um novo
//																							// departamento, semelhante
//																							// ao que existe no
//																							// mainViewController
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
//			Pane pane = loader.load();
//
//			SellerFormController controller = loader.getController();
//			controller.setSeller(obj);
//			controller.setSellerSerrvice(new SellerService());
//			controller.subscribeDataChageListener(this); // quando for executado ele conta essa tela para executar um
//															// método de fechamento e abertura da tela departmentList
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Enter Seller data");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL); // criar o tipo de janela windows que não permite que vc
//																// acesse a de baixo sem terminar com ela
//			dialogStage.showAndWait();
//
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChange() { // método para atualizar o dado assim que inserido um novo
		updateTableView();
	}

	private void initEditButtons() { // esse código é bem especefico, mas de maneira geral ele serve para pegar
										// botões que estão a frente de cada departamento com a função de tornar eles
										// editaveis. É bem semelhante com o código de inserir um novo departamento, com
										// a diferença de que aqui é instanciadp o objeto ja existente diferente daquele
										// que é instanciado um objeto vazio
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() { // mesmo principio do edit
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void  removeEntity(Seller obj) { // operação para  remover uma entidade
		Optional <ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to remove?"); // responsavel por criar a janela perguntando se tenho certeza, janela de alerta de confirmação
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
			service.remove(obj);
			updateTableView();
			}
	
			catch(DbIntegrityException e) { // se ele não conseguir vai aparecer essa msg de erro, e ela aparece por conta de existir vendedores dentro daquele departamento
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}	
	}
}
