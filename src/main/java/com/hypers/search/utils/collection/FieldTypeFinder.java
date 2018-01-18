package com.hypers.search.utils.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hypers.common.exception.ParameterErrorException;
import com.hypers.search.model.SolrFieldType.Attributes;
import java.util.ArrayList;
import org.apache.solr.client.solrj.impl.CloudSolrServer;

import com.hypers.search.model.SolrFieldType;

import java.util.List;

/**
 * @author long
 * @since 17-12-18
 */
public class FieldTypeFinder extends AbstractHttpFinder {

  private static final String PREPFIX = "/fieldtypes";
  private static final String FIELD_TYPES = "fieldTypes";
  private static final String FIELD_TYPE = "fieldType";

  public FieldTypeFinder(CloudSolrServer server) {
    super(server);
  }

  protected String constructURL(String collection, String prefix, String type) {
    StringBuilder sb = new StringBuilder();
    sb.append("/solr/");
    sb.append(collection);
    sb.append("/schema");
    sb.append(prefix);
    if (type != null) {
      sb.append("/");
      sb.append(type);
    }
    return sb.toString();
  }

  protected List<SolrFieldType> parseSingleObject(String jsonString, String key) {
    JSONObject obj = (JSONObject) JSON.parseObject(jsonString).get(key);
    SolrFieldType type = new SolrFieldType();
    type.set(Attributes.NAME, (String) obj.get(Attributes.NAME));
    type.set(Attributes.CLASS, (String) obj.get(Attributes.CLASS));

    JSONArray jsonArray = ((JSONArray) obj.get(Attributes.FIELDS));
    String[] fields = new String[jsonArray.size()];
    fields = jsonArray.toArray(fields);
    type.set(Attributes.FIELDS, fields);

    jsonArray = (JSONArray) obj.get(Attributes.DYNAMIC_FIELDS);
    fields = new String[jsonArray.size()];
    fields = jsonArray.toArray(fields);
    type.set(Attributes.DYNAMIC_FIELDS, fields);
    List<SolrFieldType> result = new ArrayList<>();

    result.add(type);
    return result;
  }

  protected List<SolrFieldType> parseObject(String jsonString, String key) {
    JSONArray array = JSON.parseObject(jsonString).getJSONArray(key);
    List<SolrFieldType> result = new ArrayList<>(array.size());
    for (Object object : array) {
      JSONObject obj = (JSONObject) object;
      SolrFieldType type = new SolrFieldType();
      type.set(Attributes.NAME, (String) obj.get(Attributes.NAME));
      type.set(Attributes.CLASS, (String) obj.get(Attributes.CLASS));

      JSONArray jsonArray = ((JSONArray) obj.get(Attributes.FIELDS));
      String[] fields = new String[jsonArray.size()];
      fields = jsonArray.toArray(fields);
      type.set(Attributes.FIELDS, fields);

      jsonArray = (JSONArray) obj.get(Attributes.DYNAMIC_FIELDS);
      fields = new String[jsonArray.size()];
      fields = jsonArray.toArray(fields);
      type.set(Attributes.DYNAMIC_FIELDS, fields);

      result.add(type);
    }
    return result;
  }

  public List<SolrFieldType> getSolrTypes(String collection) throws ParameterErrorException {
    String response;
    try {
      response = execute(constructURL(collection, PREPFIX, null));
    } catch (SecurityException e) {
      throw new ParameterErrorException("can't find collection:" + collection);
    }
    return parseObject(response, FIELD_TYPES);
  }

  public SolrFieldType getSolrType(String collection, String type) throws ParameterErrorException {
    String response;
    try {
      response = execute(constructURL(collection, PREPFIX, type));
    } catch (SecurityException e) {
      throw new ParameterErrorException(e);
    }
    return parseObject(response, FIELD_TYPE).iterator().next();
  }
}
