/**
 * 
 */
package com.example.demo.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.demo.DemoApplication;
import com.example.demo.constant.DataSourceType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author toifi
 *
 */
@Configuration
@EnableTransactionManagement
public class WithRoutingDataSourceConfig {

	private final Environment environment;

	public WithRoutingDataSourceConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public JpaTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory.getObject());
		/*
		 * JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		 * jpaTransactionManager.setEntityManagerFactory(entityManagerFactory); return
		 * jpaTransactionManager;
		 */
	}
	/*
	 * @Bean public TransactionTemplate transactionTemplate(EntityManagerFactory
	 * entityManagerFactory) { return new
	 * TransactionTemplate(transactionManager(entityManagerFactory)); }
	 */

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier("routingDataSource") DataSource routingDataSource) {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(routingDataSource);
		bean.setPackagesToScan(DemoApplication.class.getPackageName());
		bean.setPersistenceUnitName(getClass().getSimpleName());
		bean.setPersistenceProvider(new HibernatePersistenceProvider());

		// Don't need
		// HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		// HibernateJpaDialect jpaDialect = jpaVendorAdapter.getJpaDialect();
		// jpaDialect.setPrepareConnection(false);

		// bean.setJpaVendorAdapter(jpaVendorAdapter);
		// bean.setJpaProperties(additionalProperties());
		return bean;
	}

	/**
	 * 
	 * @param dataSource
	 * @param poolName
	 * @return
	 */
	private HikariDataSource connectionPoolDataSource(DataSource dataSource, String poolName) {
		return new HikariDataSource(hikariConfig(dataSource, poolName));
	}

	/**
	 * Configuration Hikari
	 * 
	 * @param dataSource
	 * @param poolName
	 * @return
	 */
	private HikariConfig hikariConfig(DataSource dataSource, String poolName) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setPoolName(poolName);
		hikariConfig.setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
		hikariConfig.setDataSource(dataSource);
		hikariConfig.setAutoCommit(false);
		return hikariConfig;
	}

	@Bean
	public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}

	@Bean
	public ReplicationRoutingDataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
			@Qualifier("slaveDataSource") DataSource slaveDataSource) {
		ReplicationRoutingDataSource dataSource = new ReplicationRoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(DataSourceType.READ_ONLY, slaveDataSource);
		dataSourceMap.put(DataSourceType.READ_WRITE, masterDataSource);

		dataSource.setDefaultTargetDataSource(masterDataSource);
		dataSource.setTargetDataSources(dataSourceMap);
		return dataSource;
	}

	@Bean
	public DataSource masterDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(environment.getProperty("datasource.master.url"));
		dataSource.setUsername(environment.getProperty("datasource.master.username"));
		dataSource.setPassword(environment.getProperty("datasource.master.password"));
		return connectionPoolDataSource(dataSource, determinePoolName(DataSourceType.READ_WRITE));
	}

	@Bean
	public DataSource slaveDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(environment.getProperty("datasource.slave.url"));
		dataSource.setUsername(environment.getProperty("datasource.slave.username"));
		dataSource.setPassword(environment.getProperty("datasource.slave.password"));
		return connectionPoolDataSource(dataSource, determinePoolName(DataSourceType.READ_ONLY));
	}

	/*
	 * Don't create BeanPostProcessor because AOP is always order before service
	 * 
	 * @Bean public BeanPostProcessor dialectProcessor() { return new
	 * BeanPostProcessor() {
	 * 
	 * @Override public Object postProcessBeforeInitialization(Object bean, String
	 * beanName) throws BeansException { if (bean instanceof
	 * HibernateJpaVendorAdapter) { ((HibernateJpaVendorAdapter)
	 * bean).setPrepareConnection(false); } return bean; }
	 * 
	 * }; }
	 */

	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", environment.getProperty("spring.jpa.database-platform"));
		properties.setProperty("hibernate.connection.provider_disables_autocommit", "true");
		return properties;
	}

	private String determinePoolName(DataSourceType dataSourceType) {
		return dataSourceType.getPoolName().concat("-").concat(dataSourceType.name());
	}

}
