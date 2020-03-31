package com.example.java2.Entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class City {
	public static ArrayList<City> CitiesSession;
	@Id
    public String id;
	public City(String id, int museums, String name, int cafesRestaurantsBars, String weather, double lat, double lon) {
		super();
		this.id = id;
		this.museums = museums;
		this.name = name;
		this.cafesRestaurantsBars = cafesRestaurantsBars;
		this.weather = weather;
		this.lat = lat;
		this.lon = lon;
	}
	public String getName() {
		
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Indexed
	private int museums;
	private String name;
	private int cafesRestaurantsBars;
	private String weather;
	private double lat;
	private double lon;
	

	public int getMuseums() {
		return museums;
	}

	public void setMuseums(int museums) {
		this.museums = museums;
	}

	public int getCafesRestaurantsBars() {
		return cafesRestaurantsBars;
	}

	public void setCafesRestaurantsBars(int cafesRestaurantsBars) {
		this.cafesRestaurantsBars = cafesRestaurantsBars;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Traveller FreeTicket(List<Traveller> travellers) {
		System.out.println("size"+travellers.size());
        Traveller travellerFound = null;
        Business businessFound=null;
        float maxSimilarity=0.0f;
        float maxSimilarityb=0.0f;
        float prevsimilarity=0.0f;
        ArrayList<Traveller> similaritypoints=new ArrayList<Traveller>();
       for(Traveller traveller:travellers) {
           if(!( traveller instanceof Business && traveller.getClass()==Business.class)){
           if(traveller.Similarity(this,traveller)>=maxSimilarity) {
               maxSimilarity=traveller.Similarity(this,traveller);
               travellerFound=traveller;
               if(traveller.Similarity(this,traveller)==maxSimilarity) {
               if(maxSimilarity==prevsimilarity) {
                    similaritypoints.add(traveller);
               }
               else {
                   prevsimilarity=traveller.Similarity(this,traveller);
                   similaritypoints.removeAll(similaritypoints);
                   similaritypoints.add(traveller);
               }
               }
       }
           }
           else {
               if(traveller.Similarity(this,traveller)>=maxSimilarityb) {
            	   maxSimilarityb=traveller.Similarity(this,traveller);
                   businessFound=(Business) traveller;
               }
           }
       }
           if(businessFound!=null) {
           similaritypoints.add(businessFound);
           }
           float maxdist=0.0f;
           if(similaritypoints.size()>0) {
           for(Traveller Travellers:similaritypoints) {
               if(maxdist<DistanceCalculator.distance(this.getLat(),this.getLon(),Travellers.getCurrentlat(),Travellers.getCurrentlon(),"K") ) {
                   maxdist=(float) DistanceCalculator.distance(this.getLat(),this.getLon(),Travellers.getCurrentlat(),Travellers.getCurrentlon(),"K");
                   travellerFound=Travellers;
               }
           }
   }
           return travellerFound;
      
  
   }
	/*@Override
	public boolean equals(Object o) {
		 if (o == this) { 
	            return true; 
	        } 
		 if (!(o instanceof City)) { 
			 
	            return false; 
	        } 
		 else {
			 City onew=(City) o;
			 Collections.sor	
			 if(CitiesSession.stream().map(City::name).filter(onew.getName()::equals).findFirst().isPresent()==true) {
				 return true;
			 }	
			 else {
				 CitiesSession.add(onew);
				 return false;
			 }
		 }
	}*/
	@Override
	public String toString() {
		return "City [id=" + id + ", museums=" + museums + ", name=" + name + ", cafesRestaurantsBars="
				+ cafesRestaurantsBars + ", weather=" + weather + ", lat=" + lat + ", lon=" + lon + "]";
	}
	
}

	
	
	
	

