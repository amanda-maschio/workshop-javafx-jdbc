package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	/**
	 * Método responsável por retornar o Stage atual
	 * @param event
	 * @return
	 */
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();		
	}
	
	/**
	 * Método responsável por ler uma string e converter para inteiro
	 * Se durante a conversão o str não for um inteiro válido, a função retorna null
	 * @param str
	 * @return
	 */
	public static Integer tryParseToInt(String str) {
	
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
}
