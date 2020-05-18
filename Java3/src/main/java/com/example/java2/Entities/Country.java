package com.example.java2.Entities;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Country {
@Id
private String CountryName;
private ArrayList<City> Cities;
private int timesvisited;
private int timeslived;
public Country(String countryName, City city) {
	CountryName = countryName;
	Cities.add(city);
	this.timesvisited =0;
	this.timeslived =0;
}
public String getCountryName() {
	return CountryName;
}
public void setCountryName(String countryName) {
	CountryName = countryName;
}
public ArrayList<City> getCities() {
	return Cities;
}
public void setCities(ArrayList<City> cities) {
	for (City iteratorcity:cities) {
		if(this.Cities.contains(iteratorcity)) {
			
		}
		else {
			Cities.add(iteratorcity);
		}
	}
}
public int getTimesvisited() {
	return timesvisited;
}
public void setTimesvisited(int timesvisited) {
	this.timesvisited += timesvisited;
}
public int getTimeslived() {
	return timeslived;
}
public void setTimeslived(int timeslived) {
	this.timeslived += timeslived;
}




}
