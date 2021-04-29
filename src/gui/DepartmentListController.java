package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	//Referências aos elementos contidos no FXML
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	/**
	 * Método auxiliar para iniciar algum componente da tela
	 */
	private void initializeNodes() {
		
		//Comando responsável por iniciar apropriadamente o comportamento das colunas da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Referência para o Stage atual
		Stage stage = (Stage) Main.getMainScene().getWindow();
		//Fazer com que a tableViewDepartment acompanhe o tamanho da janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
