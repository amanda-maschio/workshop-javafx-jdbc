package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	//Refer�ncias aos elementos contidos no FXML
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		//A��o ao clicar na op��o "SELLER" do Menu: redirecionar para a view SellerList (VBox)
		//O segundo par�metro � uma express�o lambda que seta o controller do SellerService e atualiza a tabela com os valores correspondentes
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		//A��o ao clicar na op��o "DEPARTMENT" do Menu: redirecionar para a view DepartmentList (VBox)
		//O segundo par�metro � uma express�o lambda que seta o controller do DepartmentService e atualiza a tabela com os valores correspondentes
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		//A��o ao clicar na op��o "ABOUT" do Menu: redirecionar para a view About (VBox)
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	/**
	 * M�todo respons�vel por carregar uma View passada como par�metro
	 * @param absoluteName
	 */
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			//Pegar uma referencia ao VBox que est� na janela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Guardar referencia para o Menu
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//Executa a fun��o passada de par�metro
			T controller = loader.getController();
			initializingAction.accept(controller);

		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}	
}
