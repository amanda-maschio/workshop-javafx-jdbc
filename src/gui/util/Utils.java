package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	/**
	 * M�todo respons�vel por retornar o Stage atual
	 * @param event
	 * @return
	 */
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();		
	}
	
	/**
	 * M�todo respons�vel por ler uma string e converter para inteiro
	 * Se durante a convers�o o str n�o for um inteiro v�lido, a fun��o retorna null
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
