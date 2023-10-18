/**
 * 
 */
package com.example.demo.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.demo.constant.DataSourceType;

/**
 * @author toifi
 *
 */
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? DataSourceType.READ_ONLY
				: DataSourceType.READ_WRITE;
	}

}
