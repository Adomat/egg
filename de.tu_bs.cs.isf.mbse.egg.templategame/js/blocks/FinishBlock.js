FinishBlock.prototype = new Block();

function FinishBlock(x, y) {
	this.position.x = x;
	this.position.y = y;
	
	this.newPageKey = "FINISHBLOCK";
    
    this.isSolid = false;
}