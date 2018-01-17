function HeroCharacter() {
	this.position = function () {  return {  x: null,  y: null  };  };
	this.collisionBox = function () {  return {  x: null,  y: null  };  };
	
	this.idleImages = [];
	this.attackImages = [];
	this.runImages = [];
	this.jumpImages = [];
	
	this.currentImage = null;
	this.currentImageIndex = 0;
	this.lastAnimationChange = 0;
	
	this.animationSpeed;
	this.life;
	this.speed;
	this.jumpPower;
	
	this.lookingright = true;
	this.fallingState = 0;
	this.jumpingState = 0;
}



HeroCharacter.prototype.addIdleImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.idleImages.push(image);
}

HeroCharacter.prototype.addAttackImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.attackImages.push(image);
}

HeroCharacter.prototype.addRunImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.runImages.push(image);
}

HeroCharacter.prototype.addJumpImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.jumpImages.push(image);
}

HeroCharacter.prototype.setCollisionBox = function(x, y) {
	this.collisionBox.x = x;
	this.collisionBox.y = y;
}


HeroCharacter.prototype.getCenter = function() {
	if(this.currentImage == null)
		this.currentImage = this.idleImages[0];
	
	var x = this.position.x * blockSize;
	var y = height - blockSize - this.position.y * blockSize;

	x += (blockSize - this.currentImage.width) / 2;
	y += blockSize - this.currentImage.height;

	x += this.currentImage.width / 2;
	y += this.currentImage.height / 2;
	
	var returnVector = function () {  return {  x: null,  y: null  };  };
	returnVector.x = x;
	returnVector.y = y;
	
	return returnVector;
}

HeroCharacter.prototype.displaceByPixels = function(x, y) {
	this.position.x += x / blockSize;
	this.position.y += y / blockSize;
}











// RENDERING

HeroCharacter.prototype.draw = function(scroll, mapSize) {
	if(this.currentImage == null)
		this.currentImage = this.idleImages[0];
	
	this.switchAnimationIndex();

	imageX = width/2 - this.currentImage.width / 2;
	imageY = height/2 - this.currentImage.height / 2;

	var offSet = getPixelOffsetFromScroll(scroll, mapSize);

	imageX += offSet.x;
	imageY -= offSet.y;

	if(this.lookingright) {
		ctx.drawImage(this.currentImage, imageX, imageY);
	}
	else {
		for(var x = 0; x < this.currentImage.width; x++) {
			ctx.drawImage(this.currentImage, x, 0, 1, this.currentImage.height, this.currentImage.width - x + imageX, imageY, 1, this.currentImage.height);
		}
	}
}

HeroCharacter.prototype.switchAnimationIndex = function() {
	var timePassed = (new Date()).getTime() - this.lastAnimationChange;
	
	if(timePassed > this.animationSpeed) {
		this.currentImageIndex ++;
		
		this.lastAnimationChange = (new Date()).getTime();
	}
}

HeroCharacter.prototype.drawCollisionBox = function() {
	ctx.fillStyle = "rgba(255,0,0, 0.5)";
	var x = width/2 - this.collisionBox.x / 2;
	var y = height/2 - this.collisionBox.y / 2;
	var w = width/2 + this.collisionBox.x / 2 - x;
	var h = height/2 + this.collisionBox.y / 2 - y;
	ctx.fillRect(x, y, w, h);
}









// MOVEMENT

HeroCharacter.prototype.move = function(blocks, gravity, exitGates) {
	var intendedMove = function () {  return {  x: 0,  y: 0  };  };
	intendedMove.x = 0;
	intendedMove.y = 0;
	
	if(keyDownArray.includes(37))
		intendedMove = this.moveLeft(intendedMove);
	
	if(keyDownArray.includes(39))
		intendedMove = this.moveRight(intendedMove);
	
	if(keyDownArray.includes(38))
		this.jump();
	
	intendedMove.y += Math.pow((gravity / 1000) * this.jumpingState, 2);
	intendedMove.y -= Math.min(Math.pow((gravity / 1000) * this.fallingState, 2), 0.1);
	
	this.fallingState++;
	this.jumpingState = Math.max(this.jumpingState-1, 0);
	
	var steps = this.speed;
	var stopMovementX = false;
	var stopMovementY = false;
	
	for(var i=0; i<steps; i++) {
		if(!stopMovementY) {
			this.position.y += (i/steps) * intendedMove.y;
			
			if(this.collisionWithBlocks(blocks)) {
				if(intendedMove.y < 0) {
					this.fallingState = 0;
				}
				this.jumpingState = 0;
				this.position.y -= (i/steps) * intendedMove.y;
				stopMovementY = true;
			}
		}

		if(!stopMovementX) {
			this.position.x += (i/steps) * intendedMove.x;
			
			if(this.collisionWithBlocks(blocks)) {
				this.position.x -= (i/steps) * intendedMove.x;
				stopMovementX = true;
			}
		}
	}
	
	// Check whether the character entered an exit gate
	var newPageKey = this.collisionWithExitGate(exitGates);
	if(newPageKey != null) {
		switchPage(newPageKey);
	}
	
	// Check whether the character fell to death
	if(this.position.y < -1)
		this.health = 0;
	
	// Switch Images to characters current state
	if(this.fallingState > 5) {
		if(this.currentImageIndex >= this.jumpImages.length)
			this.currentImageIndex = 0;
		this.currentImage = this.jumpImages[this.currentImageIndex];
	}
	else if(!keyDownArray.includes(37) && !keyDownArray.includes(39) && !keyDownArray.includes(38)) {
		if(this.currentImageIndex >= this.idleImages.length)
			this.currentImageIndex = 0;
		this.currentImage = this.idleImages[this.currentImageIndex];
	}
}



HeroCharacter.prototype.moveLeft = function(intendedMove) {
	if(this.currentImageIndex >= this.runImages.length)
		this.currentImageIndex = 0;
	this.currentImage = this.runImages[this.currentImageIndex];
	
	this.lookingright = false;
	intendedMove.x -= this.speed / 100;
	
	return intendedMove;
}

HeroCharacter.prototype.moveRight = function(intendedMove) {
	if(this.currentImageIndex >= this.runImages.length)
		this.currentImageIndex = 0;
	this.currentImage = this.runImages[this.currentImageIndex];
	
	this.lookingright = true;
	intendedMove.x += this.speed / 100;
	
	return intendedMove;
}

HeroCharacter.prototype.jump = function() {
	if(this.fallingState <= 5) {
		this.jumpingState = this.jumpPower;
	}
}



HeroCharacter.prototype.collisionWithBlocks = function(blocks) {
	for(var i=0; i<blocks.length; i++) {
		if(!blocks[i].isSolid)
			continue;
		
		if(blocks[i].getCenter().x-blockSize/2 < this.getCenter().x+this.collisionBox.x/2) {
			// hinter linker Kante
			if(blocks[i].getCenter().x+blockSize/2 > this.getCenter().x-this.collisionBox.x/2) {
				// vor rechter Kante
				if(blocks[i].getCenter().y-blockSize/2 < this.getCenter().y+this.collisionBox.y/2) {
					// unter oberer Kante
					if(blocks[i].getCenter().y+blockSize/2 > this.getCenter().y-this.collisionBox.y/2) {
						// über unterer Kante
						
						// COLLISION
						return true;
					}
				}
			}
		}
	}
	
	return false;
}

HeroCharacter.prototype.collisionWithExitGate = function(exitGates) {
	for(var i=0; i<exitGates.length; i++) {
		if(exitGates[i].getCenter().x-blockSize/2 < this.getCenter().x+this.collisionBox.x/2) {
			// hinter linker Kante
			if(exitGates[i].getCenter().x+blockSize/2 > this.getCenter().x-this.collisionBox.x/2) {
				// vor rechter Kante
				if(exitGates[i].getCenter().y-blockSize/2 < this.getCenter().y+this.collisionBox.y/2) {
					// unter oberer Kante
					if(exitGates[i].getCenter().y+blockSize/2 > this.getCenter().y-this.collisionBox.y/2) {
						// über unterer Kante
						
						// COLLISION
						return exitGates[i].newPageKey;
					}
				}
			}
		}
	}
	
	return null;
}












