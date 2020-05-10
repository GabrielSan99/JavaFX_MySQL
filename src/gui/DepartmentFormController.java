package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity; // entity é privada e tem seu set para chamada
	
	private DepartmentService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) { // set do entity
		this.entity = entity;
	}
	
	public void setDepartmentSerrvice(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); 
			service.saveOrUpdate(entity); // aqui vai salvar no banco de dados
			Utils.currentStage(event).close(); // quando o botão for apertado ele fecha a janela e salva
			
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() { // responsavel por pegar os dados digitados no formulario e instaciar no departamento
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt (txtId.getText())); // o método de conversão presente no utils
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close(); // quando o botão for apertado ele fecha a janela e salva
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}
	
	private void initializeNodes() { // privações no preenchimento
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		else {
		txtId.setText(String.valueOf(entity.getId())); // aqui é onde ocorrem as mudanças quando  utilizado  os textbox
		txtName.setText(entity.getName());
		}
	}
}
