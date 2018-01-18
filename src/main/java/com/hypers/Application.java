package com.hypers;

import com.hypers.history.config.SearchHistoryContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.hypers.search.config.SearchContext;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({SearchContext.class, SearchHistoryContext.class})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
