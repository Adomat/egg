function DisplayPage() {
	this.pageKey;
	this.backgroundImage;
	this.backgroundColor;
}



DisplayPage.prototype.setPageKey = function(pageKey) {
	this.pageKey = pageKey;
}

DisplayPage.prototype.getPageKey = function(pageKey) {
	return this.pageKey;
}



DisplayPage.prototype.setBackgroundImage = function(imageURL) {
	this.backgroundImage = new Image();
	this.backgroundImage.src = imageURL;
}

DisplayPage.prototype.setBackgroundColor = function(colorString) {
	this.backgroundColor = colorString;
}

DisplayPage.prototype.drawBackground = function drawBackground(xPercent, yPercent) {
	xPercent = Math.max(Math.min(xPercent, 1), 0);
	yPercent = Math.max(Math.min(yPercent, 1), 0);
	
	ctx.fillStyle = "rgb(95, 150, 255)";
	ctx.fillRect(0, 0, width, height);
	
	var offset = function () {  return {  x: 0,  y: 0  };  }
	offset.x = (this.backgroundImage.width - width) * xPercent;
	offset.y = (this.backgroundImage.height - height) * yPercent;
	
	ctx.drawImage(this.backgroundImage, -offset.x, -offset.y);
}

DisplayPage.prototype.keyPressed = function(event) {
    
}