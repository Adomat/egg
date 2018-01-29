FinishBlock.prototype = new Block();

function FinishBlock(x, y) {
	this.positionX = x;
	this.positionY = y;
	
	this.newPageKey = "FINISHBLOCK";
    
    this.isSolid = false;
}