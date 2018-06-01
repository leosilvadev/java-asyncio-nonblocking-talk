package com.github.leosilvadev.nonblockingjava.nonblocking.exceptions;

/**
 * Created by leonardo on 5/31/18.
 */
public class InvalidHTTPDefinitionException extends RuntimeException {

  public InvalidHTTPDefinitionException() {
    super("Request is not a valid HTTP Request");
  }

}
