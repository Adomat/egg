function Block() {
	this.images = [];
	this.position = function () {  return {  x: null,  y: null  };  }
	this.positionX;
	this.positionY;
	
	this.currentImageIndex = 0;
	this.lastAnimationChange = 0;
	
	this.animationSpeed = 0;
	this.isSolid = true;
}





Block.prototype.addImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.images.push(image);
}

Block.prototype.draw = function(scroll, mapSize) {
    if(this.images.length > 1 && this.animationSpeed > 0)
	   this.switchAnimationIndex();
    
	var x = this.positionX * blockSize;
	var y = height - this.positionY * blockSize;

	x -= scroll.x * mapSize.x * blockSize - width/2 + blockSize/2;
	y -= height/2 - (1-scroll.y) * mapSize.y * blockSize;
	
	var offSet = getPixelOffsetFromScroll(scroll, mapSize);
	
	x += offSet.x;
	y -= offSet.y;
    
    if(this.images.length > 0)
	   ctx.drawImage(this.images[this.currentImageIndex], x, y, blockSize, blockSize);
    else {
        ctx.fillStyle = "rgba(255, 0, 0, 0.5)";
        ctx.fillRect(x, y, blockSize, blockSize);
    }
}

Block.prototype.switchAnimationIndex = function() {
	var timePassed = (new Date()).getTime() - this.lastAnimationChange;
	
	if(timePassed > this.animationSpeed) {
		this.currentImageIndex ++;
        if(this.currentImageIndex >= this.images.length)
            this.currentImageIndex = 0;
		
		this.lastAnimationChange = (new Date()).getTime();
	}
}

Block.prototype.getCenter = function() {
	var x = this.positionX * blockSize;
	var y = height - blockSize - this.positionY * blockSize;

	x += blockSize / 2;
	y += blockSize / 2;
	
	var returnVector = function () {  return {  x: null,  y: null  };  }
	returnVector.x = x;
	returnVector.y = y;
	
	return returnVector;
}