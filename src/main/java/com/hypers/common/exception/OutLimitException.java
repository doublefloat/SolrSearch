package com.hypers.common.exception;

/**
 * @author long
 * @since 17-11-14
 */
public class OutLimitException extends Exception {

  public OutLimitException() {
  }

  public OutLimitException(String msg) {
    super(msg);
  }

  public OutLimitException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
