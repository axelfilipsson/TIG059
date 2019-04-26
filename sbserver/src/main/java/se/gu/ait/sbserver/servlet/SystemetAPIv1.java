package se.gu.ait.sbserver.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URLDecoder;

public class SystemetAPIv1 extends HttpServlet{

  @Override
  public void init() throws ServletException {
    // Here we can do some initializations
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
    request.setCharacterEncoding(UTF_8.name());
    response.setContentType("text/html;charset="+UTF_8.name());
    PrintWriter out =
      new PrintWriter(new OutputStreamWriter(response.getOutputStream(),
                                             UTF_8), true);
    StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n")
      .append("<html>\n")
      .append("<head>\n")
      .append("  <title>Minimal REST API</title>\n")
      .append("</head> \n")
      .append("<body>\n")
      .append("<p>request.getPathInfo(): ")
      .append(request.getPathInfo())
      .append("</p>")
      .append("<ul>");
    for (Object key : request.getParameterMap().keySet()) {
      sb.append("<li>")
        .append(key)
        .append("=")
        .append(URLDecoder.decode(request.getParameter("" + key), UTF_8.name()))
        .append(" ")
        .append(Arrays.toString(request.getParameterValues("" + key)))
        .append("</li>");
    }
    sb.append("</ul>");
    @SuppressWarnings("unchecked")
    ParameterParser parser = new ParameterParser((Map<String, String[]> )request.getParameterMap());
    parser.parse();
    sb.append("<p>SQL: ")
      .append(sql(parser.validConstraints()))
      .append("</p>");
    sb.append("<p>Constraints: ")
      .append(parser.toString())
      .append("</p>")
      .append("<p>")
      .append("Valid constraints: ")
      .append(parser.validConstraints())
      .append("</p>")
      .append("<p>")
      .append(parser.areValidConstraints() ? "All constraints are valid" : "Invalid constraints" +
              parser.invalidConstraints())
      .append("</body>\n")
      .append("</html>\n");
    parser.parseV1();
    out.println(sb.toString());
    out.close();
  }

  private String sql(List<Constraint> conds) {
    String condition = "";
    for (Constraint con : conds) {
      switch (con.type()) {
        case MAX:
          condition += con.field() + " <= " + con.value();
          break;
        case MIN:
          condition += con.field() + " >= " + con.value();
          break;
        case EQUALS:
          condition += con.field() + " = '" + con.value() + "' ";
          break;
      }
      condition += " AND ";
    }
    if (condition.endsWith(" AND ")) {
      condition = condition.substring(0, condition.length()-5);
    }
    if (! condition.equals("")) {
      condition = " WHERE " + condition;
    }
    return "SELECT * FROM product " + condition + ";";
  }
  
  /** 
   * Interprets the Request object's parameter map
   * (passed to the constructor) and provides a
   * List&lt;Constraint&gt; constraints() method which
   * we can use when getting a list Product filtered
   * by those Constraint objects.
   */
  static class ParameterParser {
    private Map<String, String[]> map;
    private List<Constraint> constraints;
    private static final String[] minMaxFields = {
      "alcohol",
      "price"
    };
    private static final String[] validFields = {
      "alcohol",
      "min_alcohol",
      "min_price",
      "max_alcohol",
      "max_price",
      "price",
      "name",
      "type"
    };
    ParameterParser(Map<String, String[]> map) {
      this.map = map;
      this.constraints = new ArrayList<Constraint>();
    }

    /**
     * Returns the List of valid constraints
     */
    public List<Constraint> validConstraints() {
      List<Constraint> validConstraints = new ArrayList<>(constraints);
      validConstraints.removeIf( c -> c.type() == Constraint.Type.INVALID);
      return validConstraints;
    }
    /**
     * Returns a List&lt;Constraint&gt; with the
     * invalid parameters
     */
    public List<Constraint> invalidConstraints() {
      return constraints
        .stream()
        .filter(c -> c.type() == Constraint.Type.INVALID)
        .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Returns true if all constraints are valid, 
     * false otherwise.
     */
    public boolean areValidConstraints() {
      for (Constraint constraint : constraints) {
        if (constraint.type() == Constraint.Type.INVALID) {
          return false;
        }
      }
      return true;
    }

    private String decode(String value) {
      try {
        return URLDecoder.decode(value, UTF_8.name());
      } catch (Exception e) {
        System.err.println("UrlDecode problem: " + e.getMessage());
        return value;
      }
    }

    private String keyToField(String key) {
      return key.matches("^min_.+") || key.matches("^max_.+") ?
        key.split("_")[1] : key;
    }

    private Constraint.Type keyToConstraintType(String key) {
      if( ! Arrays.asList(validFields).contains(key) ) {
        return Constraint.Type.INVALID;
      }
      return key.matches("^min_.+") ?
        Constraint.Type.MIN :
        key.matches("^max_.+") ? Constraint.Type.MAX :
        Constraint.Type.EQUALS;
    }
    
    @SuppressWarnings("unchecked")
    public void parseV1() {
      String value = null;
      for (String key : (Set<String>)map.keySet()) {      
        if (key.equals("alcohol") ||
            key.equals("min_alcohol") ||
            key.equals("min_price") ||
            key.equals("max_alcohol") ||
            key.equals("max_price") ||
            key.equals("price") ||
            key.equals("name") ||
            key.equals("type")) {
          // it's a valid key, so let's store the
          // key-value pair somehow
          value = map.get(key)[0]; // first element
          System.out.print("Valid parameter: ");
          System.out.println(key + "=" + value);
        } else {
          System.out.print("Invalid parameter: ");
          System.out.println(key + "=" + value);
        }
      }
    }
    
    @SuppressWarnings("unchecked")
    public void parse() {        
      for (String key : (Set<String>)map.keySet()) {
        String value = map.get(key)[0]; // the map is String key, String[] values
        String field = "";
        // Remove the min_ or max_ part from the key
        field = keyToField(key);
        // Is it MAX, MIN or EQUALS?
        Constraint.Type type = keyToConstraintType(key);
          // Store this constraint in the constraints list
        constraints.add(new Constraint(field, type, decode(value)));
      }      
      constraints.sort((c1, c2) -> c1.field().compareTo(c2.field()));
    }
  
    public String toString() {
      return constraints.toString();
    }
  }
}
