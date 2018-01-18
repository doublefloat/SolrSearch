package com.hypers.search.model;

/**
 * @author long
 * @since 17-8-21
 */
public class SolrField {

  private String name;
  private String type;
  private Boolean indexed;
  private Boolean stored;
  private Boolean uniqueKey;

  public SolrField(){
  }

  public SolrField(SolrField field) {
    this.name = field.name;
    this.type = field.type;
    this.indexed = field.indexed;
    this.stored = field.stored;
    this.uniqueKey = field.uniqueKey;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setIndexed(Boolean indexed) {
    this.indexed = indexed;
  }

  public Boolean getIndexed() {
    return indexed;
  }

  public void setStored(Boolean stored) {
    this.stored = stored;
  }

  public Boolean getStored() {
    return stored;
  }

  public void setUniqueKey(Boolean uniqueKey) {
    this.uniqueKey = uniqueKey;
  }

  public Boolean getUniqueKey() {
    return uniqueKey;
  }

  @Override
  public String toString() {
    return "SolrField [name=" + name + ", type=" + type + ", indexed=" + indexed + ", stored="
        + stored + "]";
  }

}
