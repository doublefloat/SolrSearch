package com.hypers.search.utils;

/**
 * @author long
 * @since 17-11-7
 */
public interface Mapper<I, O> {

  O mapping(I input);
}
