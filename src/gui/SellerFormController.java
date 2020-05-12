package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity; // entity é privada e tem seu set para chamada
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity) { // set do entity
		this.entity = entity;
	}
	
	public void setSellerSerrvice(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataChageListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
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
			notifyDataChangeListeners();
			Utils.currentStage(event).close(); // quando o botão for apertado ele fecha a janela e salva
			
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors()); // toda a programação dos erros la embaixo estão em função de clicar no botão save
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChange();
		}
		
	}

	private Seller getFormData() { // responsavel por pegar os dados digitados no formulario e instaciar no departamento
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation Error"); // instanciando a excessaõ
		
		obj.setId(Utils.tryParseToInt (txtId.getText())); // o método de conversão presente no utils
		
		if (txtName.getText() == null || txtName.getText().trim().equals ("")) {
			exception.addError("name", "Field can't be empty"); // isso faz armazenar no map do validation exception e imprimir a mensagem de erro
		}
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size()> 0) { // ele vai ver o map e vai imprimir a excessão
			throw exception;
		}
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
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}

	public void updateFormData() { // aqui  que são pego os valores de update para serem impressos
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		else {
			txtId.setText(String.valueOf(entity.getId())); // aqui é onde ocorrem as mudanças quando  utilizado  os textbox
			txtName.setText(entity.getName());
			txtName.setText(entity.getEmail());
			Locale.setDefault(Locale.US);
			txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
			
			if(entity.getBirthDate() != null) {
				dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); // aplicação de fuso horario
			}
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) { // se o erro "name" estiver presente ele vai imprimir a msg de erro na label
			labelErrorName.setText(errors.get("name")); // vai pegar a msg atrelada a "name" no map e escrever na label
		}
	}
}
