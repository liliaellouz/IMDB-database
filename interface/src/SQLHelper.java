import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLHelper {
	public static Connection getConn() throws SQLException{
		String url = "jdbc:oracle:thin:DB2018_G02/DB2018_G02@diassrv2.epfl.ch:1521:orcldias";
		return DriverManager.getConnection(url);
	}
}
