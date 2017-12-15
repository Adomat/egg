function Block(x, y) {
	this.images = [];
	this.position = function () {  return {  x: null,  y: null  };  }
	this.position.x = x;
	this.position.y = y;
	
	this.isSolid = true;
}





Block.prototype.addImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.images.push(image);
}

Block.prototype.draw = function(scroll, mapSize) {
	var x = this.position.x * blockSize;
	var y = height - this.position.y * blockSize;

	x -= scroll.x * mapSize.x * blockSize - width/2 + blockSize/2;
	y -= height/2 - (1-scroll.y) * mapSize.y * blockSize;
	
	var offSet = getPixelOffsetFromScroll(scroll, mapSize);
	
	x += offSet.x;
	y -= offSet.y;
	
	ctx.drawImage(this.images[0], x, y, blockSize, blockSize);
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