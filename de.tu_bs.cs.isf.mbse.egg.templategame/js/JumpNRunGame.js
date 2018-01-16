var canvas;
var ctx;

var width;
var height;

var blockSize;
var startPageKey;





// Setup-Methode. Wird getriggert sobald html-Inhalt geladen ist.
window.addEventListener('load', function(){
	// ODER: document einen listener auf 'DOMContentLoaded' hinzufügen
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');
    
    width = window.innerWidth;
    height = window.innerHeight;
    canvas.width = width;
    canvas.height = height;

    window.addEventListener('keydown', handleKeyDownEvent, false);
    window.addEventListener('keyup', handleKeyUpEvent, false);

    setupPages();
    
    drawGame();
    handleTicks();
}, false);





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