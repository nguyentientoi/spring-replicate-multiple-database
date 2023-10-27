/**
 * 
 */
package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Sample;
import com.example.demo.service.ISampleService;

/**
 * @author toifi
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/sample", produces = "application/json;charset=UTF-8")
public class SampleController {

	@Autowired
	private ISampleService sampleService;

	@GetMapping(value = "/list")
	public Map<String, Object> index() {
		Sample sample = new Sample();
		sample.setName(RandomStringUtils.random(20, true, false));

		// RoutingTransaction is readonly = false
		sample = sampleService.insert(sample);

		// Don't setting RoutingTransaction
		// RoutingTransaction is default readonly = false
		Optional<Sample> _sample = sampleService.findById(sample.getId());

		// RoutingTransaction is readonly = true
		List<Sample> samples = sampleService.findAll();

		Map<String, Object> data = Map.of("master", _sample.orElse(null), "slave", samples);

		return data;
	}

}
