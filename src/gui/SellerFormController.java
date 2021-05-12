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
	
	private Seller entity;	
	
	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	//Declara��o dos componentes da tela

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

	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
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
	 * Executar o m�todo onDataChanged em cada um dos listeners
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
	 * M�todo respons�vel por pegar os dados inseridos na tela e instanciar em um objeto do tipo Seller
	 * @return
	 */
	private Seller getFormData() {
		
		Seller obj = new Seller();
		
		//Instanciando a exce��o personalizada
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		//Testar se o campo Name est� vazio
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("Name", "Field can't be empty");
		}

		obj.setName(txtName.getText());
		
		//Se o Map de erros tiver algum conte�do jogamos a exce��o
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
	 * M�todo auxiliar para iniciar ou aplicar alguma configura��o em algum componente da tela
	 */
	private void initializeNodes() {
		
		//Campo de texto passado por par�metro s� poder� receber n�meros inteiros
		Constraints.setTextFieldInteger(txtId);
		
		//O m�ximo de caracteres que poder�o ser inseridos no txt passado de par�metro ser� o valor passado pelo 2� par�metro
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		
		//Define que o campo passado por par�metro � do tipo Double
		Constraints.setTextFieldDouble(txtBaseSalary);
		
		//Define um formato adequado para a data informada no DatePicker. O formato � informado no 2� par�metro
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	/**
	 * M�todo respons�vel por pegar os dados do Seller entity e popular no formul�rio
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
		
	}
	
	/**
	 * M�todo respons�vel por capturar os erros armazenados no Map de erros do ValidationException e exibir as mensagens no lbl correspondente
	 * @param error
	 */
	private void setErrorMessages(Map<String,String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("Name")) {
			labelErrorName.setText(errors.get("Name"));
		}
	}
	
}
