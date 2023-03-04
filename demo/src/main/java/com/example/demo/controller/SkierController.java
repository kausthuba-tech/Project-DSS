package com.example.demo.controller;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Skier;
import com.example.demo.repository.SkierRepository;
import com.example.demo.model.*;

import java.util.Arrays;
import java.util.List;

import javax.management.AttributeNotFoundException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class SkierController {

	@Autowired
	SkierRepository skierRepository;

	@GetMapping("/resorts")
	public ResponseEntity<List<Skier>> getResorts() {
		return ResponseEntity.ok(this.skierRepository.findAll());
	}

	@GetMapping("/resorts/{resortID}/seasons")
	public ResponseEntity<List<String>> getSeasonsByResortID(@PathVariable String resortID)
			throws AttributeNotFoundException {
		Skier skier = skierRepository.findByResortID(resortID);
		if (skier == null) {
			// Return an error response
			throw new AttributeNotFoundException("Resort or skier not found");
		}

		return ResponseEntity.status(200).body(skier.getSeasonID());
	}

	@PostMapping("/resorts/{resortID}/seasons")
	public ResponseEntity<Skier> createSeason(@PathVariable String resortID, @RequestBody SeasonRequest seasons) {
		Skier skier = skierRepository.findByResortID(resortID);
		List<String> seasonsInResort = skier.getSeasonID();
		seasonsInResort.add(seasons.getYear());
		skier.setSeasonID(seasonsInResort);
		System.out.println(skier.getSeasonID() + "Aafter the change");
		return ResponseEntity.status(201).body(this.skierRepository.save(skier));
	}

	// @GetMapping("/product/{id}")
	// public ResponseEntity getProductById(@PathVariable String id) {
	// Optional<Product> product = this.productRepository.findById(id);
	// if(product.isPresent()) {
	// return ResponseEntity.ok(product.get());
	// } else {
	// return ResponseEntity.ok("The product with id: " + id + " was not found.");
	// }

	/**
	 * @param resortID
	 * @param seasonID
	 * @param dayID
	 * @param skierID
	 * @return
	 */
	@PostMapping("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
	public ResponseEntity<Skier> createRandomLiftEvent(@PathVariable String resortID, @PathVariable String seasonID,
			@PathVariable String dayID, @PathVariable String skierID, @RequestBody LiftRequest liftRequest) {
		System.out.println(
				"here at the controller"
		);
		Skier skier = new Skier();
		skier.setResortID(resortID);
		skier.setSeasonID(Arrays.asList(seasonID));
		skier.setDayID(dayID);
		skier.setSkierID(skierID);
		//skier.setLiftID(String.valueOf((1 + (int) Math.random() * ((40 - 1) + 1))));
		System.out.println(liftRequest.getLiftID() + "lift request ID");
		skier.setLiftID(liftRequest.getLiftID());
		skier.setTime(liftRequest.getTime());
		skier.setVertical("234");
		System.out.println("here to create the repo");
		return ResponseEntity.status(201).body(this.skierRepository.save(skier));

	}

	@GetMapping("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers")
	public ResponseEntity<String> getResortsbyIdSeasonAndDay(@PathVariable String resortID, @PathVariable String seasonID,
			@PathVariable String dayID) {
		List<String> times = skierRepository.findTimesByResortIDAndSeasonIDAndDayID(resortID, seasonID, dayID);
		long totalSkiers = times.size();
		
		String response = "{"
				+ "times:" + times + ", numOfSkiers:" + totalSkiers + "}"; 
		return ResponseEntity.status(200).body(response);
		
	}
	
	@GetMapping("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
	public ResponseEntity<String> getVerticalbyResortIdSeasonAndDaySkier(@PathVariable String resortID, @PathVariable String seasonID,
			@PathVariable String dayID, @PathVariable String skierID) {
		String vertical = skierRepository.findVerticalByResortIDAndSeasonIDAndDayIDAndSkierID(resortID, seasonID, dayID, skierID);
		
		return ResponseEntity.status(200).body(vertical);
		
	}
	
	@GetMapping("/skiers/{skeirID}/vertical")
	public ResponseEntity<Skier> getTotalVerticalBySkiers(@PathVariable String skierID, @RequestBody ResortRequest resortRequest){
		Skier skier = skierRepository.findSkierBySkierIDAndResortID(skierID, resortRequest.getResortID());
		return ResponseEntity.status(200).body(skier);
	}
	
	
	

}