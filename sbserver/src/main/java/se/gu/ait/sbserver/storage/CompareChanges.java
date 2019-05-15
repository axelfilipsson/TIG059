package se.gu.ait.sbserver.storage;

import se.gu.ait.sbserver.domain.Product;


public class CompareChanges implements Product.Exporter {
    private String name;
    private double price;
    private double alcohol;
    private int volume;
    private int nr;
    private String productGroup;
    private String insertDate;
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

  public String toSQLCompareDateString (){
      return String
      .format("select * from product where insertDate > '2013-04-12';"
      ,nr,escape(name),price,alcohol,volume
      ,ProductGroups.idFromProductGroup(productGroup),escape(type));
  }

}
