package com.hypers.dataimport.solr.service.impl;

import com.hypers.dataimport.solr.SolrUpdater;
import com.hypers.dataimport.solr.service.SolrUpdateService;
import com.hypers.dataimport.solr.service.UpdaterParas;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author long
 * @since 17-10-24
 */
@Service
public class SolrUpdateServiceImpl implements SolrUpdateService {
  Logger logger= LoggerFactory.getLogger(this.getClass());

  private SolrUpdater updater;

  @Autowired
  public void setUpdater(SolrUpdater updater) {
    this.updater = updater;
  }

  void initUpdater(UpdaterParas paras){
    if(paras.getCharsPerLine() != null) {
      updater.setCharsPerLine(paras.getCharsPerLine());
    }

    if (paras.getDocsPerUpdate() != null) {
      updater.setDocsPerUpdate(paras.getDocsPerUpdate());
    }

    if (paras.getKeyValueSeparator() != null) {
      updater.setKeyValueSeparator(paras.getKeyValueSeparator());
    }
  }

  public int update(UpdaterParas paras, MultipartFile source){
    initUpdater(paras);
    try {
      updater.update(paras.getCollection(), source.getInputStream());
    }catch(IOException e){

    }
    return updater.getCommitStatus();
  }

}
