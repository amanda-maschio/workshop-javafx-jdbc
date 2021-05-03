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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity;	
	
	private DepartmentService service;
	
	//Declaração dos componentes da tela
	
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
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/**
	 * Método responsável por pegar os dados inseridos na tela e instanciar em um objeto do tipo Department
	 * @return
	 */
	private Department getFormData() {
		
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	/**
	 * Método auxiliar para iniciar ou aplicar alguma configuração em algum componente da tela
	 */
	private void initializeNodes() {
		
		//Campo de texto txtId só poderá receber números inteiros
		Constraints.setTextFieldInteger(txtId);
		
		//O máximo de caracteres que poderão ser inseridos no txtName são 30
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	/**
	 * Método responsável por pegar os dados do Department entity e popular no formulário
	 */
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity wass null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
	
}
