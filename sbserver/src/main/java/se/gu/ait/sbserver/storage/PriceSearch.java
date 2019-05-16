package se.gu.ait.sbserver.storage;

import java.sql.*;
import java.util.*;

public class PriceSearch{

  private static Scanner in = new Scanner(System.in);

  /* DBHelper getConn = new DBHelper();
  public static ResultSet nrSearch() {
    try {
      PreparedStatement nrSearch = getConn.prepareStatement(
      "SELECT * FROM priceChanges WHERE nr= ? ;");

    } catch(Exception e) {

    }
  }*/
  public static String askFor(String prompt){
    String result;
    System.out.print(prompt + ": ");
    if(System.console() == null){
      result = in.nextLine();
    }else{
      result = System.console().readLine();
    }
    return result;
  }
}
