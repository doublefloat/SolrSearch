package com.hypers.search.utils.collection;

import java.util.List;

import org.apache.solr.client.solrj.impl.CloudSolrServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hypers.common.exception.ParameterErrorException;

/**
 * @author long
 * @since 17-8-21
 */
public class FieldFinder extends AbstractHttpFinder {

  public static final String FIELD_TYPE = "fields";
  public static final String DYNAMIC_FIELD_TYPE = "dynamicfields";
  private static final String DYNAMIC_FIELD = "dynamicFields";
  private static final String ELEMENT = "";

  public FieldFinder(CloudSolrServer server) {
    super(server);
  }

//  public FieldFinder(String zkHost) {
//    try {
//    } catch (MalformedURLException e) {
//      throw new IllegalArgumentException("URL is not valid:" + zkHost, e);
//    }
//  }

  protected String constructURL(String host, String collection, String fieldType, String element) {
    assert collection != null;

    StringBuffer sb = new StringBuffer();
    sb.append("/solr/");
    sb.append(collection);
    sb.append("/schema");

    if (fieldType == null) {
      sb.append("/" + FIELD_TYPE);
    } else {
      sb.append("/" + fieldType);
    }

    if (element == null) {
      sb.append("/" + ELEMENT);
    } else {
      sb.append("/" + element);
    }

    return sb.toString();
  }

  protected String constructURL(String collection, String fieldType, String element) {
    return constructURL(null, collection, fieldType, element);
  }

  protected <T> List<T> parseObject(String jsonString, String fieldType, Class<T> cs) {
    String key = fieldType;
    if (fieldType.equals(DYNAMIC_FIELD_TYPE)) {
      key = DYNAMIC_FIELD;
    }

    JSONArray array = JSON.parseObject(jsonString).getJSONArray(key);
    return array.toJavaList(cs);
  }

  public <T> List<T> getFields(String collection, String fieldType, String element, Class<T> cls)
      throws ParameterErrorException {
    String url = constructURL(collection, fieldType, element);
    try {
      return parseObject(execute(url), fieldType, cls);
    }catch(SecurityException e){
      throw new ParameterErrorException(e);
    }
  }

  public <T> List<T> getFields(String collection, String fieldType, Class<T> cls)
      throws ParameterErrorException {
    try {
      return getFields(collection, fieldType, null, cls);
    }catch(SecurityException e){
      throw new ParameterErrorException(e);
    }
  }

}
