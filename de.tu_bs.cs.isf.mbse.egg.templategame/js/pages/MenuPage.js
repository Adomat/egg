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
	this.drawBackground(0.5, 1);
	
	this.switchLogoAnimationIndex();
	
	ctx.drawImage(this.logoImages[this.logoImageAnimationIndex], width/2 - this.logoImages[this.logoImageAnimationIndex].width/2, height/2 - this.logoImages[this.logoImageAnimationIndex].height);
    
    for(var i=0; i<this.buttons.length; i++) {
        this.drawButton(this.buttons[i].text, 30, i);
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

MenuPage.prototype.drawButton = function(text, textSize, index) {
	ctx.textAlign = "center";
	ctx.font = textSize+'pt Arial';
	var textWidth = ctx.measureText(text).width + textSize;
	var textHeight = textSize*2;
	
	ctx.fillStyle = "rgba(40,40,40, 0.5)";
	if(this.buttonIndex == index)
		ctx.fillStyle = "rgba(60,60,60, 0.9)";
	
	ctx.strokeStyle = "rgba(40,40,40, 1)";
	
	var x = width/2 - textWidth/ 2;
	var y = 50 + height/2 + index * (textSize * 2 + 10);
	
	ctx.fillRect(x, y, textWidth, textHeight);
	ctx.strokeRect(x, y, textWidth, textHeight);

	ctx.fillStyle = "rgba(255, 255, 255, 1)";
	ctx.fillText(text, width/2, y+textSize*1.5);
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


















