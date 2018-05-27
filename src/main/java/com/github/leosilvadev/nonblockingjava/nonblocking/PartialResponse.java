package com.github.leosilvadev.nonblockingjava.nonblocking;

/**
 * Created by leonardo on 5/27/18.
 */
public class PartialResponse {

  private final StringBuffer content;
  private final boolean complete;

  public PartialResponse(final StringBuffer content, final boolean complete) {
    this.content = content;
    this.complete = complete;
  }

  public PartialResponse() {
    this.content = new StringBuffer();
    this.complete = false;
  }

  public StringBuffer getContent() {
    return content;
  }

  public boolean isComplete() {
    return complete;
  }

  public void appendContent(final String text) {
    this.content.append(text);
  }
  
}
