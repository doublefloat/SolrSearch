package com.hypers.search.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author long
 * @since 17-12-18
 */
public class SolrFieldType {

  public static final class Attributes {

    public static final String NAME = "name";
    public static final String CLASS = "class";
    public static final String STORED = "stored";
    public static final String DOC_VALUES = "docValues";
    public static final String PRECISION_STEP = "precisionStep";
    public static final String FIELDS = "fields";
    public static final String DYNAMIC_FIELDS = "dynamicFields";
    public static final String OMITNORMS = "omitNorms";
    public static final String SORT_MISSING_LAST = "sortMissingLast";
    public static final String ANALYZER = "analyzer";
    public static final String TOKENIZER = "tokenizer";
    public static final String FILTERS = "filters";
    public static final String PATTERN = "pattern";
    public static final String REPLACE = "replace";
    public static final String REPLACEMENT = "replacement";
    public static final String QUERY_ANALYZER = "queryAnalyzer";
    public static final String INDEX_ANALYZER = "indexAnalyzer";
    public static final String DELIMITER = "delimiter";
    public static final String GEO = "geo";
    public static final String NUMBER_TYPE = "numberType";
    public static final String UNITS = "units";
    public static final String CURRENCY_CONFIG = "currencyConfig";
    public static final String DEFAULT_CURRENCY = "defaultCurrency";
    public static final String POSITION_INCREMENT_GAP = "positionIncrementGap";
    public static final String SUB_FIELD_SUFFIX = "subFieldSuffix";
    public static final String MAX_DISERR = "maxDisError";
    public static final String DIST_ERR_PCT = "disErrPct";
    public static final String DIMENSION = "dimension";
    public static final String WORDS = "words";
    public static final String IGNORE_CASE = "ignoreCase";
    public static final String FORMAT = "format";
    public static final String LANGUAGE = "language";
    public static final String EXPAND = "expand";
    public static final String AUTO_GENERATE_PHRASE_QUERIES = "autoGeneratePhraseQueries";
    public static final String CATENATE_NUMBERS = "catenateNumbers";
    public static final String GENERATE_NUMBER_PARTS = "generateNumberParts";
    public static final String SPLITON_CASE_CHANGE = "splitOnCaseChange";
    public static final String GENERATE_WORD_PARTS = "generateWordParts";
    public static final String CATENATE_ALL = "catenateAll";
    public static final String CATENATE_WORDS = "catenateWords";
    public static final String SYNONYMS = "synonyms";
    public static final String PROTECTED = "protected";
    public static final String CHAR_FILTERS = "charFilters";
    public static final String MODE = "mode";
    public static final String TAGS = "tags";
    public static final String MINIMUM_LENGTH = "minimumLength";
    public static final String DICTIONARY = "dictionary";
  }

  private Map<String, String[]> vals;

  private List<SolrFieldType> innerFields;

  public SolrFieldType() {
    this.vals = new HashMap<>();
    this.innerFields = new LinkedList<>();
  }

  public void set(String key, String... val) {
    if (val == null || (val.length == 1 && val[0] == null)) {
      vals.remove(key);
    } else {
      vals.put(key, val);
    }
  }

  public String get(String key) {
    String[] val = vals.get(key);
    if (val != null && val.length > 0) {
      return val[0];
    }
    return null;
  }

  public String[] getAll(String key) {
    return vals.get(key);
  }

  public void setName(String val) {
    this.set(Attributes.NAME, val);
  }

  public String getName() {
    return this.get(Attributes.NAME);
  }

  public void setClassName(String val) {
    this.set(Attributes.CLASS, val);
  }

  public String getClassName() {
    return this.get(Attributes.CLASS);
  }

//    public void setStored(boolean val){
//    this.set(Attributes.STORED,String.valueOf(val));
//  }
//
//  public void setDocValues(boolean val){
//    this.set(Attributes.DOC_VALUES,String.valueOf(val));
//  }
//
//  public void setPrecisionStep(int val){
//    this.set(Attributes.PRECISION_STEP,String.valueOf(val));
//  }
//
  public void setFields(String... val) {
    this.set(Attributes.FIELDS, val);
  }

  public String[] getFields() {
    return this.getAll(Attributes.FIELDS);
  }

  public void setDynamicFields(String... val) {
    this.set(Attributes.DYNAMIC_FIELDS, val);
  }

  public String[] getDynamicFields() {
    return this.getAll(Attributes.DYNAMIC_FIELDS);
  }
}
