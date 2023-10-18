/**
 * 
 */
package com.example.demo.constant;

/**
 * @author toifi
 *
 */
public enum DataSourceType {
	READ_ONLY, READ_WRITE;

	public String getPoolName() {
		return "Pool";
	}
}
