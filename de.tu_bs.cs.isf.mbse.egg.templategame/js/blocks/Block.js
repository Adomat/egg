function Block() {
	this.images = [];
	this.position = function () {  return {  x: null,  y: null  };  }
	this.position.x;
	this.position.y;
	
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
    
	var x = this.position.x * blockSize;
	var y = height - this.position.y * blockSize;

	x -= scroll.x * mapSize.x * blockSize - width/2 + blockSize/2;
	y -= height/2 - (1-scroll.y) * mapSize.y * blockSize;
	
	var offSet = getPixelOffsetFromScroll(scroll, mapSize);
	
	x += offSet.x;
	y -= offSet.y;
	ctx.drawImage(this.images[this.currentImageIndex], x, y, blockSize, blockSize);
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
	var x = this.position.x * blockSize;
	var y = height - blockSize - this.position.y * blockSize;

	x += blockSize / 2;
	y += blockSize / 2;
	
	var returnVector = function () {  return {  x: null,  y: null  };  }
	returnVector.x = x;
	returnVector.y = y;
	
	return returnVector;
}