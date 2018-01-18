package com.hypers.search.utils;

import java.util.Set;

/**
 * @author long
 * @since 17-10-24
 */
public class CommonTask {

  public static String escape(CharSequence sequence, char symbol, int begin, int end) {
    StringBuilder sb = new StringBuilder(sequence.subSequence(0, begin));
    for (int i = begin; i < end; i++) {
      if (sequence.charAt(i) == symbol) {
        if (i == 0 || sequence.charAt(i - 1) != '\\') {
          sb.append('\\');
        }
      }
      sb.append(sequence.charAt(i));
    }
    return sb.toString();
  }

  public static String escape(CharSequence sequence, Set<Character> symbols, int begin, int end) {
    StringBuilder sb = new StringBuilder(sequence.subSequence(0, begin));
    for (int i = begin; i < end; i++) {
      if (symbols.contains(sequence.charAt(i))) {
        if (i == 0 || sequence.charAt(i - 1) != '\\') {
          sb.append('\\');
        }
      }
      sb.append(sequence.charAt(i));
    }
    return sb.toString();
  }
}
