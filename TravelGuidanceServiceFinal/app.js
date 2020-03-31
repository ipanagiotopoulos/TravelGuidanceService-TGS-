 
var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var fetch = require('node-fetch');
 
 
// app configurations
app.set("view engine", "ejs");
app.use(express.static("public"));
app.use(express.json());
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: false }));
 
 
// RESTFUL ROUTES
 
app.get("/", function(req, res){
    res.render("index");
});
 
app.get("/offers", function(req, res){
    res.render("offers");
});
 
app.get("/results", function(req, res){
  if(req.query.rainChoice==="notinterested"){
    fetch('http://localhost:8080//Save'+req.query.category ,{
       
        method: "POST",
         
        // Adding body or contents to send
        body: JSON.stringify(
            {
                "name":req.query.name,
                 "age":req.query.age,
                 "city":req.query.currentCity,
                 "preferedWeather":"Sun",
                 "preferedMuseums":req.query.museums,
                 "preferedCafesRestaurantsBars":req.query.cafeBarResto,
                 "preferedCities":req.query.preferedCities
             }
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then((response) => response.json() //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.render("results",{name:data.name,cafeBarResto:data.cafesRestaurantsBars,museums:data.museums});
     
      }).catch((error) => {
          res.render("index");
        console.error('Error:', error);
      });}
      else{
        console.log("edw");
        fetch('http://localhost:8080//Save'+req.query.category+'BasedOnWeather' ,{
       
        method: "POST",
         
        // Adding body or contents to send
        body: JSON.stringify(
            {
                "name":req.query.name,
                 "age":req.query.age,
                 "city":req.query.currentCity,
                 "preferedWeather":"Sun",
                 "preferedMuseums":req.query.museums,
                 "preferedCafesRestaurantsBars":req.query.cafeBarResto,
                 "preferedCities":req.query.preferedCities
             }
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then((response) => response.json() //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.render("results",{name:data.name,cafeBarResto:data.cafesRestaurantsBars,museums:data.museums});
     
      }).catch((error) => {
          res.render("index");
        console.error('Error:', error);
      });
      }
   
});
 
app.get("/administration", function(req, res){
    fetch('http://localhost:8080//All'+req.query.choice)  //req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("travellers",{data:data,typeoftraveller:req.query.choice});
  });
});
app.get("/freeticket",function(req,res){
  fetch('http://localhost:8080//AnyTraveller')  //req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("freeticket",{data:data});
  })
})
app.post("/freeticketprocessing",function(req,res){
 
})
app.post("/freeticketwinner",function(req,res){   //new endpoint  an implementation needed
  // LGTM!
  console.log(req.body.candidateCity);
 // HERE!!!!!!!!!!!
  console.log(req.body.type);
  var response;
  fetch('http://localhost:8080//FreeTicket?city='+req.body.candidateCity,{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
             req.body.type
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then((response) => response.json()  //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
 
      res.render("freeticketwinner",{data:data})
      }).catch((error) => {
        console.error('Error:', error);
      });
});
app.post("/removeTravellers",function(req,res){
  console.log(req.body.id);
  
  fetch('http://localhost:8080//Delete'+req.body.typeoftraveller ,{
       
        method: "POST",
         
        // Adding body or contents to send
        body: JSON.stringify(
          req.body.id //this is a json array
        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then((response) => response.json()  //αυτο το κομμάτι θέλει βελτίωση με !reponse.ok,ώστε να βγάζει status code και μήνυμα στο χρήστη
      )
       
      .then((data) => {
      console.log('Success:', data);
      res.send("Users Deleted");
     
      }).catch((error) => {
        res.send("Fault");
        console.error('Error:', error);
      });
})
app.listen(5000, function() {
    console.log('Server up and running.');
  });