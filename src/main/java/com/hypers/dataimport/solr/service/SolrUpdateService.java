package com.hypers.dataimport.solr.service;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author long
 * @since 17-10-24
 */
public interface SolrUpdateService {
  int update(UpdaterParas paras,MultipartFile source);
}
