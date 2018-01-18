package com.hypers.search.utils;

import com.hypers.common.exception.ParameterErrorException;

/**
 * @author long
 * @since 17-11-6
 */
public interface Resolver<I, O> {

  O resolve(I input) throws ParameterErrorException;
}
