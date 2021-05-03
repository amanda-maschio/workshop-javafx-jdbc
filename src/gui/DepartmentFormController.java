package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	private Department entity;	
	
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
		
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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
