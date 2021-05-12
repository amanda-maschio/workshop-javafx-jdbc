package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
	
	/**
	 * Método responsável por formatar um Date em um SimpleDateFormat de uma Table
	 * @param <T>
	 * @param tableColumn
	 * @param format
	 */
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
	
		tableColumn.setCellFactory(column -> {
			
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			
			return cell;
		});
	}
	
	/**
	 * Método responsável por formatar um Double de uma Table
	 * @param <T>
	 * @param tableColumn
	 * @param decimalPlaces
	 */
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
}
