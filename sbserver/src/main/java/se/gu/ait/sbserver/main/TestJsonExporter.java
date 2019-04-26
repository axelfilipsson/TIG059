package se.gu.ait.sbserver.main;

import se.gu.ait.sbserver.domain.*;
import se.gu.ait.sbserver.storage.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestJsonExporter {
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
        JsonExporter jsonExp = new JsonExporter();
        // Example use:
        //p1.export(jsonExp);
        //System.out.println(jsonExp.toJsonString());

        // Load all products from the XML file and export each product
        // so that we can get an SQL INSERT statement from each one of them
        try {
            ProductLine line = ProductLineFactory.getProductLine();
            List<Product> products = line.getAllProducts();

            for (Product product : products) {
                product.export(jsonExp);
                System.out.println(jsonExp.toJsonString());
            }

        } catch (Exception e) {
            // Shoudn't happen...
            System.err.println("Error parsing XML: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
