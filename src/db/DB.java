package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	//método para conectar com o banco de dados
	public static Connection getConnection() {
		
		if(conn == null) {
			
			try{
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		
		return conn;
	}
	
	//método para fechar a conexão com o banco de dados
	public static void closeConnection() {
		
		if(conn != null) {
			
			try {
			conn.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
				
			}
		}
	}
	
	//método para carregar as propriedades do banco contidas no arquivo db.properties
	private static Properties loadProperties() {
		
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	//método para fechar um Objeto do tipo STATEMENT
	public static void closeStatement(Statement st) {
		
		if(st != null) {
			
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	//método para fechar um Objeto do tipo RESULTSET
		public static void closeResultSet(ResultSet rs) {
			
			if(rs != null) {
				
				try {
					rs.close();
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}
}
