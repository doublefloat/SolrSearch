package com.hypers.common.exception.util;

import com.hypers.common.exception.ResourceNotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

/**
 * @author long
 * @since 17-8-27
 */
public class HttpExceptionUtil {

  public static void assertResouceAvailable(HttpResponse response)
      throws ResourceNotFoundException {
    assert response != null;

    int statusCode = response.getStatusLine().getStatusCode();

    if (statusCode == HttpStatus.SC_NOT_FOUND) {
      throw new ResourceNotFoundException();
    }
  }

}
