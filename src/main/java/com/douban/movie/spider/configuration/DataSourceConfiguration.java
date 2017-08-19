package com.douban.movie.spider.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan(basePackages = DataSourceConfiguration.MAPPER_LOCATION, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {

	private static final String XMLMAPPER_LOCAL = "classpath:mapper/*.xml";
	protected static final String MAPPER_LOCATION = "com.douban.movie.spider.mapper";

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "movie.datasource")
	public DataSource druidDataSource() {
		return new DruidDataSource();
	}

	@Bean
	@Primary
	public DataSourceTransactionManager druidTransactionManager() {
		return new DataSourceTransactionManager(druidDataSource());
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(XMLMAPPER_LOCAL));
		return sessionFactoryBean.getObject();
	}
}
