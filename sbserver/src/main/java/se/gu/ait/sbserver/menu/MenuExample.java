package src.main.java.se.gu.ait.sbserver.menu;


/**
 *
 * This class is not a part of the address book API, it only
 * serves as an example on how to use the Menu API, as seen
 * in this class' main method.
 */
public class MenuExample{
  public static void main(String[] args){
    Menu m = new Menu("Meny");

    m.addMenuItem("Sortiment", new MenuAction(){
        public void onItemSelected(){
          System.out.println("Alla produkter");

        }
      });

      m.addMenuItem("Nyheter", new MenuAction(){
          public void onItemSelected(){
            System.out.println("Alla förändringar i sortimentet");
          }
        });

      m.addMenuItem("Produktsök", new MenuAction(){
          public void onItemSelected(){
            System.out.println("Ange produkt id");
          }          });

    m.start();
  }
}
