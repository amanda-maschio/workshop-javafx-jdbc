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
	
	//Declara��o dos componentes da tela
	
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
	 * M�todo auxiliar para iniciar ou aplicar alguma configura��o em algum componente da tela
	 */
	private void initializeNodes() {
		
		//Campo de texto txtId s� poder� receber n�meros inteiros
		Constraints.setTextFieldInteger(txtId);
		
		//O m�ximo de caracteres que poder�o ser inseridos no txtName s�o 30
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	/**
	 * M�todo respons�vel por pegar os dados do Department entity e popular no formul�rio
	 */
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity wass null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}
	
}
