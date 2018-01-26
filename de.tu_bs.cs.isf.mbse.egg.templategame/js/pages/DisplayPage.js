function DisplayPage() {
	this.pageKey;
	this.backgroundImage;
	this.backgroundColor = "#000";
    
	this.textSize = 15;
	this.textStyle = "#fff";
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
	ctx.fillStyle = this.backgroundColor;
	ctx.fillRect(0, 0, width, height);
    
    if(this.backgroundImage == null)
        return;
    
	xPercent = Math.max(Math.min(xPercent, 1), 0);
	yPercent = Math.max(Math.min(yPercent, 1), 0);
	
	var offset = function () {  return {  x: 0,  y: 0  };  }
	offset.x = (this.backgroundImage.width - width) * xPercent;
	offset.y = (this.backgroundImage.height - height) * yPercent;
	
	ctx.drawImage(this.backgroundImage, -offset.x, -offset.y);
}

DisplayPage.prototype.keyPressed = function(event) {
    
}