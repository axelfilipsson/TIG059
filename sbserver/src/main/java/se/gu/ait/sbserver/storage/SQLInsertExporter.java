package se.gu.ait.sbserver.storage;

import se.gu.ait.sbserver.domain.Product;

/**
 * <p>This class is used when we want to export the state of a Product
 * for use in a different context or format.</p>
 * <p>An SQLInsertExporter object can, produce a String with the SQL
 * for inserting (or replacing) a Product into the product table in our
 * database. </p>
 *
 * <p>Typical usage:
 * <pre>
 *     Product p1 = new Product.Builder()
 *     .name("Renat")
 *     .price(209.00)
 *     .alcohol(37.50)
 *     .volume(700)
 *     .nr(101)
 *     .productGroup("Okryddad sprit")
 *     .build();
 *
 *     SQLInsertExporter sqlExp = new SQLInsertExporter();
 *     p1.export(sqlExp);
 *     System.out.println(sqlExp.toSQLInsertString());
 * </pre>
 * The above will print (on a single line):
 * <pre>
 * REPLACE INTO product(nr, name, price, alcohol, volume, productGroup, type)
 *   VALUES(101, "Renat", 209.000000, 37.500000, 700, "Okryddad sprit", "");
 * </pre>
 * </p>
 */
public class SQLInsertExporter implements Product.Exporter {
  private String name;
  private double price;
  private double alcohol;
  private int volume;
  private int nr;
  private String productGroup;
  private String insertDate;
  private String changeDate;
  private String type;

  @Override
  public void addName(String name) {
    this.name = name;
  }

  @Override
  public void addPrice(double price) {
    this.price = price;
  }

  @Override
  public void addAlcohol(double alcohol) {
    this.alcohol = alcohol;
  }

  @Override
  public void addVolume(int volume) {
    this.volume = volume;
  }

  @Override
  public void addNr(int nr) {
    this.nr = nr;
  }

  @Override
  public void addProductGroup(String productGroup) {
    this.productGroup = productGroup;
  }

  @Override
  public void addInsertDate(String insertDate) {
    this.insertDate = insertDate;
  }

  @Override
  public void addType(String type) {
    this.type = type;
  }

  /**
   * Returns the exported Product as a plain String
   * for debuggin purposes
   * @return The exported Product as a plain String
   */
  public String toString() {
    return new StringBuilder(name)
      .append(" ")
      .append(price)
      .append(" ")
      .append(alcohol)
      .append(" ")
      .append(volume)
      .append(" ")
      .append(nr)
      .append(" ")
      .append(productGroup)
      .append(" ")
      .append(insertDate)
      .append(" ")
      .append(type)
      .toString();
  }
  private String escape(String string) {
    if (string == null) {
      return "";
    }
    return string
      .replace("\"", "\"\"");
  }

  /**
   * Returns a String with an SQL REPLACE statement for the exported Product
   * with all double quotes escaped by doubling them (the way SQLite3 treats
   * a string with double quotes).
   * @return A String with an SQL REPLACE statement for the exported Product
   */
  public String toSQLReplaceString() {
    return String
      .format("REPLACE INTO product" +
              "(nr, name, price, alcohol, volume, productGroupId, insertDate, type) " +
              "VALUES(%d, \"%s\", %f, %f, %d, \"%d\", \"%s\", \"%s\");",
              nr, escape(name), price, alcohol,
              volume, ProductGroups.idFromProductGroup(productGroup), escape(insertDate), escape(type));
  }

  public String toSQLInsertString() {
    return String
      .format("REPLACE INTO priceChanges (nr, price, changeDate) VALUES(%d, %f, date('now'));", nr, price, changeDate);
  }

  public String toSQLUpdateString() {
    return String
      .format("UPDATE product SET insertDate = date('now') WHERE insertDate = '';");
  }

  public String toSQLCompareDateString (){
      return String
      .format("select * from product where insertDate > (SELECT date('now','-4 month'));"
      ,nr,escape(name),price,alcohol,volume
      ,ProductGroups.idFromProductGroup(productGroup),escape(type));
  }

}
