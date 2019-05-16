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
  
  public static void askFor(String prompt){
    String result; //Skapar variabeln result, borde vara en arreylist
    System.out.print(prompt + ": "); //skriver ut frågan om
    if(System.console() == null){
      result = in.nextLine();
    }else{
      result = System.console().readLine();
    }

    System.out.println(result);
    //return result; //datan vi vill visa
  }
}
