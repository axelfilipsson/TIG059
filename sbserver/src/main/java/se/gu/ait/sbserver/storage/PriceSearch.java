package se.gu.ait.sbserver.storage;

import java.sql.*;
import java.util.*;

public class PriceSearch{

  private static Scanner in = new Scanner(System.in);

  public static void askFor(String prompt){
    String result;
    System.out.print(prompt + ": ");
    if(System.console() == null){
      result = in.nextLine();
    }else{
      result = System.console().readLine();
    }
    try {
      DBHelper getConn = new DBHelper();
      Connection conn = getConn.connect();
      PreparedStatement nrSearch = conn.prepareStatement(
      "SELECT * FROM priceChanges WHERE nr= ? ;");
      nrSearch.setString(1, result);
      ResultSet rs = nrSearch.executeQuery();
      while (rs.next()) {
        System.out.println("Product: " + rs.getInt("nr") + " " + rs.getString("changeDate") + " " + rs.getDouble("price"));
      }
    } catch(Exception e) {
      System.out.println("Något gick fel med att hämta från databasen");
    }
  }
}
