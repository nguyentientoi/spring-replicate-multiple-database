/**
 * 
 */
package com.example.demo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author toifi
 *
 */
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
	private static final Logger logger = LoggerFactory.getLogger(ReplicationRoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		logger.info("Current datasource lookup key: " + DynamicDataSourceContextHolder.getDataSourceKey());

		return DynamicDataSourceContextHolder.getDataSourceKey();
	}

}
