package se.gu.ait.sbserver.menu;

import se.gu.ait.sbserver.domain.Product;
import se.gu.ait.sbserver.storage.*;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * This class is not a part of the address book API, it only
 * serves as an example on how to use the Menu API, as seen
 * in this class' main method.
 */
public class MenuExample{
  public static void main(String[] args){
    Menu m = new Menu("Meny");

    CompareChanges nyheter = new CompareChanges();

    m.addMenuItem("Sortiment", new MenuAction(){
        public void onItemSelected(){
          System.out.println("Alla produkter");
          ProductLine line = ProductLineFactory.getXMLProductLine();
          List<Product> products = line.getAllProducts();
          for (Product p : products) {
            System.out.println(p);
          }

          /*
          for(int i=0;i<products.size();i++){
            System.out.println(products.get(i));
          } */
        }
      });

      m.addMenuItem("Nyheter", new MenuAction(){
          public void onItemSelected(){
            System.out.println("Alla förändringar i sortimentet");
            nyheter.sqlStatement();
          }
        });

      m.addMenuItem("Produktsök", new MenuAction(){
          public void onItemSelected(){
            PriceSearch.askFor("Ange produktid");
          }          });

    m.start();
  }
}
