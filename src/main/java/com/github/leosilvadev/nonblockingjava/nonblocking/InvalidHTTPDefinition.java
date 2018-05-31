package com.github.leosilvadev.nonblockingjava.nonblocking;

/**
 * Created by leonardo on 5/31/18.
 */
public class InvalidHTTPDefinition extends RuntimeException {

  public InvalidHTTPDefinition() {
    super("Request is not a valid HTTP Request");
  }

}
