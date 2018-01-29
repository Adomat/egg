ExitGate.prototype = new Block();

function ExitGate(newPageKey, x, y) {
	this.positionX = x;
	this.positionY = y;
	
	this.newPageKey = newPageKey;
}