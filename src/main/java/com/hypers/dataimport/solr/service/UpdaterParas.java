package com.hypers.dataimport.solr.service;

/**
 * @author long
 * @since 17-10-24
 */
public class UpdaterParas {
  private String collection;
  private Integer docsPerUpdate;
  private Integer charsPerLine;
  private String pairSeparator;
  private String keyValueSeparator;

  public void setCharsPerLine(Integer charsPerLine) {
    this.charsPerLine = charsPerLine;
  }

  public void setPairSeparator(String pairSeparator) {
    this.pairSeparator = pairSeparator;
  }

  public void setKeyValueSeparator(String keyValueSeparator) {
    this.keyValueSeparator = keyValueSeparator;
  }

  public void setDocsPerUpdate(Integer docsPerUpdate) {
    this.docsPerUpdate = docsPerUpdate;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public Integer getCharsPerLine() {
    return charsPerLine;
  }

  public Integer getDocsPerUpdate() {
    return docsPerUpdate;
  }

  public String getCollection() {
    return collection;
  }

  public String getKeyValueSeparator() {
    return keyValueSeparator;
  }

  public String getPairSeparator() {
    return pairSeparator;
  }

}
