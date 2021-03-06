package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity;	
	
	private DepartmentService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	//Declara??o dos componentes da tela

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

	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener (DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
			
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	
	/**
	 * Executar o m?todo onDataChanged em cada um dos listeners
	 */
	private void notifyDataChangeListeners() {
		
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/**
	 * M?todo respons?vel por pegar os dados inseridos na tela e instanciar em um objeto do tipo Department
	 * @return
	 */
	private Department getFormData() {
		
		Department obj = new Department();
		
		//Instanciando a exce??o personalizada
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		//Testar se o campo Name est? vazio
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("Name", "Field can't be empty");
		}

		obj.setName(txtName.getText());
		
		//Se o Map de erros tiver algum conte?do jogamos a exce??o
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	/**
	 * M?todo auxiliar para iniciar ou aplicar alguma configura??o em algum componente da tela
	 */
	private void initializeNodes() {
		
		//Campo de texto txtId s? poder? receber n?meros inteiros
		Constraints.setTextFieldInteger(txtId);
		
		//O m?ximo de caracteres que poder?o ser inseridos no txtName s?o 30
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	/**
	 * M?todo respons?vel por pegar os dados do Department entity e popular no formul?rio
	 */
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
	
	/**
	 * M?todo respons?vel por capturar os erros armazenados no Map de erros do ValidationException e exibir as mensagens no lbl correspondente
	 * @param error
	 */
	private void setErrorMessages(Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("Name")) {
			labelErrorName.setText(errors.get("Name"));
		}
	}
	
}
