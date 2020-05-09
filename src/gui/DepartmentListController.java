package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
			throw new IllegalStateException("Service was null"); // como se fosse uma exce��o normal caso a tabaela esteja vazia
		}
		
		List <Department> list = service.findAll(); // referencia dos dados
		obsList = FXCollections.observableArrayList(list); // ambos comando abaixo servem para coletar os itens e depois jogar na tableview
		tableViewDepartment.setItems(obsList);
	}

}
