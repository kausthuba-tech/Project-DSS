package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Skier;

@Repository
public interface SkierRepository extends MongoRepository<Skier, String>{

	Skier findByResortID(String resortID);
	
	@Query(value = "{'resortID' : ?0, 'seasonID' : ?1, 'dayID' :?2}", fields = "{'time':1, '_id':0}")
	List<String> findTimesByResortIDAndSeasonIDAndDayID(String resortID, String seasonID, String dayID);
	
	@Query(value = "{'resortID' : ?0, 'seasonID' : ?1, 'dayID' :?2, 'skierID' :?3}", fields = "{'vertical':1, '_id':0}")
	String findVerticalByResortIDAndSeasonIDAndDayIDAndSkierID(String resortID, String seasonID, String dayID, String skierID);
	
	Skier findSkierBySkierID(String skierID);

	@Query(value = "{'skierID': ?0 , resortID' : ?1}", fields = "{'vertical':2, seasonID':1, '_id':0}")
	Skier findSkierBySkierIDAndResortID(String skierID, String resortID);
	
	
	

}
