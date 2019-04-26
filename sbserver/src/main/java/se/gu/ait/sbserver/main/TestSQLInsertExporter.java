package se.gu.ait.sbserver.main;

import se.gu.ait.sbserver.domain.*;
import se.gu.ait.sbserver.storage.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSQLInsertExporter {
  public static void main(String[] args) {

    /* Proof of concept - build a product
     * Using a Product.Builder:
     */
    /*
    Product p1 = new Product.Builder()
      .name("Renat")
      .price(209.00)
      .alcohol(37.50)
      .volume(700)
      .nr(101)
      .productGroup("Okryddad sprit")
      .build();
    */

    // An object to export a product to
    // This Exporter can produce SQL Insert statements for us
    SQLInsertExporter sqlExp = new SQLInsertExporter();
    // Example use:
    //p1.export(sqlExp);
    //System.out.println(sqlExp.toSQLInsertString());
    
    // Load all products from the XML file and export each product
    // so that we can get an SQL INSERT statement from each one of them
    try {
      ProductLine line = ProductLineFactory.getProductLine();
      List<Product> products = line.getAllProducts();

      // Create the insert statements for the product groups
      Set<String> productGroupInserts = new HashSet<>();
      ProductGroupExporter pge = new ProductGroupExporter();
      /*
      for (Product product : products) {
        product.export(pge);
        productGroupInserts.add(pge.toSQLReplaceString());
      }
      // Print the insert statements
      for (String insert : productGroupInserts) {
        System.out.println(insert);
      }
      */
      // TODO: Fix sqlExp so that it uses the productGroupId
      // instead of the productGroup string itself
      for (Product product : products) {
        product.export(sqlExp);
        product.export(pge);
        System.out.println(pge.toSQLReplaceString());
        System.out.println(sqlExp.toSQLReplaceString());
      }
      
    } catch (Exception e) {
      // Shoudn't happen...
      System.err.println("Error parsing XML: " + e.getMessage());
      e.printStackTrace();
    }
    
  }
}
