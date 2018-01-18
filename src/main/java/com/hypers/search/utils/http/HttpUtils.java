package com.hypers.search.utils.http;

import java.net.URI;
import java.security.InvalidParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Ivan
 *
 */
public class HttpUtils {
  public static URIBuilder URIBuilder(String scheme, String host, String path) {
    return new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
  }

  public static void validURI(URI uriToValid) {
    if (null == uriToValid) {
      throw new InvalidParameterException("Not normative to form request with empty uri");
    }
  }
  public static CloseableHttpClient customSecureClient(BasicCookieStore cookieStore) {
    CloseableHttpClient secureHttpClient = null;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(10 * 1000).build();
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
        .setDefaultRequestConfig(config).setSSLSocketFactory(sslsf)
        .setDefaultCookieStore(cookieStore).build();

    return secureHttpClient;
  }

}