package gui;

import java.io.IOException;	

import java.net.URL;
import java.util.ResourceBundle;

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
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		//A��o ao clicar na op��o "DEPARTMENT" do Menu: redirecionar para a view DepartmentList (VBox)
		loadView2("/gui/DepartmentList.fxml");
	}

	@FXML
	public void onMenuItemAboutAction() {
		//A��o ao clicar na op��o "ABOUT" do Menu: redirecionar para a view About (VBox)
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	/**
	 * M�todo respons�vel por carregar uma View passada como par�metro
	 * @param absoluteName
	 */
	private synchronized void loadView(String absoluteName) {

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
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading View!", e.getMessage(), AlertType.ERROR);
		}
	}

	/**
	 * M�todo respons�vel por carregar uma View passada como par�metro
	 * @param absoluteName
	 */
	private synchronized void loadView2(String absoluteName) {
		
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
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}	
}
