/**
 * 
 */
package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly = false)
	@Override
	public Sample insert(Sample sample) {
		return sameRepository.save(sample);
	}

	/**
	 * Should be used the READ_ONLY
	 */
	@Transactional(readOnly = true)
	@Override
	public Optional<Sample> findById(Long id) {
		return sameRepository.findById(id);
	}

	/**
	 * Read all data from sample Table in slave database
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Sample> findAll() {
		return sameRepository.findAll();
	}

}
