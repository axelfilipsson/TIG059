package se.gu.ait.sbserver.storage;

import java.sql.*;

public class ProductSearch{

    DBHelper getConn = new DBHelper();
  public static ResultSet nrSearch() {
    try {
      PreparedStatement nrSearch = getConn.prepareStatement(
      "SELECT * FROM priceChanges WHERE nr= ? ;");

    } catch(Exception e) {

    }
  }
}
