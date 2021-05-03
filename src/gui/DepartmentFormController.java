package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

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
	

}
