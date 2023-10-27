/**
 * 
 */
package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.annotation.RoutingTransactional;
import com.example.demo.entity.Sample;
import com.example.demo.repository.SameRepository;
import com.example.demo.service.ISampleService;

/**
 * @author toifi
 *
 */
@Service
public class SampleService implements ISampleService {

	@Autowired
	private SameRepository sameRepository;

	/**
	 * Should be used the READ_WRITE Insert data to sample table with master
	 * database
	 */
	@RoutingTransactional(readonly = false)
	@Override
	public Sample insert(Sample sample) {
		return sameRepository.save(sample);
	}

	/**
	 * Should be used the READ_ONLY
	 */
	@Override
	public Optional<Sample> findById(Long id) {
		return sameRepository.findById(id);
	}

	/**
	 * Read all data from sample Table in slave database
	 */
	@RoutingTransactional(readonly = true)
	@Override
	public List<Sample> findAll() {
		return sameRepository.findAll();
	}

}
