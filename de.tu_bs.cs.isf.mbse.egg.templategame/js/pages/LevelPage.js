LevelPage.prototype = new DisplayPage();

function LevelPage() {
	this.levelScroll = function () {  return {  x: null,  y: null  };  };
	this.mapSize = function () {  return {  x: null,  y: null  };  };
	this.gravity;

	this.blocks = [];
	this.enemies = [];
	this.exitGates = [];
	this.hero = null;
	this.showDeathScreen = false;
    
    this.blockSize = 75;
    
    this.deathMessage = "GAME OVER";
    this.deathMessageParagraph = "Press \'Enter\' to enter the main menu";
    this.deathReturnPage = startPageKey;
}

LevelPage.prototype.addBlock = function(givenBlock, x, y) {
    var newBlock = new Block();
    newBlock.images = givenBlock.images;
    newBlock.isSolid = givenBlock.isSolid;
    newBlock.animationSpeed = givenBlock.animationSpeed;
    
    newBlock.position.x = x;
    newBlock.position.y = y;
    
	this.blocks.push(newBlock);
}

LevelPage.prototype.addEnemy = function(givenEnemy, x, y) {
	var newEnemy = new EnemyCharacter();
    newEnemy.movable = givenEnemy.movable;
    
	newEnemy.positionX = x;
	newEnemy.positionY = y;
    
	newEnemy.idleImages = givenEnemy.idleImages;
	newEnemy.attackImages = givenEnemy.attackImages;
	newEnemy.runImages = givenEnemy.runImages;
	newEnemy.jumpImages = givenEnemy.jumpImages;
	
    /*for(var i=0; i<givenEnemy.idleImages.length; i++)
	   newEnemy.idleImages.push(givenEnemy.idleImages[i]);
    for(var i=0; i<givenEnemy.attackImages.length; i++)
	   newEnemy.attackImages.push(givenEnemy.attackImages[i]);
    for(var i=0; i<givenEnemy.runImages.length; i++)
        newEnemy.addRunImage(givenEnemy.runImages[i].src);
    for(var i=0; i<givenEnemy.jumpImages.length; i++)
	   newEnemy.jumpImages.push(givenEnemy.jumpImages[i]);*/
	
	newEnemy.collisionBoxX = givenEnemy.collisionBoxX;
	newEnemy.collisionBoxY = givenEnemy.collisionBoxY;
	newEnemy.animationSpeed = givenEnemy.animationSpeed;
	newEnemy.life = givenEnemy.life;
	newEnemy.speed = givenEnemy.speed;
	newEnemy.jumpPower = givenEnemy.jumpPower;
    newEnemy.showCollisionBox = givenEnemy.showCollisionBox;
    
	this.enemies.push(newEnemy);
}

LevelPage.prototype.addHero = function(newHero, x, y) {
	this.hero = new HeroCharacter();
    
	this.hero.positionX = x;
	this.hero.positionY = y;
	
	this.hero.idleImages = newHero.idleImages;
	this.hero.attackImages = newHero.attackImages;
	this.hero.runImages = newHero.runImages;
	this.hero.jumpImages = newHero.jumpImages;
	
	this.hero.collisionBoxX = newHero.collisionBoxX;
	this.hero.collisionBoxY = newHero.collisionBoxY;
	this.hero.animationSpeed = newHero.animationSpeed;
	this.hero.life = newHero.life;
	this.hero.speed = newHero.speed;
	this.hero.jumpPower = newHero.jumpPower;
    this.hero.showCollisionBox = newHero.showCollisionBox;
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
		this.blocks[i].draw(this.levelScroll, this.mapSize);
	}
	
	this.hero.draw(this.levelScroll, this.mapSize);
    
    for(var i=0; i<this.enemies.length; i++) {
		this.enemies[i].draw(this.levelScroll, this.mapSize);
	}
	
	if(this.showDeathScreen) {
		this.drawDeathScreen();
	}
}

LevelPage.prototype.executeTick = function() {
	this.calculateMapSize();
	this.moveBackground();
	
	if(this.hero.life <= 0) {
		this.showDeathScreen = true;
	} else {
		this.hero.move(this.blocks, this.gravity, this.exitGates);
	}
    
    for(var i=0; i<this.enemies.length; i++) {
		if(this.enemies[i].life > 0) {
            this.enemies[i].move(this.blocks, this.gravity, this.exitGates);
        }
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
	this.levelScroll.x = this.hero.positionX / this.mapSize.x;
	this.levelScroll.y = 1 - this.hero.positionY / this.mapSize.y;
}

LevelPage.prototype.drawDeathScreen = function() {
	ctx.fillStyle = "rgba(0, 0, 0, 0.75)";
	ctx.fillRect(0, 0, width, height);

	ctx.fillStyle = "rgba(255, 255, 255, 0.75)";
	ctx.font = '100pt Arial';
	ctx.textAlign = "center";
	ctx.fillText(this.deathMessage, width/2, height/2+0);

	ctx.font = '20pt Arial';
	ctx.fillText(this.deathMessageParagraph, width/2, height/2+100);
	
	if(keyDownArray.includes(13)) {
		setupPages();
		switchPage(this.deathReturnPage);
	}
}






