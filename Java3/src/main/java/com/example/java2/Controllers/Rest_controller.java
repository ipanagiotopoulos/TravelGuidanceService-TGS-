package com.example.java2.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.files.TravellerRecordsJson;
import com.example.java2.Entities.Business;
import com.example.java2.Entities.City;
import com.example.java2.Entities.Tourist;
import com.example.java2.Entities.Traveller;
import com.example.java2.Repositories.BusinessRepository;
import com.example.java2.Repositories.CityRepository;
import com.example.java2.Repositories.TouristRepository;
import com.example.java2.Repositories.TravellerRepository;
import com.example.java2.RetrieveData.OpenData;

@RestController
public class Rest_controller {
	public static List<City> listofcities;
	@Autowired CityRepository cr;
	@Autowired TravellerRepository tr;
	@Autowired BusinessRepository br;
	@Autowired TouristRepository tour;
	@RequestMapping(value = "/SaveTraveller", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTraveller(@RequestBody Traveller traveller) throws IOException {
	   List<Traveller> travellers=new ArrayList<Traveller>();
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities);
        City visitCity=OpenData.RetrieveData(visit);
        traveller.setVisit(visit);
        TravellerRecordsJson.SaveTravellers(travellers);
        tr.save(traveller);
        travellers.add(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@RequestMapping(value = "/SaveTravellerBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTravellerBasedOnWeather(@RequestBody Traveller traveller) throws IOException {
		System.out.println(traveller);
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        City visitCity=OpenData.RetrieveData(visit);
        traveller.setVisit(visit);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	
	@RequestMapping(value = "/FreeTicket", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public Traveller FreeTicket(@RequestBody String[] ids,@RequestParam(name="city",required=false) String CityString) {
	   Traveller travellerfound=null;
	   City city=null;
	try {
		city = OpenData.RetrieveData(CityString);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	List<Traveller> TravellerstoBeCompared=new ArrayList<Traveller>();
	Traveller temp;
	List<Traveller> traveller;
	List<Tourist> tourists;
	List<Business> business;
	  for(String id:ids) {
		  if((traveller=tr.findById(id)).size()>0) {
			  temp=traveller.get(0);
			  System.out.println(temp);
			  TravellerstoBeCompared.add(temp);
		  }
		  else if((tourists=tour.findById(id)).size()>0) {
			  temp=tourists.get(0);
			  System.out.println(temp);
			  TravellerstoBeCompared.add(temp);
		  }
		  else if((business= br.findById(id)).size()>0) {
			  temp=business.get(0);
			  System.out.println(temp);
			  TravellerstoBeCompared.add(temp);
		  }
		  else {
			 
		  }
	  }
			travellerfound=city.FreeTicket(TravellerstoBeCompared);
		  return travellerfound;
	}
	@RequestMapping(value = "/SaveBusiness", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateBusiness(@RequestBody Business traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities);
        City visitCity=OpenData.RetrieveData(visit);
        traveller.setVisit(visit);
        tr.save(traveller);
		return visitCity;
	}
	@RequestMapping(value = "/SaveBusinessBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateBusinessBasedOnWeather(@RequestBody Business traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        City visitCity=OpenData.RetrieveData(visit);
        traveller.setVisit(visit);
        tr.save(traveller);
		return visitCity;
	}
	@RequestMapping(value = "/SaveTourist", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTourist(@RequestBody Tourist traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
		   cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities);
        traveller.setVisit(visit);
        City visitCity=OpenData.RetrieveData(visit);
        tour.save(traveller);
		return visitCity;
	}
	@RequestMapping(value = "/SaveTouristBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTouristBasedOnWeather(@RequestBody Tourist traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
		   cities.add(OpenData.RetrieveData(arrofStr[0]));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        traveller.setVisit(visit);
        City visitCity=OpenData.RetrieveData(visit);
        tour.save(traveller);
		return visitCity;
	}
	
	@RequestMapping(value="/DeleteTraveller/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	"application/xml"})
	public String DeleteTraveller(@PathVariable("id") String id) {
	  tr.deleteById(id);
	  return "User Deleted";
	}
	//@RequestMapping(value="/DeleteBusiness/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	//"application/xml"})
	//public String DeleteBusiness(@PathVariable("id") String id) {
	 // tr.deleteById(id);
	  
	  //return "User Deleted";
	//}
	@RequestMapping(value="/DeleteTourist/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	"application/xml"})
	public String DeleteTourist(@PathVariable("id") String id) {
	  tr.deleteById(id);
	  return null;
	}
	@RequestMapping(value="/DeleteCity/{id}",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteCityById(@PathVariable("id") String id) {
	System.out.println(id);
	  cr.deleteById(id);
	  return null;
	}
	@RequestMapping(value="/DeleteTravellers",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteTravellers(@RequestBody String[] ids) {
	for(String string:ids) {
		tr.deleteById(string);
	}
	return "Items Deleted";
	}
	@RequestMapping(value="/DeleteTourists",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteTourists(@RequestBody String[] ids) {
		System.out.println("hit delete tourists");
		for(String string:ids) {
			tour.deleteById(string);
		}
	return "Items Deleted";
	}
	@RequestMapping(value="/DeleteBusiness",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteBusiness(@RequestBody String[] ids) {
		for(String string:ids) {
			br.deleteById(string);
		}
	return "Items Deleted";	
	}
	@RequestMapping(value="/UpdateCity/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	"application/xml"})
	public String UpdateCity(@PathVariable("id") String id,@RequestBody City city) {
		city.setId(id);
		cr.update(city);
		return null;
	}
	@RequestMapping(value="/UpdateTraveller/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateTraveller(@PathVariable("id") String id,@RequestBody Traveller traveller) {
		traveller.setId(id);
		tr.update(traveller);
		return null;
	}
	@RequestMapping(value="/UpdateBusiness/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateBusiness(@PathVariable("id") String id,@RequestBody Business traveller) {
		traveller.setId(id);
		br.update( traveller);
		return null;
	}
	@RequestMapping(value="/UpdateTourist/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateBusiness(@PathVariable("id") String id,@RequestBody Tourist traveller) {
		traveller.setId(id);
		tour.update( traveller);
		return null;
	}
	@RequestMapping(value="/AllTravellers",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Traveller> AllTravellers() {
		return tr.findAll();
	}
	@RequestMapping(value="/AllBusiness",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Business> AllBusiness() {
		return br.findAll();
	}
	@RequestMapping(value="/AllTourists",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Tourist> AllTourist() {
		return tour.findAll();
	}
	@RequestMapping(value="/AnyTraveller",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Traveller> AnyTraveller() {
	    List<Traveller> trl=new ArrayList();
		trl.addAll(tr.findAll());
		trl.addAll((List<Traveller>)((List<?>) br.findAll()));
		trl.addAll((List<Traveller>)((List<?>) tour.findAll()));
		return  trl;
	}
	@RequestMapping(value="/AllCities",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<City> AllCities() {
		return cr.findAll();
	}
}
