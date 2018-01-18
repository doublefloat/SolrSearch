package com.hypers.dataimport.solr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author long
 * @since 17-10-11
 */
public class LogResolver {
  private static final Logger logger= LoggerFactory.getLogger(LogResolver.class);

  private int bufferSize = 4096;
  private LineParser parser;

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void setParser(LineParser parser) {
    this.parser = parser;
  }

  public void resolve(File src, File dst) throws IOException {
    BufferedReader bufferedReader = null;
    BufferedWriter writer = null;

    if (!dst.exists()) {
      logger.info("Create new file {}",dst.getCanonicalFile());

      dst.createNewFile();
    }

    logger.info("Parse file: {} to file: {}",src.getCanonicalFile(),dst.getCanonicalFile());

    try {
      bufferedReader = new BufferedReader(new FileReader(src), bufferSize);
      writer = new BufferedWriter(new FileWriter(dst));

      String line = bufferedReader.readLine();
      long count=0;
      while (line != null) {
        try {
          logger.trace("Parse line {}: {}",count,line);

          line = parser.parseLine(line);
        } catch (ParseException e) {
          logger.error("Cant't parse line {}: {}",count,line,e);

          line = bufferedReader.readLine();
          count++;
          continue;
        } catch (Exception e) {
          logger.error("Unexpected exception: ",e);
          return;
        }
        writer.write(line);

        line = bufferedReader.readLine();
        count++;
      }

    } finally {
      if (bufferedReader != null) {
        bufferedReader.close();
      }

      if (writer != null) {
        writer.flush();
        writer.close();
      }

      logger.info("Parsing is completed");
    }

  }
}
