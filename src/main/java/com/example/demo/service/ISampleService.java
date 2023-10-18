/**
 * 
 */
package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Sample;

/**
 * @author toifi
 *
 */
public interface ISampleService {
	public Sample insert(Sample sample);

	public Optional<Sample> findById(Long id);

	public List<Sample> findAll();

}
