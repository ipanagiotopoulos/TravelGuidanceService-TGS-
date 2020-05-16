 
var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var fetch = require('node-fetch');
const cookieParser = require('cookie-parser');
var localStorage = require('localStorage');
var jwt = require('jwt-simple');
var  middleware=require('./authentication-middleware')
var jwt = require('jsonwebtoken');

const exjwt = require('express-jwt');
// app configurations
app.set("view engine", "ejs");
app.use(express.static("public"));
app.use(express.json());
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser())

// RESTFUL ROUTES
const secret="z97r#s"
var infosuccess;
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', 'http://localhost:8080');
  res.setHeader('Access-Control-Allow-Headers', 'Content-type,Authorization');
  next();
});


app.get("/", function(req, res){
    res.render("login.ejs",{message:null});
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
              username=decoded.username;
             if(decoded!=null){
               payload={username};
               const token=jwt.sign(payload,secret);
              res.cookie('username', responseJson.username);
              res.cookie('role', decoded.role[0]["authority"]);
              res.cookie('token', token, { httpOnly: true});
              
              role=decoded.role[0]["authority"];
          
             localStorage.setItem('token', responseJson.accessToken);
            if( role==="ROLE_USER"){
              res.render("index.ejs")
              }
              else if(role==="ROLE_ADMIN"){
                res.render("adminindex.ejs")
              }
            else{
                res.render("login.ejs",{message:"Deactivated!"});
              }
            }
            else{
              res.render("login.ejs",{message:"Username or password incorrect!"});
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
app.get("/logedin",middleware, function(req, res) { 
  res.render("index"); 
}); 
app.get("/logout",middleware  ,function(req,res){
  cookie = req.cookies;
  for (var prop in cookie) {
      if (!cookie.hasOwnProperty(prop)) {
          continue;
      }    
      res.cookie(prop, '', {expires: new Date(0)});
  }
  res.render("login.ejs",{message:"Succesfully loged out!"});
});

app.get("/admin",middleware,function(req,res) {
res.render("adminindex.ejs");
});

app.get("/register",function(req,res){
  infosuccess={
    registrymessage:null
  }
  res.render("register",{message:infosuccess});
});
app.post("/registered",middleware,function(req,res){
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
            infosuccess={
              registrymessage:message
            }
             
           localStorage.setItem('token', responseJson.accessToken);
           if(message==="User registered successfully!"){
            res.render("index.ejs",{message:message});
             }
           else{
            res.render("register.ejs",{message:infosuccess});
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


app.get("/offers", middleware  ,function(req, res){
    res.render("offers");
});
 
app.get("/results",middleware , function(req, res){
  if(req.query.rainChoice==="notinterested"){
    fetch('http://localhost:8080/web/api/Save'+req.query.category ,{
       
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
        fetch('http://localhost:8080/web/api/Save'+req.query.category+'BasedOnWeather' ,{
       
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
             },
             {
             "username":cookies.username
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
 
app.get("/administration", middleware,function(req, res){
  if(req.cookies.role[0]["authority"]==="ROLE_ADMIN"){
    fetch('http://localhost:8080/web/api/All'+req.query.choice)  //req.query.choice should be Traveller,Tourist or Business.
  .then((response) => {
    return response.json();
  })
  .then((data) => {
    console.log(data);
    res.render("travellers",{data:data,typeoftraveller:req.query.choice});
  });
}
else{
  res.send("Not admin");
}
});
app.get("/freelotterysubmission",function(req,res){
  res.render("UserLotteryDestinationSubmission.ejs");
})
app.get("/freeticket", middleware ,function(req,res){
  var token = localStorage.getItem('token');
  console.log(`Authorization=Bearer ${token}`)
  let headers = {"Content-Type": "application/json"};
    if (token) {
      headers["Authorization"] = ` Bearer ${token}`;
    }
    console.log( headers)
  fetch('http://localhost:8080/web/api/AnyTraveller',{
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
app.post("/freeticketprocessing", middleware ,function(req,res){
 
})
app.post("/freeticketwinner", middleware ,function(req,res){   //new endpoint  an implementation needed
  // LGTM!
  console.log(req.body.candidateCity);
 // HERE!!!!!!!!!!!
  console.log(req.body.type);
  var response;
  fetch('http://localhost:8080/web/api/FreeTicket?city='+req.body.candidateCity,{
       
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
app.post("/removeTravellers",middleware,function(req,res){
  console.log(req.body.id);
  
  fetch('http://localhost:8080/web/api/Delete'+req.body.typeoftraveller ,{
       
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

app.get('/checkToken', middleware, function(req, res) {
  res.sendStatus(200);
});

app.listen(5000, function() {
    console.log('Server up and running.');
  });