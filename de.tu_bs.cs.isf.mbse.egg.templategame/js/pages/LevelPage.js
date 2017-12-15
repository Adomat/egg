LevelPage.prototype = new DisplayPage();

function LevelPage() {
	this.levelScroll = function () {  return {  x: null,  y: null  };  };
	this.mapSize = function () {  return {  x: null,  y: null  };  };
	this.gravity;

	this.blocks = [];
	this.exitGates = [];
	this.hero = null;
	
	this.showDeathScreen = false;
}

LevelPage.prototype.addBlock = function(newBlock) {
	this.blocks.push(newBlock);
}

LevelPage.prototype.addHero = function(newHero) {
	this.hero = newHero;
}

LevelPage.prototype.addExitGate = function(newPageKey, x, y) {
	var newExitGate = new ExitGate(newPageKey, x, y);
	this.exitGates.push(newExitGate);
}

LevelPage.prototype.draw = function() {
	var offSet = getPixelOffsetFromScroll(this.levelScroll, this.mapSize);

	var newScrollX = offSet.x / (this.mapSize.x * blockSize);
	var newScrollY = offSet.y / (this.mapSize.y * blockSize);
	
	this.drawBackground(this.levelScroll.x-newScrollX, this.levelScroll.y+newScrollY);
	
	for(var i=0; i<this.blocks.length; i++) {
		this.blocks[i].draw(this.levelScroll, this.mapSize, this.hero.collisionBox);
	}
	
	this.hero.draw(this.levelScroll, this.mapSize);
	
//	this.hero.drawCollisionBox();
	
	if(this.showDeathScreen) {
		this.drawDeathScreen();
	}
}

LevelPage.prototype.executeTick = function() {
	this.calculateMapSize();
	this.moveBackground();
	
	if(this.hero.health <= 0) {
		this.showDeathScreen = true;
	} else {
		this.hero.move(this.blocks, this.gravity, this.exitGates);
	}
}

LevelPage.prototype.calculateMapSize = function() {
	if(this.mapSize.x == null) {
		this.mapSize.x = 1;
		this.mapSize.y = 1;
		
		for(var i=0; i<this.blocks.length; i++) {
			if(this.blocks[i].position.x > this.mapSize.x)
				this.mapSize.x = this.blocks[i].position.x;
			
			if(this.blocks[i].position.y > this.mapSize.y)
				this.mapSize.y = this.blocks[i].position.y;
		}
	}
}

LevelPage.prototype.moveBackground = function() {
	this.levelScroll.x = this.hero.position.x / this.mapSize.x;
	this.levelScroll.y = 1 - this.hero.position.y / this.mapSize.y;
}

LevelPage.prototype.drawDeathScreen = function() {
	ctx.fillStyle = "rgba(0, 0, 0, 0.75)";
	ctx.fillRect(0, 0, width, height);

	ctx.fillStyle = "rgba(255, 255, 255, 0.75)";
	ctx.font = '100pt Arial';
	ctx.textAlign = "center";
	ctx.fillText("Game Over", width/2, height/2+0);

	ctx.font = '20pt Arial';
	ctx.fillText("Press \'Enter\' to enter the main menu", width/2, height/2+100);
	
	if(keyDownArray.includes(13)) {
		setupPages();
		switchPage("menu");
	}
}






