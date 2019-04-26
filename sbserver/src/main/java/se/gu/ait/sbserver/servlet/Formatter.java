package se.gu.ait.sbserver.servlet;

import java.util.List;
import se.gu.ait.sbserver.domain.Product;

public interface Formatter {
  public String format(List<Product> products);
}
