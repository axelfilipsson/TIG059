package se.gu.ait.sbserver.storage;

import se.gu.ait.sbserver.domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Collection;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

/**
 * <p>An implementation of ProuctLine which reads products from the database.
 * </p>
 */
public class InsertProductLine implements ProductLine {

  static String XML_FILE = "src/main/resources/sortiment.xml";

  static {
    String file = System.getProperty("sortiment-xml-file");
    if (file != null) {
      XML_FILE = file;
    }
  }
  static final String PRODUCT = "artikel";
  static final String NAME = "Namn";
  static final String NAME2 = "Namn2";
  static final String ALCOHOL = "Alkoholhalt";
  static final String PRICE = "Prisinklmoms";
  static final String VOLUME = "Volymiml";
  static final String DROPPED = "Utgått";
  static final String NR = "nr";
  static final String PRODUCT_GROUP = "Varugrupp";
  static final String TYPE = "Typ";


  private List<Product> xmlproducts;
  private List<Product> products;
  private static final String DB_URL =
    "jdbc:sqlite:src/main/resources/bolaget.db";

  SQLInsertExporter sqlInsert = new SQLInsertExporter();
  ProductGroupExporter pge = new ProductGroupExporter();

  // Prevent instantiation from outside this package
  InsertProductLine() { }

  public List<Product> getProductsFilteredBy(Predicate<Product> predicate) {
    readProductsFromFile();
    readProductsFromDatabase();
    xmlInserter();
    return xmlproducts.stream().filter(predicate).collect(Collectors.toList());
  }

  public List<Product> getAllProducts() {
    readProductsFromFile();
    readProductsFromDatabase();
    xmlInserter();
    return null;
  }

  private void readProductsFromDatabase() {
    System.out.println("Reading products from database.");
    products = new ArrayList<>();
    try {
      ResultSet rs = DBHelper.productsResultSet();
      while (rs.next()) {
        String name;
        double alcohol;
        int volume;
        double price;
        int nr;
        String productGroup;
        String type;
        name = rs.getString(DBHelper.ColumnId.NAME);
        alcohol = rs.getDouble(DBHelper.ColumnId.ALCOHOL);
        volume = rs.getInt(DBHelper.ColumnId.VOLUME);
        price = rs.getDouble(DBHelper.ColumnId.PRICE);
        nr = rs.getInt(DBHelper.ColumnId.PRODUCT_NR);
        type = rs.getString(DBHelper.ColumnId.TYPE);
        productGroup = rs.getString(DBHelper.ColumnId.PRODUCT_GROUP);
        products.add(new Product.Builder()
                     .name(name)
                     .price(price)
                     .alcohol(alcohol)
                     .volume(volume)
                     .nr(nr)
                     .productGroup(productGroup)
                     .type(type)
                     .build());
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  private void readProductsFromFile() {
    xmlproducts = new ArrayList<>();
    try {
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      InputStream in = new FileInputStream(XML_FILE);
      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

      String name = null;
      String alcohol = null;
      String volume = null;
      String price = null;
      String nr = null;
      String productGroup = null;
      String insertDate = "";
      String type = "";
      boolean hadType = false;

      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(NR)) {
            event = eventReader.nextEvent();
            nr = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(NAME)) {
            event = eventReader.nextEvent();
            name = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(NAME2)) {
            event = eventReader.nextEvent();
            // <namn2> is sometimes empty: <namn2/>
            if (event.isCharacters()) {
              name += " " + (event.asCharacters().getData()).trim();
              name = name.trim();
              name = name.replace("\n", "");
            }
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(ALCOHOL)) {
            event = eventReader.nextEvent();
            alcohol = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(VOLUME)) {
            event = eventReader.nextEvent();
            volume = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(PRICE)) {
            event = eventReader.nextEvent();
            price = (event.asCharacters().getData());
            if (price == null) {
              System.err.println(name + " has price null");

            }
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(PRODUCT_GROUP)) {
            event = eventReader.nextEvent();
            productGroup = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(DBHelper.INSERTDATE)) {
            event = eventReader.nextEvent();
            insertDate = (event.asCharacters().getData());
            continue;
          }
        }
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          if (startElement.getName().getLocalPart().equals(TYPE)) {
            hadType = true;
            event = eventReader.nextEvent();
            // <Typ> is sometimes empty: <Typ/>
            // Sometimes it's not here at all :-/
            if (event.isCharacters()) {
              type = (event.asCharacters().getData());
            }
            continue;
          }
        }
        if (event.isEndElement()) {
          EndElement endElement = event.asEndElement();
          if (endElement.getName().getLocalPart().equals(PRODUCT)) {
            xmlproducts.add(new Product.Builder()
                         .name(name)
                         .price(Double.parseDouble(price))
                         .alcohol(Double
                                  .parseDouble
                                  (alcohol.substring(0, alcohol.length()-1)))
                         .volume((int)Double.parseDouble(volume))
                         .nr(Integer.parseInt(nr))
                         .productGroup(productGroup)
                         .insertDate(insertDate)
                         .type(type)
                         .build());
            hadType = false;
            // Some products don't have a nested Type element,
            // so we'd better reset this one, so that it remains
            // empty if the next product doesn't have a Type
            type = "";
            /*
              products.add(new Product(name,
                                     Double.parseDouble(alcohol.substring(0, alcohol.length()-1)),
                                     Double.parseDouble(price),
                                     (int)Double.parseDouble(volume)));
            */
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }

  }

  private void xmlInserter() {
      try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
        xmlproducts.removeAll(products);
        for (Product product : xmlproducts) {
            product.export(sqlInsert);
            stmt.execute(sqlInsert.toSQLReplaceString());
            stmt.execute(sqlInsert.toSQLUpdateString());
            System.out.println("New product added: " + product);
          }
      }catch (SQLException sqle) {
        System.err.println("Unable to insert products from xml " + sqle.getMessage());
      }
  }

  public Connection connect(){
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(DB_URL);
      System.out.println("Connection established");
    } catch (SQLException sqle) {
      System.err.println("Couldn't get connection to " + DB_URL +
                         sqle.getMessage());
    }
    return conn;
    }
}
