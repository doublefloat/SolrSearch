package com.hypers.common.exception;

/**
 * @author long
 * @since 17-8-27
 */
public class ParameterErrorException extends Exception {

  private static final long serialVersionUID = 1L;

  public ParameterErrorException() {
    super();
  }

  public ParameterErrorException(String message) {
    super(message);
  }

  public ParameterErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParameterErrorException(Throwable cause) {
    super(cause);
  }

}