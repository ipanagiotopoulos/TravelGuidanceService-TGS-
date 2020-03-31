package com.example.java2.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.java2.Entities.City;
import com.example.java2.Entities.Tourist;
import com.example.java2.Entities.Traveller;
@Repository
public class CityRepository {
	@Autowired MongoTemplate mongoTemplate;
	
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, City.class);
    }
	public City save(City city) {
        mongoTemplate.save(city);
        return city;
    }
	public List<City> findAll() {
	    return mongoTemplate.findAll(City.class);

    }
	public void findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.find(query, City.class);
    }
	public City update(City city){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(city.getId()));
        Update update = new Update();
        update.set("museums", city.getMuseums());
        update.set("cafesRestaurantsBars", city.getCafesRestaurantsBars());
        update.set("weather", city.getWeather());
        update.set("lat", city.getLat());
        update.set("lon", city.getLon());
        return mongoTemplate.findAndModify(query, update, City.class);
    }
}
