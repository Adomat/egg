MenuPage.prototype = new DisplayPage();

function MenuPage() {
	this.logoImages = [];
	this.logoImageAnimationIndex = 0;
	this.logoAnimationSpeed;
	this.lastLogoAnimationChange = 0;
	
	this.buttons = [];
	this.buttonIndex = 0;
}

MenuPage.prototype.addLogoImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.logoImages.push(image);
}












MenuPage.prototype.draw = function() {
	this.drawBackground(0.5, 0.5);
	
	if(this.logoImages.length > 0) {
	   this.switchLogoAnimationIndex();
	   ctx.drawImage(this.logoImages[this.logoImageAnimationIndex], width/2 - this.logoImages[this.logoImageAnimationIndex].width/2, height/2 - this.logoImages[this.logoImageAnimationIndex].height);
    }
    
    for(var i=0; i<this.buttons.length; i++) {
        this.drawButton(this.buttons[i].text, i);
    }
}

MenuPage.prototype.switchLogoAnimationIndex = function() {
	var timePassed = (new Date()).getTime() - this.lastLogoAnimationChange;
	
	if(timePassed > this.logoAnimationSpeed) {
		this.logoImageAnimationIndex ++;
		if(this.logoImageAnimationIndex >= this.logoImages.length)
			this.logoImageAnimationIndex = 0;
		
		this.lastLogoAnimationChange = (new Date()).getTime();
	}
}

MenuPage.prototype.addButton = function(text, pageKey) {
    var newButton = function () {  return {  text: null,  pageKey: null  };  }
    newButton.text = text;
    newButton.pageKey = pageKey;
    
    this.buttons.push(newButton);
}

MenuPage.prototype.drawButton = function(text, index) {
	ctx.textAlign = "center";
	ctx.font = this.textSize+'pt Arial';
	var textWidth = ctx.measureText(text).width + this.textSize;
	var textHeight = this.textSize*2;
	
	ctx.fillStyle = hexToRgbA(this.textStyle, 0.4);
	if(this.buttonIndex == index)
		ctx.fillStyle = hexToRgbA(this.textStyle, 0.8);
	
	ctx.strokeStyle = "rgba(40,40,40, 1)";
	
	var x = width/2 - textWidth/ 2;
	var y = 50 + height/2 + index * (this.textSize * 2 + 10);
	
	ctx.fillRect(x, y, textWidth, textHeight);
	ctx.strokeRect(x, y, textWidth, textHeight);

	ctx.fillStyle = "rgba(255, 255, 255, 1)";
	ctx.fillText(text, width/2, y+this.textSize*1.5);
}











MenuPage.prototype.executeTick = function() {
}

MenuPage.prototype.keyPressed = function(event) {
	if(event.keyCode == 13)
        switchPage(this.buttons[this.buttonIndex].pageKey);
	
	if(event.keyCode == 38)
		this.buttonIndex --;
	if(event.keyCode == 40)
		this.buttonIndex ++;

	if(this.buttonIndex < 0)
		this.buttonIndex = this.buttons.length-1;
	if(this.buttonIndex >= this.buttons.length)
		this.buttonIndex = 0;
}


















