package se.gu.ait.sbserver.servlet;

public class FormatterFactory {
  public static Formatter getFormatter() {
    // The only format we have so far is Json...
    return new JsonFormatter();
  }
}
