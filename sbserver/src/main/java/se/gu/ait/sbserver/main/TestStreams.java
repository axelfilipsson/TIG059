package se.gu.ait.sbserver.main;

import se.gu.ait.sbserver.domain.Product;
import se.gu.ait.sbserver.storage.ProductLine;
import se.gu.ait.sbserver.storage.ProductLineFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class which shows how to filter and print Products using
 * Java 8's streams API.
 */
public class TestStreams {
  public static void main(String[] args) {

    ProductLine productLine = ProductLineFactory.getProductLine();
    List<Product> products = productLine.getAllProducts();
    System.out.println("\n====================\nProducts with > 70% alcohol:");

    products.stream()
      .filter(p -> p.alcohol() > 70.0)
      .collect(Collectors.toList())
      .forEach(System.out::println);

  }
}
