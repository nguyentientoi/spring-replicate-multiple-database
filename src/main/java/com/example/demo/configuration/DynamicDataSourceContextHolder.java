package com.example.demo.configuration;

import com.example.demo.constant.DataSourceType;

/**
 * 
 */
public class DynamicDataSourceContextHolder {

	/**
	 * 
	 */
	private static final ThreadLocal<DataSourceType> ctxHolder = new ThreadLocal<>() {
		@Override
		protected DataSourceType initialValue() {
			return DataSourceType.READ_WRITE;
		}
	};

	public static void setDataSourceKey(DataSourceType key) {
		ctxHolder.set(key);
	}

	public static DataSourceType getDataSourceKey() {
		return ctxHolder.get();
	}

	public static void clear() {
		ctxHolder.remove();
	}
}
