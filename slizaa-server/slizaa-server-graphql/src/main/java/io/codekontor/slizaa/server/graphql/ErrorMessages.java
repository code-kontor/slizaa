package io.codekontor.slizaa.server.graphql;

public class ErrorMessages {

  public static final String ERR_NON_EXISTING_DATABASE = "The specified database (id '%s') does not exist.";
  
  public final static RuntimeException newException(String msg, Object... args) {
    String message = args.length > 0 ? String.format(msg, args) : msg;
    return new RuntimeException(message);    
  }
  
  public final static RuntimeException newException(Exception cause, String msg, Object... args) {
    String message = args.length > 0 ? String.format(msg, args) : msg;
    return new RuntimeException(message, cause);    
  }
}
