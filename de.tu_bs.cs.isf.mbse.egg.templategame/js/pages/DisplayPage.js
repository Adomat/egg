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
    
    var imageWidth = this.backgroundImage.width;
    var imageHeight = this.backgroundImage.height;
    
    var ratio = height / imageHeight;
    var additionalZoom = 1 - Math.max(width - this.backgroundImage.width * ratio, 0) / width;
    ratio = ratio / additionalZoom;
    
    if(Math.random() < 0.01)
        console.log(additionalZoom);
	
	var offsetX = (this.backgroundImage.width * ratio - width) * xPercent;
    var offsetY = (this.backgroundImage.height * ratio - height) * yPercent;
	
	ctx.drawImage(this.backgroundImage, -offsetX, -offsetY, this.backgroundImage.width * ratio, this.backgroundImage.height * ratio);
}

DisplayPage.prototype.keyPressed = function(event) {
    
}