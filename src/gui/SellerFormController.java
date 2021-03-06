package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;	
	
	private SellerService service;
	
	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	//Declaração dos componentes da tela

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
	private ComboBox<Department> comboBoxDepartment;
	
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

	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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
	 * Executar o método onDataChanged em cada um dos listeners
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
	 * Método responsável por pegar os dados inseridos na tela e instanciar em um objeto do tipo Seller
	 * @return
	 */
	private Seller getFormData() {
		
		Seller obj = new Seller();
		
		//Instanciando a exceção personalizada
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		//Testar se o campo Name está vazio
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("Name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		// Testar se o campo Email está vazio
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("Email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());

		//Pegando o valor do Date Picker
		if(dpBirthDate.getValue() == null) {
			exception.addError("BirthDate", "Field can't be empty");
		}else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
			
		}

		// Testar se o campo BaseSalary está vazio
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("BaseSalary", "Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

		//Setando no objeto o Department capturado pelo comboBox
		obj.setDepartment(comboBoxDepartment.getValue());

		//Se o Map de erros tiver algum conteúdo jogamos a exceção
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
	 * Método auxiliar para iniciar ou aplicar alguma configuração em algum componente da tela
	 */
	private void initializeNodes() {
		
		//Campo de texto passado por parâmetro só poderá receber números inteiros
		Constraints.setTextFieldInteger(txtId);
		
		//O máximo de caracteres que poderão ser inseridos no txt passado de parâmetro será o valor passado pelo 2º parâmetro
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		
		//Define que o campo passado por parâmetro é do tipo Double
		Constraints.setTextFieldDouble(txtBaseSalary);
		
		//Define um formato adequado para a data informada no DatePicker. O formato é informado no 2º parâmetro
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}
	
	/**
	 * Método responsável por pegar os dados do Seller entity e popular no formulário
	 */
	public void updateFormData() {
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		
		if(entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}

	}
	
	/**
	 * Método responsável por chamar o DepartmentService e carregar os Departments do banco de dados, preenchendo a obsList com esses Departaments
	 */
	public void loadAssociatedObjects() {
		
		if(departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		
		//Carregar uma lista com os Departments do banco
		List<Department> list = departmentService.findAll();
		
		//Jogar a lista com os Departments para a obsList
		obsList = FXCollections.observableArrayList(list);
		
		//Setar a obsList como associada ao comboBoxDepartment
		comboBoxDepartment.setItems(obsList);
	}
	
	
	/**
	 * Método responsável por capturar os erros armazenados no Map de erros do ValidationException e exibir as mensagens no lbl correspondente
	 * @param error
	 */
	private void setErrorMessages(Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();

		if (fields.contains("Name")) {
			labelErrorName.setText(errors.get("Name"));
		} else {
			labelErrorName.setText("");
		}

		if (fields.contains("Email")) {
			labelErrorEmail.setText(errors.get("Email"));
		} else {
			labelErrorEmail.setText("");
		}

		if (fields.contains("BaseSalary")) {
			labelErrorBaseSalary.setText(errors.get("BaseSalary"));
		} else {
			labelErrorBaseSalary.setText("");
		}

		if (fields.contains("BirthDate")) {
			labelErrorBirthDate.setText(errors.get("BirthDate"));
		} else {
			labelErrorBirthDate.setText("");
		}

	}
	
	/**
	 * Método responsável por inicializar o comboBox
	 */
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
	
}
