package com.hypers.history.config;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author long
 * @since 17-9-1
 */
@Configuration
public class SearchHistoryContext {
  @Value("${mybatis.resource}")
  String resource;

  @Bean
  public SqlSessionFactory sqlSessionFactory()throws IOException {
    InputStream inputStream = Resources.getResourceAsStream(resource);
    return new SqlSessionFactoryBuilder().build(inputStream);
  }

  @Bean
  public SqlSession sqlSession()throws IOException{
    SqlSessionFactory factory=sqlSessionFactory();
    return factory.openSession();
  }
}
