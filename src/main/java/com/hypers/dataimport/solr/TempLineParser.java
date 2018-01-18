package com.hypers.dataimport.solr;


import com.hypers.search.utils.date.SolrDateUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author long
 * @since 17-10-12
 */
public class TempLineParser implements LineParser {

  private String dateFormat = "yyyyMMdd_HH:mm:ss.SSS";
  private DateFormat format = new SimpleDateFormat(dateFormat);
  private char pairSeparator = ' ';
  private char keyValueSeparator = ':';
  private String formatPairSeparator = "\t\t\t";

  public String parseLine(String line) throws ParseException {
    StringBuffer sb = new StringBuffer(line);

    sb.delete(0, sb.indexOf("_") + 1);
    String time = sb.substring(0, sb.indexOf(" "));
    sb.delete(0, sb.indexOf(" "));
    sb.insert(0, parseAsUTC(time));
    sb.insert(0, "log_dt:");
    sb.insert(sb.indexOf(" ") + 1, "type:");

    if (line.contains("已过期")) {
      sb.delete(sb.lastIndexOf("|", sb.indexOf("已过期")) + 1, sb.length());
    }
    if (line.contains("营业部") && !line.contains("耗时")) {
      int index = sb.indexOf("(*)", line.indexOf("营业部"));
      sb.delete(index == -1 ? sb.indexOf("\u0001") : index, sb.length());
    }
    if(line.contains("港股行情服务")||line.contains("深沪行情服务")){
      return "";
    }
    replacePairSeparator(sb);

    sb.append(formatPairSeparator + "message:");
    sb.append(line);
    sb.insert(sb.length(), "\n");

    return sb.toString();
  }

  String parseAsUTC(String timeString) throws ParseException {
    return SolrDateUtil
        .formatAsUTC(TimeZone.getDefault(), Locale.getDefault(), format.parse(timeString));
  }

  void replacePairSeparator(StringBuffer sb) {
    String keyValueSeparator = Character.toString(this.keyValueSeparator);
    String pairSeparator = Character.toString(this.pairSeparator);

    for (int i = sb.indexOf(pairSeparator, 0); i < sb.length() && i != -1;
        i = sb.indexOf(pairSeparator, i + 1)) {
      int index = sb.indexOf(keyValueSeparator, i);
      int next = sb.indexOf(keyValueSeparator, index + 1);
      if (index == -1 || next - index <= 3 && next > 0
          || next == -1 && sb.indexOf("C:", index - 1) != -1) {
        return;
      }
      sb.deleteCharAt(i);
      sb.insert(i, formatPairSeparator);
    }
  }

  void replaceFormatPairSeparator(StringBuffer sb) {
    for (int i = sb.indexOf(formatPairSeparator, 0); i < sb.length() && i != -1;
        i = sb.indexOf(formatPairSeparator, i + 1)) {
      sb.deleteCharAt(i);
      sb.insert(i, " ");
    }
  }
}
