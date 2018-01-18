package com.hypers.search.utils.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.hypers.search.enums.EnumsHttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Ivan
 *
 */
public abstract class BaseHttpRequest {

  private final static Logger log = LoggerFactory.getLogger(BaseHttpRequest.class);

  private static RequestConfig config = null;
  private static CloseableHttpClient httpClient = null;
  private static CloseableHttpClient secureHttpClient = null;

  static {
    config = RequestConfig.custom().setConnectTimeout(10 * 1000).build();
    httpClient = HttpClients.custom().setRetryHandler(CustomRetryHandler.getRetryHandler())
        .setDefaultRequestConfig(config).build();
    SSLConnectionSocketFactory sslsf = null;
    try {
      @SuppressWarnings("deprecation")
      SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {

//        @Override
        public boolean isTrusted(final X509Certificate[] chain, final String authType)
            throws CertificateException {
          return true;
        }
      }).useTLS().build();

      sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
      e.printStackTrace();
    }
    secureHttpClient = HttpClients.custom().setRetryHandler(CustomRetryHandler.getRetryHandler())
        .setDefaultRequestConfig(config).setSSLSocketFactory(sslsf).build();
  }

  public abstract HttpRequestBase buildRequest(URI uri);

  public abstract HttpRequestBase buildRequest(URI uri, boolean insecure);

  public abstract boolean insecure();

  public abstract EnumsHttpMethodType methodType();

  public abstract List<NameValuePair> parameters();

  public abstract String jsonParameters();

  public abstract boolean cookieSet();

  public abstract BasicCookieStore cookieStore();

  public abstract boolean headerSet();

  public abstract Map<String, String> headers();

  public abstract MultipartEntityBuilder multipartEntityBuilder();

  private static CloseableHttpClient initHttpClient() {
    return httpClient;
  }

  private static CloseableHttpClient initSecureHttpClient() {
    return secureHttpClient;
  }

  private HttpResponse retrieveResp(URI uri, boolean insecure)
      throws ClientProtocolException, IOException {

    // build request
    HttpRequestBase request = buildRequest(uri, insecure);

    if (methodType() == EnumsHttpMethodType.POST_ENCODED_DATA) {

      ((HttpPost) request).setEntity(buildFormEntity(parameters()));

    } else if (methodType() == EnumsHttpMethodType.POST_JSON_DATA) {

      ((HttpPost) request).setEntity(new StringEntity(jsonParameters()));

    } else if (methodType() == EnumsHttpMethodType.POST_MULTIPART) {

      ((HttpPost) request).setEntity(buildHttpEntity(multipartEntityBuilder()));

    }

    // add header
    if (headerSet() && headers().size() > 0) {
      for (String key : headers().keySet())
        request.setHeader(key, headers().get(key));
    }

    // use custom retry handler to enable retry function
    CloseableHttpClient httpClient = null;
    if (insecure()) {
      httpClient = initSecureHttpClient();
    } else {
      httpClient = initHttpClient();
    }

    log.info("cookie set: " + cookieSet());
    if (cookieSet()) {
//      log.info("cookie store: " + cookieStore() == null ? "null" : cookieStore());
      log.info("cookie store: " + cookieStore());
      httpClient = HttpUtils.customSecureClient(cookieStore());
    }

    // transmit and get response
    return httpClient.execute(request);

  }

  public String process(URI uri, boolean insecure) throws ClientProtocolException, IOException {
    HttpResponse response = retrieveResp(uri, insecure);
    return new BasicResponseHandler().handleResponse(response);
  }

  public void process(URI uri, boolean insecure, String filePath)
      throws ClientProtocolException, IOException {

    HttpResponse response = retrieveResp(uri, insecure);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      InputStream instream = entity.getContent();
      try {
        BufferedInputStream bis = new BufferedInputStream(instream);
        BufferedOutputStream bos =
            new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        int inByte;
        while ((inByte = bis.read()) != -1) {
          bos.write(inByte);
        }
        bis.close();
        bos.close();
      } catch (IOException ex) {
        throw ex;
      } catch (RuntimeException ex) {
        throw ex;
      } finally {
        instream.close();
      }
    }

  }

  private UrlEncodedFormEntity buildFormEntity(List<NameValuePair> nameValuePairs) {
    try {
      return new UrlEncodedFormEntity(nameValuePairs);
    } catch (UnsupportedEncodingException e) {
      log.error("Unsupported encoding exception");
      throw new SecurityException("Unsupported encoding exception");
    }
  }

  private HttpEntity buildHttpEntity(MultipartEntityBuilder builder) {
    return builder.build();
  }


}
