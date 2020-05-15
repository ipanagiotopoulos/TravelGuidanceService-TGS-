 
var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var fetch = require('node-fetch');
var localStorage = require('localStorage');
var jwt = require('jwt-simple');
 
// app configurations
app.set("view engine", "ejs");
app.use(express.static("public"));
app.use(express.json());
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: false }));
var jwt = require('jsonwebtoken');
// RESTFUL ROUTES



app.get("/", function(req, res){
    res.render("login");
});
app.post("/login",function(req,res){   //new endpoint  an implementation needed
  // LGTM!
  //console.log(req.body);
 // HERE!!!!!!!!!!!
  var role;
  var name;
  fetch('http://localhost:8080/web/auth/signin',{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
          {
             "username":req.body.username,
              "password":req.body.password
          }

        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then(response => {
        response.json()
          .then(responseJson => {
           
             var decoded = jwt.decode(responseJson.accessToken, 'z97r#s');
             if(decoded!=null){
             role=decoded.role[0]["authority"];
          
            localStorage.setItem('token', responseJson.accessToken);
            if( role==="ROLE_USER"){
              res.render("index.ejs")
              }
              else if(role==="ROLE_ADMIN"){
                res.render("adminindex.ejs")
              }
            else{
                res.render("login.ejs");
              }
            }
            else{
              res.render("login.ejs");
            }
            //localStorage.setItem('token', responseJson.accessToken)
            // set localStorage with your preferred name,..
            // ..say 'my_token', and the value sent by server
         
            //console.log(localStorage);
            
            // you may also want to redirect after you have saved localStorage:
            // window.location.assign("http://www.example.org")
          })
      })
      .then((data) => {
        
      }).catch((error) => {
        console.error('Error:', error);
      });
});
app.get("/logedin", function(req, res) { 
  res.render("index"); 
}); 
app.post("/logout",function(req,res){

});
app.get("/admin",function(req,res) {
res.render("adminindex.ejs");
});

app.get("/register",function(req,res){
  res.render("register");
});
app.post("/registered",function(req,res){
  var message;
  fetch('http://localhost:8080/web/auth/signup',{
       
        method: "POST",
 
        // Adding body or contents to send
        body: JSON.stringify(
          {
             "username":req.body.email[0],
             "roles":["ROLE_USER"],
             "password":req.body.password,
             "email":req.body.email[1]
          }

        ),
         
        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
      })
      .then(response => {
        response.json()
          .then(responseJson => {
            console.log(responseJson);

            var decoded = jwt.decode(responseJson.accessToken, 'z97r#s');
            console.log(decoded);
            message=responseJson.message;
            var infosuccess={
              registrymessage:message
            }
             
           localStorage.setItem('token', responseJson.accessToken);
           if(message==="User registered successfully!"){
            res.render("index.ejs",{message:message});
             }
           else{
            res.send({message:infosuccess});
             }
           
            // set localStorage with your preferred name,..
            // ..say 'my_token', and the value sent by server
            
            
            // you may also want to redirect after you have saved localStorage:
            // window.location.assign("http://www.example.org")
          })
      })
      .then((data) => {
      
      }).catch((error) => {
        console.error('Error:', error);
      });
});


app.get("/information",function(req,res){});


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
        fetch('http://localhost:8080/Save'+req.query.category+'BasedOnWeather' ,{
       
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
    fetch('http://localhost:8080/All'+req.query.choice)  //req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("travellers",{data:data,typeoftraveller:req.query.choice});
  });
});
app.get("/freeticket",function(req,res){
  var token = localStorage.getItem('token');
  console.log(`Authorization=Bearer ${token}`)
  let headers = {"Content-Type": "application/json"};
    if (token) {
      headers["Authorization"] = ` Bearer ${token}`;
    }
    console.log( headers)
  fetch('http://localhost:8080/AnyTraveller',{
    headers: {
    "Content-type": "application/json; charset=UTF-8",
    "Authorization": ` Bearer ${token}`
    }
  })//req.query.choice should be Traveller,Tourist or Business.
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
  fetch('http://localhost:8080/FreeTicket?city='+req.body.candidateCity,{
       
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
  
  fetch('http://localhost:8080/Delete'+req.body.typeoftraveller ,{
       
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