package com.hypers.dataimport.solr;

import com.hypers.common.exception.ParameterErrorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author long
 * @since 17-10-11
 */
public class SolrUpdater {

  private static final Logger logger = LoggerFactory.getLogger(SolrUpdater.class);

  private SolrServer solrServer;
  private int docsPerUpdate = 1000;
  private int bufferSize = 4096;
  private int charsPerLine;
  private String pairSeparator = "\t\t\t";
  private String keyValueSeparator = ":";
  private FieldMapper mapper;
  private long id = 0;
  //0 means successful, 1 means part fail, 2 means fail
  private int commitStatus = 0;

  public SolrUpdater(SolrServer solrServer) {
    this.solrServer = solrServer;
  }

  void init() {
    commitStatus = 0;
  }

  public void setMapper(FieldMapper mapper) {
    this.mapper = mapper;
  }

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void setKeyValueSeparator(String keyValueSeparator) {
    this.keyValueSeparator = keyValueSeparator;
  }

  public void setPairSeparator(String pairSeparator) {
    this.pairSeparator = pairSeparator;
  }

  public void setDocsPerUpdate(int docsPerUpdate) {
    this.docsPerUpdate = docsPerUpdate;
  }

  public void setCharsPerLine(int charsPerLine) {
    this.charsPerLine = charsPerLine;
    if (charsPerLine * docsPerUpdate * 2 > bufferSize) {
      bufferSize = charsPerLine * docsPerUpdate * 2;
    }
  }

  public void setSolrServer(SolrServer solrServer) {
    this.solrServer = solrServer;
  }

  void synchronizeId() {
    //TODO
  }

  void storeErrorLine(int rowNum, String line) {
    //TODO
    logger.error("format is error in line {}: {}", rowNum, line);
  }

  void storeCommitErrorLine(BufferedReader reader, int rowStart, int count) throws IOException {
    for (int i = 0; i < count; i++) {
      logger.error("failed when commit in line {}: {}", rowStart++, reader.readLine());
    }
  }

  InputStream writeErrorLines() {
    PipedOutputStream pipedOutputStream = new PipedOutputStream();
    PipedInputStream pipedInputStream = new PipedInputStream();
    try {
      pipedInputStream.connect(pipedOutputStream);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(pipedOutputStream));
      //TODO do write
    } catch (IOException e) {
      //TODO
    }
    return null;
  }

  //   public void update(String collection,InputStream in) throws IOException{
//    ((CloudSolrServer) solrServer).setDefaultCollection(collection);
//    synchronizeId();
//
//    InputStreamReader inputStreamReader=new InputStreamReader(in);
//    BufferedReader bufferedReader = new BufferedReader(inputStreamReader, bufferSize);
//    int count = 1;
//    int rows = 0;
//    try {
//      String line = bufferedReader.readLine();
//      List<SolrInputDocument> documents = new ArrayList<>(docsPerUpdate);
//
//      while (line != null) {
//        try {
//          documents.add(resolveLine(line));
//        } catch (Exception e) {
//          logger.error("Error in line {}: {}", rows, line, e);
//          storeErrorLine(rows,line);
//          line = bufferedReader.readLine();
//          count++;
//          rows++;
//          continue;
//        }
//
//        if (count == docsPerUpdate) {
//          logger.info("Commit {} documents to collection: {}", count, collection);
//          try {
//            commit(documents);
//          } catch (ParameterErrorException e) {
//            synchronizeId();
//          }
//          documents.clear();
//          count = 0;
//        }
//
//        line = bufferedReader.readLine();
//        count++;
//        rows++;
//      }
//
//      if (documents.size() > 0) {
//        logger.info("Commit {} documents to collection: {}", count, collection);
//        try {
//          commit(documents);
//        } catch (ParameterErrorException e) {
//          synchronizeId();
//        }
//      }
//    } catch (IOException e) {
//      logger.error("Error occurs when reading {}th row",rows, e);
//      throw e;
//    }
//  }
//

  public int getCommitStatus(){
    return commitStatus;
  }

  public InputStream update(String collection, InputStream in) throws IOException {
    ((CloudSolrServer) solrServer).setDefaultCollection(collection);
    synchronizeId();
    init();

    InputStreamReader inputStreamReader = new InputStreamReader(in);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader, bufferSize);
    int count = 1;
    int rows = 0;
    try {
      bufferedReader.mark(charsPerLine * docsPerUpdate);
      String line = bufferedReader.readLine();
      List<SolrInputDocument> documents = new ArrayList<>(docsPerUpdate);

      while (line != null) {
        try {
          documents.add(resolveLine(line));
        } catch (Exception e) {
          logger.error("Error in line {}: {}", rows, line, e);
          commitStatus = 1;
          storeErrorLine(rows, line);
          line = bufferedReader.readLine();
          count++;
          rows++;
          continue;
        }

        if (count == docsPerUpdate) {
          logger.info("Commit {} documents to collection: {}", count, collection);
          try {
            commit(documents);
            bufferedReader.mark(charsPerLine * docsPerUpdate);

          } catch (ParameterErrorException e) {
            commitStatus = 1;
            synchronizeId();

            bufferedReader.reset();
            storeCommitErrorLine(bufferedReader, rows - count + 1, count);
            bufferedReader.mark(charsPerLine * docsPerUpdate);
          }
          documents.clear();
          count = 0;
        }

        line = bufferedReader.readLine();
        count++;
        rows++;
      }

      if (documents.size() > 0) {
        logger.info("Commit {} documents to collection: {}", count, collection);
        try {
          commit(documents);
        } catch (ParameterErrorException e) {
          commitStatus = 1;
          bufferedReader.reset();
          storeCommitErrorLine(bufferedReader, rows - count + 1, count);
        }
      }
    } catch (IOException e) {
      logger.error("Error occurs when reading {}th row", rows, e);
      if (rows == 0) {
        commitStatus = 2;
      } else {
        commitStatus = 1;
      }
      throw e;
    }

    return writeErrorLines();
  }

  public void update(String collection, File docs) throws FileNotFoundException {
    try {
      update(collection, new FileInputStream(docs));
    } catch (IOException e) {
      commitStatus = 1;
      logger.error("Error occurs when reading file '{}'", docs, e);
    }
  }

  SolrInputDocument resolveLine(String line) throws NoSuchFieldException {
    SolrInputDocument doc = new SolrInputDocument();
    String[] pairs = line.split(pairSeparator);
    for (String pair : pairs) {
//      String[] keyValue=pair.split(keyValueSeparator);
//      doc.addField(mapping(keyValue[0]),keyValue[1]);
      logger.trace("Resolve line: {}", line);

      int index = pair.indexOf(keyValueSeparator);
      doc.addField(mapping(pair.substring(0, index)), pair.substring(index + 1, pair.length()));
    }
    doc.addField(mapping("_id"), ++id);

    return doc;
  }

  public String mapping(String in) throws NoSuchFieldException {
    return mapper.mapping(in);
  }

  public void commit(List<SolrInputDocument> docs) throws ParameterErrorException {
    try {
      solrServer.add(docs);
      solrServer.commit();
      docs.clear();
    } catch (SolrException | SolrServerException | IOException e) {
      logger.error("Error occurs when commit documents", e);
      throw new ParameterErrorException(e.getMessage(), e);
    }
  }
}
