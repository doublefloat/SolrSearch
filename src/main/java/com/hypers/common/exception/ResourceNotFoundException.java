package com.hypers.common.exception;

/**
 * @author long
 * @since 17-8-27
 */
public class ResourceNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}