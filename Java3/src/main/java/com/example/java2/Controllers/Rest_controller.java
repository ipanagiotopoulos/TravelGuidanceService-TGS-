package com.example.java2.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.example.java2.Entities.Country;
import com.example.java2.Entities.Tourist;
import com.example.java2.Entities.Traveller;
import com.example.java2.Repositories.BusinessRepository;
import com.example.java2.Repositories.CityRepository;
import com.example.java2.Repositories.CountryRepository;
import com.example.java2.Repositories.TouristRepository;
import com.example.java2.Repositories.TravellerRepository;
import com.example.java2.RetrieveData.OpenData;
import com.example.java2.security.WebSecurityConfig;
@CrossOrigin(origins = "*", maxAge = 3600)


@Import(WebSecurityConfig.class)
@RestController
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RequestMapping("/web/api")
public class Rest_controller {
	public static List<City> listofcities;
	@Autowired CityRepository cr;
	@Autowired CountryRepository countryr;
	@Autowired TravellerRepository tr;
	@Autowired BusinessRepository br;
	@Autowired TouristRepository tour;
	
	@RequestMapping(value = "/SaveTraveller", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	@PreAuthorize("hasRole('USER')")
	public City CreateTraveller(@RequestBody Traveller traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		ArrayList<Country> countries=new ArrayList<Country>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		Country countryFound=null;
		ArrayList<City> citytmp=new ArrayList<>();
		System.out.println("hereeeeeee"+cities.size());
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
			 if(countryr.findById(cities.get(i).getCountryName()).size()>0){
				System.out.println("hehehey");
			   countryFound=countryr.findById(cities.get(i).getCountryName()).get(0);
				 countryFound.setCities(cities);
				 countryr.update(countryFound);
			 }
			 else {
				 System.out.println("hehehey"+cities.get(i));
				 citytmp.add(cities.get(i));
				 countryFound.setCities(citytmp);
				 citytmp=null;
				 countryFound.setCountryName(cities.get(i).getCountryName());
				 countryr.save(countryFound);
			 }
				 		 
		}
		
        String visit=traveller.CompareCities(cities);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CityCurrentlyLivingString=traveller.getCity();
        City CityCurrentlyLiving=OpenData.RetrieveData(CityCurrentlyLivingString,1,0);
        cr.save(CityCurrentlyLiving);
        Country CountryCurrentlyLiving=null;
        String CountryCurrentlyLivingString=traveller.getCountryName();
        if(countryr.findById(traveller.getCountryName())!=null){
         CountryCurrentlyLiving=countryr.findById(traveller.getCountryName()).get(0);
         CountryCurrentlyLiving.setTimeslived(1);
         countryr.update(CountryCurrentlyLiving);
        }
        else {
        
        ArrayList cityliving=new ArrayList<>();
        cityliving.add(CityCurrentlyLiving);
        CountryCurrentlyLiving=new Country(traveller.getCountryName(),null);
        CountryCurrentlyLiving.setCities(cityliving);
        CountryCurrentlyLiving.setTimeslived(1);
        countryr.save(CountryCurrentlyLiving);
        }
        tr.save(traveller);
       
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTravellerBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTravellerBasedOnWeather(@RequestBody Traveller traveller) throws IOException {
		System.out.println(traveller);
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CurrentlyLivingString=traveller.getCity();
        City CurrentlyLiving=OpenData.RetrieveData(CurrentlyLivingString,1,0);
        cr.save(CurrentlyLiving);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/FreeTicket", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public Traveller FreeTicket(@RequestBody String[] ids,@RequestParam(name="city",required=false) String CityString) {
	   Traveller travellerfound=null;
	   City city=null;
	try {
		city = OpenData.RetrieveData(CityString,0,0);
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
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveBusiness", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateBusiness(@RequestBody Business traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CurrentlyLivingString=traveller.getCity();
        City CurrentlyLiving=OpenData.RetrieveData(CurrentlyLivingString,1,0);
        cr.save(CurrentlyLiving);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	//@RequestMapping(value = "/GetSessions", method = RequestMethod.GET, produces = { "application/json",
	//"application/xml" })
	//public String GetSearchSessions(){
		
//	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveBusinessBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateBusinessBasedOnWeather(@RequestBody Business traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
			cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CurrentlyLivingString=traveller.getCity();
        City CurrentlyLiving=OpenData.RetrieveData(CurrentlyLivingString,1,0);
        cr.save(CurrentlyLiving);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTourist", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTourist(@RequestBody Tourist traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
		   cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CurrentlyLivingString=traveller.getCity();
        City CurrentlyLiving=OpenData.RetrieveData(CurrentlyLivingString,1,0);
        cr.save(CurrentlyLiving);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/SaveTouristBasedOnWeather", method = RequestMethod.POST, produces = { "application/json",
	"application/xml" })
	public City CreateTouristBasedOnWeather(@RequestBody Tourist traveller) throws IOException {
		ArrayList<City> cities= new ArrayList<City>();
		for (int i=0;i<traveller.preferedCities.size(); i++) {
			String city=traveller.preferedCities.get(i);
			String [] arrofStr=city.split(",");
		   cities.add(OpenData.RetrieveData(arrofStr[0],0,0));
		}
		for (int i=0;i<cities.size(); i++) {
			 cr.save(cities.get(i));
		}
        String visit=traveller.CompareCities(cities,true);
        City visitCity=OpenData.RetrieveData(visit,0,1);
        traveller.setVisit(visit);
        String CurrentlyLivingString=traveller.getCity();
        City CurrentlyLiving=OpenData.RetrieveData(CurrentlyLivingString,1,0);
        cr.save(CurrentlyLiving);
        tr.save(traveller);
        System.out.println(tr.findTravellerByName(traveller.getName()));
		return visitCity;
	}
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/DeleteTourist/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	"application/xml"})
	public String DeleteTourist(@PathVariable("id") String id) {
	  tr.deleteById(id);
	  return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/DeleteCity/{id}",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteCityById(@PathVariable("id") String id) {
	System.out.println(id);
	  cr.deleteById(id);
	  return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/DeleteTravellers",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteTravellers(@RequestBody String[] ids) {
	for(String string:ids) {
		tr.deleteById(string);
	}
	return "Items Deleted";
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/DeleteTourists",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteTourists(@RequestBody String[] ids) {
		System.out.println("hit delete tourists");
		for(String string:ids) {
			tour.deleteById(string);
		}
	return "Items Deleted";
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/DeleteBusiness",method = RequestMethod.POST,produces={ "application/json",		
	"application/xml"})
	public String DeleteBusiness(@RequestBody String[] ids) {
		for(String string:ids) {
			br.deleteById(string);
		}
	return "Items Deleted";	
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/UpdateCity/{id}",method = RequestMethod.DELETE,produces={ "application/json",		
	"application/xml"})
	public String UpdateCity(@PathVariable("name") String id,@RequestBody City city) {
		cr.update(city);
		return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/UpdateTraveller/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateTraveller(@PathVariable("id") String id,@RequestBody Traveller traveller) {
		traveller.setId(id);
		tr.update(traveller);
		return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/UpdateBusiness/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateBusiness(@PathVariable("id") String id,@RequestBody Business traveller) {
		traveller.setId(id);
		br.update( traveller);
		return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/UpdateTourist/{id}",method = RequestMethod.PUT,produces={ "application/json",		
	"application/xml"})
	public String UpdateBusiness(@PathVariable("id") String id,@RequestBody Tourist traveller) {
		traveller.setId(id);
		tour.update( traveller);
		return null;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/AllTravellers",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Traveller> AllTravellers() {
		return tr.findAll();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/AllBusiness",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Business> AllBusiness() {
		return br.findAll();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/AllTourists",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Tourist> AllTourist() {
		return tour.findAll();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/AnyTraveller",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<Traveller> AnyTraveller() {
	    List<Traveller> trl=new ArrayList();
		trl.addAll(tr.findAll());
		trl.addAll((List<Traveller>)((List<?>) br.findAll()));
		trl.addAll((List<Traveller>)((List<?>) tour.findAll()));
		return  trl;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/AllCities",method = RequestMethod.GET,produces={ "application/json",		
	"application/xml"})
	public List<City> AllCities() {
		return cr.findAll();
	}
}
