package com.hypers.search.utils;


import com.hypers.common.exception.ParameterErrorException;
import static com.hypers.search.utils.CommonTask.escape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author long
 * @since 17-11-6
 */
public class FilterResolver implements Resolver<String, List<String>> {

  public static Set<Character> particularCharacters;

  static {
    //TODO add more characters
    Set<Character> set = new HashSet<>();
    set.add('(');
    set.add(')');
    set.add(' ');
    set.add(':');
    set.add('\"');
    particularCharacters = Collections.unmodifiableSet(set);
  }

  public List<String> resolve(String filters) throws ParameterErrorException{
    if (filters == null) {
      List<String> rs = new ArrayList<>(1);
      rs.add("*");
      return rs;
    }

    // TODO should handle errors in filters
    // filter format is as f1:v1,v2;f2:v1,v2
    List<String> results = new ArrayList<>();
    for (String filter : filters.split(";")) {
      StringBuilder sb = new StringBuilder();
      String[] fl = filter.split(",");
      sb.append(escape(fl[0],particularCharacters,fl[0].indexOf(":")+1,fl[0].length()));

      for (int i = 1; i < fl.length; i++) {
        sb.append(" OR " + escape(fl[i],particularCharacters,0,fl[i].length()));
      }
      sb.insert(sb.indexOf(":") + 1, '(');
      sb.insert(sb.length(), ")");
      results.add(sb.toString());
    }

    return results;
  }

  public static String joinParameters(List<String> paras, String symbol) {
    String usedSymbol = " " + symbol + " ";
    StringBuilder sb = new StringBuilder();

    for (String para : paras) {
      sb.append(para);
      sb.append(usedSymbol);
    }
    sb.delete(sb.lastIndexOf(usedSymbol), sb.length());

    return sb.toString();
  }
}
