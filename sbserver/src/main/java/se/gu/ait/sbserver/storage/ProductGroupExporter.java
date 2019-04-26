package se.gu.ait.sbserver.storage;

/**
 * <p>Allows a Product to export itself to an object of this class, so that
 * we can get an SQL REPLACE statement for the Product's productGroup.</p>
 * <pre>
 * ProductGroupExporter pge = new ProductGroupExporter();
 * product.export(pge);
 * System.out.println(pge.toSQLReplaceString());
 * // Prints:
 * // REPLACE INTO productGroup (id, name) values(5, "Öl");
 * </pre>
 * <p>The product group id is guaranteed to be uniq (within one session).</p>
 */
public class ProductGroupExporter extends AbstractProductExporter {

  /**
   * Returns a String with an SQL REPLACE INTO statement for a product group
   */
  public String toSQLReplaceString() {
    Integer productGroupId =
      ProductGroups.idFromProductGroup(this.productGroup);
    return "REPLACE INTO productGroup (id, name) " +
      "values(" + productGroupId + ", \"" + productGroup + "\");";
  }
}
