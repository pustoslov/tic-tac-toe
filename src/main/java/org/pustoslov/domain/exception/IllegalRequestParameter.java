package org.pustoslov.domain.exception;

public class IllegalRequestParameter extends RuntimeException {
  public IllegalRequestParameter(String message) {
    super(message);
  }
}
