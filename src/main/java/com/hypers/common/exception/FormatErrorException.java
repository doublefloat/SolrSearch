package com.hypers.common.exception;

/**
 * @author long
 * @since 17-11-14
 */
public class FormatErrorException extends Exception {

  public FormatErrorException() {
  }

  public FormatErrorException(String msg) {
    super(msg);
  }

  public FormatErrorException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
