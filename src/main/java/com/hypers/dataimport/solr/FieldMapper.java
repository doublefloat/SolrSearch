package com.hypers.dataimport.solr;

/**
 * @author long
 * @since 17-10-11
 */
public interface FieldMapper {

  String mapping(String in) throws NoSuchFieldException;
}
