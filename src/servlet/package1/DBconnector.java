package servlet.package1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBconnector {
	
	public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//Mysql Connection
        	//Class.forName("oracle.jdbc.driver.OracleDriver");//Oracle Connection
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletTalk.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:8889/a9613246_mydata1", "root", "root");//mysql database
        	//con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hemabh_demo", "hemabh_demo");//oracle database
 
        } catch (SQLException ex) {
            Logger.getLogger(ServletTalk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

}
