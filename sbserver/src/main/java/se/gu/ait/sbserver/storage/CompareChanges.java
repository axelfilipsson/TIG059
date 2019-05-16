package se.gu.ait.sbserver.storage;

import se.gu.ait.sbserver.domain.Product;
import se.gu.ait.sbserver.storage.*;


import java.util.*;
import javax.xml.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;


public class CompareChanges {

  private List<Product> products;

  private static final String DB_URL =
    "jdbc:sqlite:src/main/resources/bolaget.db";

  DBHelper getConn = new DBHelper(); // Skapar connection
  SQLInsertExporter sqlInsert = new SQLInsertExporter(); // Insertar SQL-statementet
  SQLBasedProductLine sqlProductLine = new SQLBasedProductLine(); // Läser ut svaret från SQL




  public void sqlStatement() {
  //    Collections.sort(products, Product.ID_ORDER);

  try {

    Connection conn = getConn.connect();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sqlInsert.toSQLCompareDateString());
    while(rs.next()) {
      System.out.println("Product name: " + rs.getString("name") + "\nProduct insertion date:" + rs.getString("insertDate") + "\n");
    }
      }catch (SQLException sqle) {
        System.err.println("Inga produkter" + sqle.getMessage());
      }
  }




}
