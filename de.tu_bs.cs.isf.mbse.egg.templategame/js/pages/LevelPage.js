LevelPage.prototype = new DisplayPage();

function LevelPage() {
	this.levelScroll = function () {  return {  x: null,  y: null  };  };
	this.mapSize = function () {  return {  x: null,  y: null  };  };
	this.gravity = 10;

	this.blocks = [];
	this.enemies = [];
	this.exitGates = [];
	this.finishBlocks = [];
	this.hero = null;
	this.showWinScreen = false;
    
    this.blockSize = 75;
    
	this.showDeathScreen = false;
    this.deathMessage = "GAME OVER";
    this.deathMessageParagraph = "Press \'Enter\' to return to the main menu";
    this.deathReturnPage = startPageKey;
    
	this.showWinScreen = false;
    this.winMessage = "CONGRATULIATIONS";
    this.winMessageParagraph = "You won the Game.\nPress \'Enter\' to return to the main menu";
    this.winReturnPage = startPageKey;
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
	newEnemy.positionY = y-1 + (givenEnemy.collisionBoxY/2) / blockSize;
    
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
	this.hero.positionY = y-1 + (newHero.collisionBoxY/2) / this.blockSize;
	
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

LevelPage.prototype.addFinishBlock = function(x, y) {
	var newFinishBlock = new FinishBlock(x, y);
	this.finishBlocks.push(newFinishBlock);
}

LevelPage.prototype.draw = function() {
	var offSet = getPixelOffsetFromScroll(this.levelScroll, this.mapSize);

	var newScrollX = offSet.x / (this.mapSize.x * blockSize);
	var newScrollY = offSet.y / (this.mapSize.y * blockSize);
	
	this.drawBackground(this.levelScroll.x-newScrollX, this.levelScroll.y+newScrollY);
	
	this.hero.draw(this.levelScroll, this.mapSize);
    
    for(var i=0; i<this.enemies.length; i++) {
		this.enemies[i].draw(this.levelScroll, this.mapSize);
	}
	
	for(var i=0; i<this.blocks.length; i++) {
		this.blocks[i].draw(this.levelScroll, this.mapSize);
	}
	
	if(this.showWinScreen) {
		this.drawAfterScreen(this.winMessage, this.winMessageParagraph, this.winReturnPage);
	}
	else if(this.showDeathScreen) {
		this.drawAfterScreen(this.deathMessage, this.deathMessageParagraph, this.deathReturnPage);
	}
}

LevelPage.prototype.executeTick = function() {
    if(this.showWinScreen || this.showDeathScreen)
        return;
    
	this.calculateMapSize();
	this.moveBackground();
	
	if(this.hero.life <= 0) {
		this.showDeathScreen = true;
	} else {
		this.hero.move(this.blocks, this.gravity, this.exitGates);
	}
    
    var heroX = this.hero.positionX * blockSize;
    var heroY = this.hero.positionY * blockSize;
    
    for(var i=0; i<this.enemies.length; i++) {
		if(this.enemies[i].life > 0) {
            this.enemies[i].move(this.blocks, this.gravity, this.exitGates);
            
            // Check if this enemy touches the hero!
            var enemyX = this.enemies[i].positionX * blockSize;
            var enemyY = this.enemies[i].positionY * blockSize;
            
            if(heroX + this.hero.collisionBoxX/2 > enemyX - this.enemies[i].collisionBoxX/2) {
                if(heroX - this.hero.collisionBoxX/2 < enemyX + this.enemies[i].collisionBoxX/2) {
                    // collision on X axes
                    
                    if(heroY + this.hero.collisionBoxY/2 > enemyY - this.enemies[i].collisionBoxY/2) {
                        if(heroY - this.hero.collisionBoxY/2 < enemyY + this.enemies[i].collisionBoxY/2) {
                            // COLLISION WITH ENEMY DETECTED
                            this.hero.dealDamage(this.enemies[i].strength);
                        }
                    }
                }
            }
        }
	}
    
    // Check whether the hero entered an exit gate
	var newPageKey = this.hero.collisionWithSpecialBlocks(this.exitGates);
	if(newPageKey != null) {
		switchPage(newPageKey);
	}
    
    // Check whether the hero finished the game!
    var newPageKey = this.hero.collisionWithSpecialBlocks(this.finishBlocks);
	if(newPageKey != null) {
		this.showWinScreen = true;
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

LevelPage.prototype.drawAfterScreen = function(message, messageParagraph, returnPage) {
	ctx.fillStyle = "rgba(0, 0, 0, 0.75)";
	ctx.fillRect(0, 0, width, height);

	ctx.fillStyle = "rgba(255, 255, 255, 0.75)";
	ctx.font = '100pt Arial';
	ctx.textAlign = "center";
	ctx.fillText(message, width/2, height/2+0);

	ctx.font = '20pt Arial';
	ctx.fillText(messageParagraph, width/2, height/2+100);
	
	if(keyDownArray.includes(13)) {
		setupPages();
		switchPage(returnPage);
	}
}







