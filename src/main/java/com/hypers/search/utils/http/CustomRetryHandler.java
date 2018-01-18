package com.hypers.search.utils.http;

import java.io.IOException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

/**
 * @author Ivan
 *
 */
public class CustomRetryHandler {

  public static HttpRequestRetryHandler getRetryHandler() {

    return new HttpRequestRetryHandler() {

      @Override
      public boolean retryRequest(IOException exception, int executionNum, HttpContext context) {
        if (executionNum >= 3) {
          return false;
        }
        return false;
      }
    };

  }

}
