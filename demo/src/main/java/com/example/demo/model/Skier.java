package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Skier {
	@Id
	private String skierID;

	@Field
	private String resortID;
	
	@Field
	private String liftID;

	@Field
	private List<String> seasonID;

	@Field
	private String dayID;

	@Field
	private String time;
	
	@Field
	private String vertical;

	public Skier() {}
	
	

	public Skier(String skierID, String resortID, String liftID, List<String> seasonID, String dayID, String time) {
		this.skierID = skierID;
		this.resortID = resortID;
		this.liftID = liftID;
		this.seasonID = seasonID;
		this.dayID = dayID;
		this.time = time;
	}

	public String getSkierID() {
		return skierID;
	}

	public String getResortID() {
		return resortID;
	}

	public String getLiftID() {
		return liftID;
	}

	public List<String> getSeasonID() {
		return seasonID;
	}

	public String getDayID() {

		return dayID;
	}

	public String getTime() {
		return time;
	}

	public void setSkierID(String skierID) {
		this.skierID = skierID;
	}

	public void setResortID(String resortID) {
		this.resortID = resortID;
	}

	public void setLiftID(String liftID) {
		this.liftID = liftID;
	}

	public void setSeasonID(List<String> seasonsInResort) {
		this.seasonID = seasonsInResort;
	}

	public void setDayID(String dayID) {
		this.dayID = dayID;
	}

	public void setTime() {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Skier{" +
				"skierID='" + skierID + '\'' +
				", resortID='" + resortID + '\'' +
				", liftID='" + liftID + '\'' +
				", seasonID='" + seasonID + '\'' +
				", dayID='" + dayID + '\'' +
				", time='" + time + '\'' +
				'}';
	}

	public void setTime(String time) {
		// TODO Auto-generated method stub
		this.time = time;
		
	}



	public String getVertical() {
		return vertical;
	}



	public void setVertical(String vertical) {
		this.vertical = vertical;
	}


}
