/*
Example Javascript
*/

var rect1 = {x: 20, y:20, width: 50, height: 30};
var rect2 = {left: 20, top: 20, right: 70, bottom: 50};

//exploring objects

console.log("--------exploring objects----------\n");

for(k in rect1) console.log(k);
for(k in rect1) console.log(rect1[k]);
for(k in rect1) console.log(k + ": " + rect1[k]);
for(k in rect1) console.log(typeof k + ": " + typeof rect1[k]);

console.log("-----------------------------------\n");

console.log("--------------Problem 2 -Javascript Functions----------------\n");

/*function contains(rect, point){
    //answer whether rect contains point
    return rect.left <= point.x &&
           rect.right >= point.x &&
           rect.top <= point.y &&
           rect.bottom >= point.y;
}*/


/*Javascript, in an effort to be helpful, will insert a semicolon where it
"thinks" you might have forgotten one. Specifically it compiled the code as:*/

function contains(rect, point){
    //answer whether rect contains point
    return
           rect.left <= point.x &&
           rect.right >= point.x &&
           rect.top <= point.y &&
           rect.bottom >= point.y;
}


var pt = {x:30,y:40};
console.log(contains(rect2, pt));
console.log("-------------------------------------------------------------\n");

console.log("------------------Default-----------------\n");
console.log(rect1);
console.log(rect2);
console.log("-----------------------------------\n");
