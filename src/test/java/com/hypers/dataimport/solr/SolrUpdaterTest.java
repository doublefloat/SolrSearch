package com.hypers.dataimport.solr;

import java.io.File;
import org.junit.Test;

/**
 * @author long
 * @since 17-10-11
 */
public class SolrUpdaterTest {

  private SolrUpdater updator = DataProvider.updater("10.123.114.93:11030");

//  @Test
  public void testUpdate() throws Exception {
    updator.update("log-production", new File("out_full_utf.log"));
  }
}
