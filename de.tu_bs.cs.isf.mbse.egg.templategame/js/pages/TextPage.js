TextPage.prototype = new DisplayPage();

function TextPage() {
	this.title = "";
	this.paragraphs = [];
	
	this.newPageKey;
	
	this.scroll = 0;
	this.lastLineCount = 0;
}

TextPage.prototype.addParagraph = function(text) {
	this.paragraphs.push(text);
}



TextPage.prototype.draw = function() {
	this.drawBackground(0.5, 0.5);

	ctx.fillStyle = this.textStyle;

	ctx.textAlign = "center";
	ctx.font = this.textSize*2+'pt Arial';
	ctx.fillText(this.title, width/2, this.textSize*2 + 50 + this.scroll);
	
	ctx.font = this.textSize+'pt Arial';
	var textBlockSize = Math.min(width-40, 800);
	var currentLineIndex = 0;
	
	for(var i=0; i<this.paragraphs.length; i++) {
		var paragraphWords = this.paragraphs[i].split(" ");
		
		var paragraphWordIndex = 0;
		while(paragraphWordIndex < paragraphWords.length) {
			var textLine = "";
			while(true) {
				var newTextLine = textLine + " " + paragraphWords[paragraphWordIndex];
				
				if(ctx.measureText(newTextLine).width > textBlockSize)
					break;
				
				textLine = newTextLine;
				paragraphWordIndex++;
				if(paragraphWordIndex >= paragraphWords.length)
					break;
			}
			
			ctx.fillText(textLine, width/2, 150 + this.textSize * 1.5 * currentLineIndex + this.scroll);
			currentLineIndex++;
		}
	}
	
	/*currentLineIndex +=4;
	ctx.font = this.textSize*2+'pt Arial';
	ctx.fillText("Press Enter to Continue", width/2, 150 + this.textSize * 1.5 * currentLineIndex + this.scroll);*/
	
	this.lastLineCount = currentLineIndex;
}

TextPage.prototype.keyPressed = function(event) {
	if(event.keyCode == 13)
		switchPage(this.newPageKey);
	
	if(150 + this.textSize * 1.5 * this.lastLineCount > height) {
		if(event.keyCode == 38)
			this.scroll += 10;
		if(event.keyCode == 40)
			this.scroll -= 10;
		
		if(this.scroll > 0)
			this.scroll = 0;
		if(this.scroll < (-1) * (200 + this.textSize * 1.5 * this.lastLineCount - height))
			this.scroll = (-1) * (200 + this.textSize * 1.5 * this.lastLineCount - height);
		
		if(this.lastLineCount == 0)
			this.scroll = 0;
	}
}

TextPage.prototype.executeTick = function() {
	
}