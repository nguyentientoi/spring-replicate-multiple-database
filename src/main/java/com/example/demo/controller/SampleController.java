/**
 * 
 */
package com.example.demo.controller;

import java.util.List;

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
	public List<Sample> index() {
		Sample sample = new Sample();
		sample.setName(RandomStringUtils.random(20, true, false));
		sampleService.insert(sample);

		List<Sample> samples = sampleService.findAll();

		return samples;
	}

}
