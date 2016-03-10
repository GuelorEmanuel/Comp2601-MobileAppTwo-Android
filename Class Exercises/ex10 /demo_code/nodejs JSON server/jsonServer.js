/*
Basic Node.js server using only built-in Node.js capabilities
(No extra NPM modules like "connect" or "express" being used.)
*/

/*
//collaboration through polling
//=============================

This server is polled using an HTTP messages with path /pollData.
It responds with a JSON object containing an array of all the words
maintained by this server and their locations.

Clients can request to add words or ask for them to be moved slightly
left, right, up, or down.

API:
All API methods respond with JSON array containing all current word objects
maintained by server

GET methods:
/pollData

/add?word=Bird

/move?word=Bird&direction=right

POST, PUT methods:

/pollData

/add
data: {word: "Bird"}

/move
data: {word: "Bird", direction:"right"}


This polling app is a great candidate to use web sockets instead of polling.
*/

//Cntl+C to stop server (in Windows, Mac CMD console)

var http    = require('http'); //need for http
var fs      = require('fs'); //needed to read static files
var url     = require('url');  //needed to parse url strings
var colour  = require('colour');  // needed for changing terminal colour

colour.setTheme({
  silly: 'rainbow',
  input: 'grey',
  verbose: 'cyan',
  prompt: 'grey',
  data: 'grey',
  help: 'cyan',
  warn: ['yellow', 'underline'], // Applies two styles at once
  debug: 'blue',
  error: 'red bold', // Again, two styles
  get: 'green',
  post: 'red',
  put: 'yellow'
});


//Hard coded size of client window -should be done better
var canvasWidth = 600; //hard code expected client height
var canvasHeight = 300; //hard coded expected client width

//words and locations maintained by server
var words = [];
words.push({word: "Louis", x: 100, y: 200});
words.push({word: "Sean", x: 200, y: 250});


var ROOT_DIR = 'html'; //dir to serve static files from

var MIME_TYPES = {
    'css': 'text/css',
    'gif': 'image/gif',
    'htm': 'text/html',
    'html': 'text/html',
    'ico': 'image/x-icon',
    'jpeg': 'image/jpeg',
    'jpg': 'image/jpeg',
    'js': 'text/javascript', //should really be application/javascript
    'json': 'application/json',
    'png': 'image/png',
    'txt': 'text/text'
};

var get_mime = function(filename) {
    //return mime type based on file extension
    //e.g. return 'html' for path request ending in .html
    var ext, type;
    for (ext in MIME_TYPES) {
        type = MIME_TYPES[ext];
        if (filename.indexOf(ext, filename.length - ext.length) !== -1) {
            return type;
        }
    }
    return MIME_TYPES['txt'];
};

function addWord(aWordString){

    //add aWordString to the words collection if it is not already there
    if(aWordString == null || aWordString.length == 0) return;

    for(var i=0; i<words.length; i++){
       if(words[i].word == aWordString) return; //already there
    }
    //add word at random location
    words.push({word: aWordString,
                   x: 20 + Math.floor(Math.random()*(canvasWidth -40)),
                   y: 20 + Math.floor(Math.random()*(canvasHeight - 40))});


}

function moveWord(aWord, aDirection){
   //direction should be one of left, right, up, down
   //Exercise: change this so words stay entirely within canvas area
   console.log("MOVE THE WORD");
   var increment = 10;
   for(var i=0; i<words.length; i++) {
        var w = words[i];
        if(w.word == aWord){
          if(aDirection == "right" && w.x <= canvasWidth - increment) w.x += increment;
          if(aDirection == "left" && w.x >= increment) w.x -= increment;
          if(aDirection == "up" && w.y >= increment) w.y -= increment;
          if(aDirection == "down" && w.y <= canvasHeight - increment) w.y += increment;
        }
   }

}

http.createServer(function (request,response){


     //parse the query parameters if there are some
	 var urlObj = url.parse(request.url, true, false);

     if(urlObj.pathname != "/pollData"){
        //don't show details of /pollData requests
        console.log('\n============================');
        console.log("http request: " + request.url);
        if (request.method == "GET") {
          console.log("METHOD: " + request.method.get);
        }else if (request.method == "PUT") {
          console.log("METHOD: " + request.method.put);
        }else if (request.method == "POST"){
          console.log("METHOD: " + request.method.post);
        }
	    console.log("PATHNAME: " + urlObj.pathname);
	    //show any URL query parameters for GET methods
	    for(key in urlObj.query)
	       console.log("   " + key + ": " + urlObj.query[key]);
     }

     if(request.method == "POST" || request.method == "PUT"){

     //handle http POST or PUT requests

        var receivedData = '';

        //attached event handlers to collect POST message data body
        //which may arrive in chunks
        request.on('data', function(chunk) {
           receivedData += chunk;
        });

	    //event handler for the end of the POST message data
        request.on('end', function(){
           //console.log('REQUEST END: ');
		   var dataObj;
		   try {
		      //try to parse received data as valid JSON
              dataObj = JSON.parse(receivedData);
           } catch (e) {
		        //invalid JSON data format
                console.log('ERROR: data not valid JSON: ', receivedData);
                response.writeHead(404);
                response.end('ERROR: Invalid JSON data');
                return;
           }


		   if(urlObj.pathname == "/move"){
		     //move request
		     //expected message data format: {word:"Bird", direction:"right"}
             console.log('received data: ', receivedData);
             moveWord(dataObj.word, dataObj.direction);
		   }
		   else if(urlObj.pathname == "/add"){
		    //move request
		    //expected message data format: {word:"Bird"}
            console.log('received data: ', receivedData);
            addWord(dataObj.word);
		   }
		   else if (urlObj.pathname == "/pollData"){
              //nothing extra to do
		   }
		   //echo back the location of all the words as a JSON object
		   var responseObject = {words: words};
           response.writeHead(200, {'Content-Type': MIME_TYPES["json"]});
           response.end(JSON.stringify(responseObject)); //send just the JSON object
        });

     }

     if(request.method == "GET"){
       //handle HTTP GET requests
       //console.log("METHOD: GET".get); // outputs underlined yellow text

	    if(urlObj.pathname.indexOf("/add") == 0){
	       //add request
	       //expected format: /add?word=Lou
	       var wordFromClientRequest = urlObj.query.word;
	       addWord(wordFromClientRequest);
	       var jsonResponse = {words: words};
           response.writeHead(200, {'Content-Type': MIME_TYPES["json"]});
           response.end(JSON.stringify(jsonResponse)); //send just the JSON object
	    }
	    else if(urlObj.pathname.indexOf("/move") == 0){
     	   //move request
     	   //expected format: /move?word=Lou&direction=right
     	   var wordFromClientRequest = urlObj.query.word;
     	   var directionToMove = urlObj.query.direction;
     	   moveWord(wordFromClientRequest, directionToMove);
     	   var jsonResponse = {words: words};
           response.writeHead(200, {'Content-Type': MIME_TYPES["json"]});
           response.end(JSON.stringify(jsonResponse)); //send just the JSON object
        }
	    else if(urlObj.pathname.indexOf("/pollData") == 0){
     	   //echo back data
     	   var jsonResponse = {words: words};
           response.writeHead(200, {'Content-Type': MIME_TYPES["json"]});
           response.end(JSON.stringify(jsonResponse)); //send just the JSON object
        }

	    else{
	       //handle GET requests as a request for a static file
	       //use this to serve the html and javascript apps store in html directory

           fs.readFile(ROOT_DIR + urlObj.pathname, function(err,data){
             if(err){
		        //report error to console
                console.log('ERROR: ' + JSON.stringify(err));
		        //respond with not found 404 to client
                response.writeHead(404);
                response.end(JSON.stringify(err));
                return;
               }
               //respond to client with static file
               response.writeHead(200, {'Content-Type': get_mime(urlObj.pathname)});
               response.end(data);
           });
        }
	 }


 }).listen(3001); //listen on  port 3000

console.log('Server Running at PORT:3000  CNTL-C to quit');
