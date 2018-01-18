package com.hypers.search.utils.http;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;

import com.hypers.search.enums.EnumsHttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivan
 *
 */
public class HttpPostRequest extends BaseHttpRequest {

  private final static Logger log = LoggerFactory.getLogger(BaseHttpRequest.class);

  private boolean secureTag;
  private HttpRequestBase httpRequest;
  private EnumsHttpMethodType methodType;
  // used to url encoded
  private List<NameValuePair> parameters;
  private String jsonParameters;
  private MultipartEntityBuilder multipartEntityBuilder;
  private boolean cookieSet;
  private BasicCookieStore cookieStore;
  private boolean headerSet;
  private Map<String, String> headers = new HashMap<String, String>();

  public HttpPostRequest(List<NameValuePair> parameters, EnumsHttpMethodType methodType) {
    this.parameters = parameters;
    this.methodType = methodType;
  }

  public HttpPostRequest(MultipartEntityBuilder multipartEntityBuilder,
      EnumsHttpMethodType methodType) {
    this.multipartEntityBuilder = multipartEntityBuilder;
    this.methodType = methodType;
  }

  public HttpPostRequest(String jsonParameters, EnumsHttpMethodType methodType) {
    this.jsonParameters = jsonParameters;
    this.methodType = methodType;
  }

  public HttpPostRequest() {}

  public HttpPostRequest decoratorCookie(BasicCookieStore cookieStore) {
    this.cookieSet = true;
    this.cookieStore = cookieStore;
    return this;
  }

  public HttpPostRequest decoratorHeader(Map<String, String> headers) {
    this.headerSet = true;
    this.headers = headers;
    return this;
  }

  @Override
  public HttpRequestBase buildRequest(URI uri) {
    return new HttpPost(uri);
  }

  @Override
  public HttpRequestBase buildRequest(URI uri, boolean insecure) {
    secureTag = insecure;
    this.httpRequest = new HttpPost(uri);
    return this.httpRequest;
  }

  @Override
  public boolean insecure() {
    return secureTag;
  }

  public HttpRequestBase setEntity(UrlEncodedFormEntity entity) {
    ((HttpPost) this.httpRequest).setEntity(entity);
    return this.httpRequest;
  }

  @Override
  public EnumsHttpMethodType methodType() {
    return this.methodType;
  }

  @Override
  public List<NameValuePair> parameters() {
    return this.parameters;
  }

  @Override
  public MultipartEntityBuilder multipartEntityBuilder() {
    return this.multipartEntityBuilder;
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

  @Override
  public String jsonParameters() {
    return jsonParameters;
  }

}
