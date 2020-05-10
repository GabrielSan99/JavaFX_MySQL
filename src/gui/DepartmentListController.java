package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private  TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private  TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department(); // criação de um novo departamento assim que clicar no botão new na janela departamento
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); // esses comandos permitem establecer o comportamento de tabela realmente
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); // fazendo casting para stage para ser usado na linha de baixo
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); // ajusta o tamanho da tabela para a janela
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null"); // como se fosse uma exceção normal caso a tabaela esteja vazia
		}
		
		List <Department> list = service.findAll(); // referencia dos dados
		obsList = FXCollections.observableArrayList(list); // ambos comando abaixo servem para coletar os itens e depois jogar na tableview
		tableViewDepartment.setItems(obsList);
	}
	private void createDialogForm(Department obj, String absolutName, Stage parentStage) { // chamada da tela para inserir um novo departamento, semelhante ao que existe no mainViewController
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentSerrvice(new DepartmentService());
			controller.updateFormData();
			
			Stage dialogStage = new Stage ();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL); // criar o tipo de janela windows que não permite que vc acesse a de baixo sem terminar com ela
			dialogStage.showAndWait();
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
