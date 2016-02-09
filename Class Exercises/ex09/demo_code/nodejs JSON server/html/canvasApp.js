/*
Javasript app to display words obtained by polling server

Keyboard arrow keys are used to send move api events to server

handle mouse dragging and release to drag a word locally around the
html canvas but locations are not sent to server (part of a future exercise)

*/

//Use javascript array of objects to represent words and their locations
var words = [{word: "Bird", x:200, y:50}];

var movingString = {word: "Moving",
                    x: 100,
					y:100,
					xDirection: 1, //+1 for leftwards, -1 for rightwards
					yDirection: 1, //+1 for downwards, -1 for upwards
					stringWidth: 50, //will be updated when drawn
					stringHeight: 24}; //assumed height based on drawing point size


var timer; //used to control the free moving word
var pollingTimer; //timer to poll server for word and location updates

var wordBeingMoved; //word being moved
var wordTargetRect = {x:0,y:0,width:0,height:0}; //bounding box around word being targeted

var deltaX, deltaY; //location where mouse is pressed
var isMouseDown = false; //true when mouse is down
var canvas = document.getElementById('canvas1'); //our drawing canvas
var fontPointSize = 18; //point size for word text
var wordHeight = 20; //estimated height of a string in the editor
var editorFont = 'Arial'; //font for your editor

function getWordAtLocation(aCanvasX, aCanvasY){

	  //locate the word targeted by aCanvasX, aCanvasY
	  //find a word whose bounding box contains location (aCanvasX, aCanvasY)

	  var context = canvas.getContext('2d');

	  for(var i=0; i<words.length; i++){
	     var wordWidth = context.measureText(words[i].word).width;
		 if((aCanvasX > words[i].x && aCanvasX < (words[i].x + wordWidth))  &&
			    (aCanvasY > words[i].y - wordHeight && aCanvasY < words[i].y)) {
			//set word targeting rectangle for debugging display
		    wordTargetRect = {x: words[i].x, y: words[i].y-wordHeight, width: wordWidth, height : wordHeight};
			return words[i]; //return the word found
		 }

    }
}

var drawCanvas = function(){
    //draw the objects on the canvas
    //this function should be called whenever a GUI update is required

    var context = canvas.getContext('2d');

    context.fillStyle = 'white';
    context.fillRect(0,0,canvas.width,canvas.height); //erase canvas

    context.font = '' + fontPointSize + 'pt ' + editorFont;
    context.fillStyle = 'cornflowerblue';
    context.strokeStyle = 'blue';

    if(wordBeingMoved != null & isMouseDown)
        context.fillText(wordBeingMoved.word, wordBeingMoved.x, wordBeingMoved.y);

    for(var i=0; i<words.length; i++){
		    var data = words[i];
			context.fillText(data.word, data.x, data.y);
            context.strokeText(data.word, data.x, data.y);

	}

    movingString.stringWidth = context.measureText(	movingString.word).width;
    context.fillText(movingString.word, movingString.x, movingString.y);


	//draw circle
    context.beginPath();
    context.arc(canvas.width/2, //x co-ord
            canvas.height/2, //y co-ord
			canvas.height/2 - 5, //radius
			0, //start angle
			2*Math.PI //end angle
			);
    context.stroke();

	//draw box around word last targeted with mouse -for debugging
	context.strokeStyle = 'red';
	context.strokeRect(wordTargetRect.x, wordTargetRect.y, wordTargetRect.width, wordTargetRect.height);
}

function handleMouseDown(e){

	//get mouse location relative to canvas top left
	isMouseDown = true;
	var rect = canvas.getBoundingClientRect();
    var canvasX = e.pageX - rect.left; //use jQuery event object pageX and pageY
    var canvasY = e.pageY - rect.top;
	console.log("mouse down:" + canvasX + ", " + canvasY);

	wordBeingMoved = getWordAtLocation(canvasX, canvasY);
	//console.log(wordBeingMoved.word);
	if(wordBeingMoved != null ){
	   deltaX = wordBeingMoved.x - canvasX;
	   deltaY = wordBeingMoved.y - canvasY;
	   //attach mouse move and mouse up handlers
	   $("#canvas1").mousemove(handleMouseMove);
	   $("#canvas1").mouseup(handleMouseUp);
	}

    // Stop propagation of the event and stop any default
    // browser action
    e.stopPropagation();
    e.preventDefault();

	drawCanvas();
	}

function handleMouseMove(e){

	console.log("mouse move");

	//get mouse location relative to canvas top left
	var rect = canvas.getBoundingClientRect();
    var canvasX = e.pageX - rect.left;
    var canvasY = e.pageY - rect.top;

	wordBeingMoved.x = canvasX + deltaX;
	wordBeingMoved.y = canvasY + deltaY;

	e.stopPropagation();

	drawCanvas();
	}

function handleMouseUp(e){

    isMouseDown = false;
	console.log("mouse up");
	e.stopPropagation();

	//remove mouse move and mouse up handlers but leave mouse down handler
    $("#canvas1").off("mousemove", handleMouseMove); //remove mouse move handler
    $("#canvas1").off("mouseup", handleMouseUp); //remove mouse up handler

	drawCanvas(); //redraw the canvas
	}



function handleTimer(){
  //Handle intersection
    var rect1, rect2;
  for(var i=0; i<words.length; i++) {
      rect1 = {x: movingString.x ,y:movingString.y,width:movingString.stringWidth, height:movingString.stringHeight};
    if (  words[i].word === "Louis" || words[i].word === "Sean" ) {
          rect2 = {x: words[i].x, y:words[i].y,width:wordTargetRect.width, height:wordTargetRect.height};
    }
    intersectRect(rect1, rect2);
    //keep moving word within bounds of canvas
    movingString.x = (movingString.x + 5*movingString.xDirection);
    movingString.y = (movingString.y + 5*movingString.yDirection);

    if(movingString.x + movingString.stringWidth > canvas.width) movingString.xDirection = -1;
    if(movingString.x < 0) movingString.xDirection = 1;
    if(movingString.y > canvas.height) movingString.yDirection = -1;
    if(movingString.y - movingString.stringHeight < 0) movingString.yDirection = 1;


  }

	drawCanvas()
}

    //KEY CODES
	//should clean up these hard coded key codes
	var RIGHT_ARROW = 39;
	var LEFT_ARROW = 37;
	var UP_ARROW = 38;
	var DOWN_ARROW = 40;


function pollingTimerHandler(){
	//console.log("poll server");
	//create a JSON string representation of the data object
	var jsonString = JSON.stringify({});

    //Poll the server with an HTTP POST message
    //for the location of the moving box

	$.post("pollData",
	    jsonString,
		function(data, status){
			//console.log("data: " + data);
			words = data.words;
			drawCanvas();
			});
}

function handleKeyDown(e){

	console.log("keydown code = " + e.which );

	if(!wordBeingMoved) return;

	var dataObj = {word: wordBeingMoved.word, direction: "none"};

	if(e.which == UP_ARROW){
	   dataObj.direction = "up";  //up arrow
	}
	if(e.which == RIGHT_ARROW){
	   dataObj.direction = "right";  //right arrow
	}
	if(e.which == LEFT_ARROW){
	   dataObj.direction = "left";  //left arrow
	}
	if(e.which == DOWN_ARROW){
	   dataObj.direction = "down";  //down arrow
	}

	//create a JSON string representation of the data object
	var jsonString = JSON.stringify(dataObj);

    //update the server with an HTTP POST message containing
    //word and desired direction to move

	$.post("move",
	    jsonString,
		function(data, status){
		   //do nothing
		});
}

function handleKeyUp(e){
	console.log("key UP: " + e.which);
}

//JQuery Ready function -called when HTML has been parsed and DOM
//created
//can also be just $(function(){...});
//much JQuery code will go in here because the DOM will have been loaded by the time
//this runs

$(document).ready(function(){

	//add mouse down listener to our canvas object
	$("#canvas1").mousedown(handleMouseDown);
	//add keyboard handler to document
	$(document).keydown(handleKeyDown);
	$(document).keyup(handleKeyUp);

	timer = setInterval(handleTimer, 100); //milliseconds
	pollingTimer = setInterval(pollingTimerHandler, 100);  //milliseconds
    //timer.clearInterval(); //to stop

	drawCanvas();
});


                  // {left: x, top: y, right:x+width, bottom:y+height}
//var wordTargetRect = {x:0,y:0,width:0,height:0};
function intersectRect(r1, r2) {
  if( !(r2.x > (r1.x+r1.width) ||
           (r2.x+r2.width) < r1.x ||
           r2.y > (r1.y+r1.height) ||
           (r2.y+r2.height) < r1.y)){
             console.log("true!!");
             if(r1.y+r1.height < r2.y+r2.height) movingString.yDirection = -1;
             if(r1.y+r1.height > r2.y+r2.height) movingString.yDirection = 1;
         		 if(r1.y < r2.y) movingString.xDirection = -1;
         		 if(r1.y > r2.y) movingString.xDirection = 1;
           }
}
