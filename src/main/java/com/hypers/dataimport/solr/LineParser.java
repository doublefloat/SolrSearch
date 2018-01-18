package com.hypers.dataimport.solr;

import java.text.ParseException;

/**
 * @author long
 * @since 17-10-12
 */
public interface LineParser {

  String parseLine(String line) throws ParseException;
}
