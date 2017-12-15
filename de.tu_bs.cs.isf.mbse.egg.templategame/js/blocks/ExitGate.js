ExitGate.prototype = new Block();

function ExitGate(newPageKey, x, y) {
	this.position.x = x;
	this.position.y = y;
	
	this.newPageKey = newPageKey;
}