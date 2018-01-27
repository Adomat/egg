function Character() {
	this.positionX = null;
	this.positionY = null;
	this.collisionBoxX = null;
    this.collisionBoxY = null;
	
	this.idleImages = [];
	this.attackImages = [];
	this.runImages = [];
	this.jumpImages = [];
	
	this.animationSpeed = 300;
	this.life = 100;
    this.strength = 10;
	this.speed = 5;
	this.jumpPower = 50;
    this.showCollisionBox = false;
	
	this.lookingright = true;
	this.fallingState = 0;
	this.jumpingState = 0;
    
    this.currentImage = null;
	this.currentImageIndex = 0;
	this.lastAnimationChange = 0;
	this.lastDamageDealt = 0;
    
    this.movable = false;
}




Character.prototype.addIdleImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
    
    console.log("addIdleImage: " + this.idleImages.length + " : " + imageURL);
	this.idleImages.push(image);
}

Character.prototype.addAttackImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.attackImages.push(image);
}

Character.prototype.addRunImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.runImages.push(image);
}

Character.prototype.addJumpImage = function(imageURL) {
	var image = new Image();
	image.src = imageURL;
	
	this.jumpImages.push(image);
}

Character.prototype.setCollisionBox = function(x, y) {
	this.collisionBoxX = x;
	this.collisionBoxY = y;
}


Character.prototype.getCenter = function() {
  if(this.currentImage == null)
    this.currentImage = this.idleImages[0];
  
  var x = this.positionX * blockSize;
  var y = height - blockSize - this.positionY * blockSize;

  x += blockSize / 2;
  
  var returnVector = function () {  return {  x: null,  y: null  };  };
  returnVector.x = x;
  returnVector.y = y;
  
  return returnVector;
}

Character.prototype.displaceByPixels = function(x, y) {
	this.positionX += x / blockSize;
	this.positionY += y / blockSize;
}

Character.prototype.dealDamage = function(damage) {
    var timePassed = (new Date()).getTime() - this.lastDamageDealt;
	
	if(timePassed > 1000) {
        this.life -= damage;
        this.jump(0.5);
		
		this.lastDamageDealt = (new Date()).getTime();
	}
}
















// RENDERING


Character.prototype.switchAnimationIndex = function() {
	var timePassed = (new Date()).getTime() - this.lastAnimationChange;
	
	if(timePassed > this.animationSpeed) {
		this.currentImageIndex ++;
        this.jum
		
		this.lastAnimationChange = (new Date()).getTime();
	}
}
























// MOVEMENT

Character.prototype.getIntendedMove = function(blocks, gravity, exitGates) {
    // Implemented in inheriting classes
}


Character.prototype.move = function(blocks, gravity) {
	var intendedMove = this.getIntendedMove(blocks, gravity);
	
	intendedMove.y += Math.pow((10 / 1000) * this.jumpingState, 2);
	intendedMove.y -= Math.min(Math.pow((1 / gravity) * this.fallingState, 2), 0.2);
	
	this.fallingState++;
	this.jumpingState = Math.max(this.jumpingState-1, 0);
	
	var steps = this.speed;
	var stopMovementX = false;
	var stopMovementY = false;
	
	for(var i=0; i<steps; i++) {
		if(!stopMovementY) {
			this.positionY += (1/steps) * intendedMove.y;
			
			if(this.collisionWithBlocks(blocks)) {
				if(intendedMove.y < 0) {
					this.fallingState = 0;
				}
				this.jumpingState = 0;
				this.positionY -= (1/steps) * intendedMove.y;
				stopMovementY = true;
			}
		}

		if(!stopMovementX) {
			this.positionX += (1/steps) * intendedMove.x;
			
			if(this.collisionWithBlocks(blocks)) {
				this.positionX -= (1/steps) * intendedMove.x;
				stopMovementX = true;
			}
		}
	}
	
	// Check whether the character fell to death
	if(this.positionY < -2)
		this.life = 0;
	
	// Switch Images to characters current state
	if(this.fallingState > 5) {
		if(this.currentImageIndex >= this.jumpImages.length)
			this.currentImageIndex = 0;
		this.currentImage = this.jumpImages[this.currentImageIndex];
	}
    else if(intendedMove.x == 0) {
        if(this.currentImageIndex >= this.idleImages.length)
			this.currentImageIndex = 0;
		this.currentImage = this.idleImages[this.currentImageIndex];
    }
	/*else if(!keyDownArray.includes(37) && !keyDownArray.includes(39) && !keyDownArray.includes(38)) {
		if(this.currentImageIndex >= this.idleImages.length)
			this.currentImageIndex = 0;
		this.currentImage = this.idleImages[this.currentImageIndex];
	}*/
}



Character.prototype.collisionWithBlocks = function(blocks) {
	for(var i=0; i<blocks.length; i++) {
		if(!blocks[i].isSolid)
			continue;
		
		if(blocks[i].getCenter().x-blockSize/2 < this.getCenter().x+this.collisionBoxX/2) {
			// hinter linker Kante
			if(blocks[i].getCenter().x+blockSize/2 > this.getCenter().x-this.collisionBoxX/2) {
				// vor rechter Kante
				if(blocks[i].getCenter().y-blockSize/2 < this.getCenter().y+this.collisionBoxY/2) {
					// unter oberer Kante
					if(blocks[i].getCenter().y+blockSize/2 > this.getCenter().y-this.collisionBoxY/2) {
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



Character.prototype.moveLeft = function(intendedMove) {
	if(this.currentImageIndex >= this.runImages.length)
		this.currentImageIndex = 0;
	this.currentImage = this.runImages[this.currentImageIndex];
	
	this.lookingright = false;
	intendedMove.x -= this.speed / blockSize;
	
	return intendedMove;
}

Character.prototype.moveRight = function(intendedMove) {
	if(this.currentImageIndex >= this.runImages.length)
		this.currentImageIndex = 0;
	this.currentImage = this.runImages[this.currentImageIndex];
	
	this.lookingright = true;
	intendedMove.x += this.speed / blockSize;
	
	return intendedMove;
}

Character.prototype.jump = function(jumpHeight) {
	if(this.fallingState <= 5) {
		this.jumpingState = this.jumpPower * jumpHeight;
	}
}