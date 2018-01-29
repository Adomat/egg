var canvas;
var video;
var ctx;

var width;
var height;

var blockSize;
var startPageKey;

var videoStartUpTime;





// Setup-Methode. Wird getriggert sobald html-Inhalt geladen ist.
//window.addEventListener('load', function(){
document.addEventListener('DOMContentLoaded', function(){
    startUpTime = (new Date()).getTime();
	// ODER: document einen listener auf 'DOMContentLoaded' hinzufügen
    video = document.getElementById('video');
    
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');
    
    width = window.innerWidth;
    height = window.innerHeight;
    canvas.width = width;
    canvas.height = height;

    window.addEventListener('keydown', handleKeyDownEvent, false);
    window.addEventListener('keyup', handleKeyUpEvent, false);
    
    video.addEventListener('play', function(){
        videoStartUpTime = -1;
    },false);
    
    video.addEventListener('ended', function(){
        videoStartUpTime = -2;
    },false);
    
    waitForIntro();
}, false);





function waitForIntro() {
    ctx.fillStyle = "rgb(50, 50, 50)";
	ctx.fillRect(0, 0, width, height);
    
    var timePassed = (new Date()).getTime() - videoStartUpTime;
    
    if(videoStartUpTime == -1) {
        playIntro();
    }
    else if(timePassed > 1500) {
        console.log("Couldnt load intro.mp4");
        setupPages();
    
        drawGame();
        handleTicks();
    }
    else
        setTimeout(waitForIntro, 10);
}





function playIntro() {
    if(width != window.innerWidth  ||  height != window.innerHeight) {
        width = window.innerWidth;
        height = window.innerHeight;
        canvas.width = width;
        canvas.height = height;
    }
    
    var imageRatio = video.videoWidth / video.videoHeight;
    var screenRatio = width / height;
    
    var offSet = function () {  return {  x: 0,  y: 0  };  }
    offSet.x = 0;
    offSet.y = 0;
    
    var imageWidth = width;
    var imageHeight = height;
    
    if(screenRatio > imageRatio) {
        // Browserausschnitt breiter als Bildverhältnis
        
        imageHeight = width / imageRatio;
        offSet.y = (imageHeight - height) / 2;
    }
    else {
        // Browserausschnitt höher als Bildverhältnis
        
        imageWidth = height * imageRatio;
        offSet.x = (imageWidth - width) / 2;
    }
    
    if(videoStartUpTime != -2) {
        ctx.drawImage(video, -offSet.x, -offSet.y, imageWidth, imageHeight);
        setTimeout(playIntro, 0);
    }
    else {
        console.log("Finished playing the intro.");
        setupPages();
    
        drawGame();
        handleTicks();
    }
}






// Funktion zum Bild darstellen
function drawGame() {
//	blockSize = Math.sin((new Date()).getTime()/10000) * 25 + 75;
	
    if(width != window.innerWidth  ||  height != window.innerHeight) {
        width = window.innerWidth;
        height = window.innerHeight;
        canvas.width = width;
        canvas.height = height;
    }
    
	drawCurrentPage();
	
    setTimeout(drawGame, 0);
}




function handleTicks() {
	var timeMessureStart = (new Date()).getTime();
	
	passTickToCurrentPage();
	
	var timeMessureEnd = (new Date()).getTime();
	var timeMessureDifference = timeMessureEnd - timeMessureStart;
	var tps = 1000 / 50;
	var timeOut = Math.max(0, tps - timeMessureDifference);
	
    setTimeout(handleTicks, timeOut);
}