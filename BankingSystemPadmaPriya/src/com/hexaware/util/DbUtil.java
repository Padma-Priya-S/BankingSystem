package com.hexaware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * this util class is basically to establish database connection.
 */
public class DbUtil {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/hmbank";
  private static final String USER = "root";
  private static final String PASSWORD = "sqlPadma,1234";
  
  /**
   * this method establishes database connection using username,password and the database.

   * @return it returns the reference to the database connection.
   * @throws SQLException used to handle when connection is not established.
   */
  public static Connection getDbConn() throws SQLException {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }

  /**
   * this main method is to ensure whether connection is established.

   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    try {
      System.out.println(getDbConn());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
