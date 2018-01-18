package com.hypers.search.utils.http;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;

import com.hypers.search.enums.EnumsHttpMethodType;

/**
 * @author Ivan
 *
 */
public class HttpGetRequest extends BaseHttpRequest {

  private boolean secureTag;
  private boolean cookieSet;
  private BasicCookieStore cookieStore;
  private boolean headerSet;
  private Map<String, String> headers = new HashMap<String, String>();

  @Override
  public HttpRequestBase buildRequest(URI uri) {
    return new HttpGet(uri);
  }

  @Override
  public HttpRequestBase buildRequest(URI uri, boolean insecure) {
    secureTag = insecure;
    return new HttpGet(uri);
  }

  @Override
  public boolean insecure() {
    return secureTag;
  }

  @Override
  public EnumsHttpMethodType methodType() {
    return EnumsHttpMethodType.GET;
  }

  @Override
  public List<NameValuePair> parameters() {
    throw new SecurityException(
        "It is not nessary to cut request body and parameter in get request");
  }

  @Override
  public MultipartEntityBuilder multipartEntityBuilder() {
    throw new SecurityException(
        "It is not nessary to cut request body and parameter in get request");
  }

  public HttpGetRequest decoratorCookie(BasicCookieStore cookieStore) {
    this.cookieSet = true;
    this.cookieStore = cookieStore;
    return this;
  }

  public HttpGetRequest decoratorHeader(Map<String, String> headers) {
    this.headerSet = true;
    this.headers = headers;
    return this;
  }

  @Override
  public String jsonParameters() {
    throw new SecurityException(
        "It is not nessary to cut request body and parameter in get request");
  }

  @Override
  public boolean cookieSet() {
    return this.cookieSet;
  }

  @Override
  public BasicCookieStore cookieStore() {
    return this.cookieStore;
  }

  @Override
  public boolean headerSet() {
    return this.headerSet;
  }

  @Override
  public Map<String, String> headers() {
    return this.headers;
  }

}
