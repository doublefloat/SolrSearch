package com.hypers.dataimport.solr;

import java.io.File;
import org.junit.Test;

/**
 * @author long
 * @since 17-10-11
 */
public class LogResolverTest {

  LogResolver resolver = DataProvider.logResolver();

//  @Test
  public void testResolve() throws Exception {
    resolver.resolve(new File("top_10000.log"), new File("out_10000.log"));
  }

}
