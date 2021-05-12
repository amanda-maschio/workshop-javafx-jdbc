package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {
	
	private DepartmentService service;
	
	//Referências aos elementos contidos no FXML
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Referência para o Stage atual
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		//Fazer com que a tableViewDepartment acompanhe o tamanho da janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	/**
	 * Resposável por acessar o serviço, carregar os departments e jogar na obsList
	 */
	public void updateTableView() {
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		
		//Instanciar obsList com os dados vindos da list
		obsList = FXCollections.observableArrayList(list);
		
		//Carregar os itens na TableView e mostrar na tela
		tableViewDepartment.setItems(obsList);
		
		//Chama o método que acrescenta um botão de edição para cada registro de departamento
		initEditButtons();
		
		//Chama o método que acrescenta um botão de exclusão para cada registro de departamento
		initRemoveButtons();
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//Pegar referência para o controlador DepartmentFormController
			DepartmentFormController controller = loader.getController();
			
			//Injetar o Department obj no controller do DepartmentFormController
			controller.setDepartment(obj);
			
			//Injetar o DepartmentService
			controller.setDepartmentService(new DepartmentService());
			
			controller.subscribeDataChangeListener(this);
			
			//Carrega os dados do obj no formulario
			controller.updateFormData();
						
			//Para janelas modal devemos instanciar um novo Stage (um palco em cima do outro)
			Stage dialogStage = new Stage();
			
			//Configurando o novo Stage
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	/**
	 * Método responsável por criar um botão do lado de cada registro de departamento, que possibilita alterar os dados do mesmo
	 */
	private void initEditButtons() {
		
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	/**
	 * Método responsável por criar um botão do lado de cada registro de departamento, que possibilita excluir o mesmo
	 */
	private void initRemoveButtons() {
		
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	/**
	 * Método responsável por remover uma entidade
	 * @param obj
	 */
	private void removeEntity(Department obj) {
	
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				service.remove(obj);
				updateTableView();
			}
			catch(DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
		

	}
}
