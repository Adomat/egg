HeroCharacter.prototype = new Character();

function HeroCharacter() {
    this.movable = true;
}





HeroCharacter.prototype.draw = function(scroll, mapSize) {
	if(this.currentImage == null)
		this.currentImage = this.idleImages[0];
	
	this.switchAnimationIndex();

	var imageX = width/2 - this.currentImage.width / 2;
	var imageY = height/2 - this.currentImage.height / 2;

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
    
    // draw Collision Box
    if(this.showCollisionBox) {
        ctx.fillStyle = "rgba(255, 100, 100, 0.5)";
        ctx.fillRect(imageX + this.currentImage.width / 2 - this.collisionBoxX / 2, imageY + this.currentImage.height / 2 - this.collisionBoxY / 2, this.collisionBoxX, this.collisionBoxY);
    }
}






HeroCharacter.prototype.postProcessMovement = function(blocks, gravity, exitGates) {
    // Check whether the character entered an exit gate
	var newPageKey = this.collisionWithExitGate(exitGates);
	if(newPageKey != null) {
		switchPage(newPageKey);
	}
}

HeroCharacter.prototype.getIntendedMove = function(blocks, gravity, exitGates) {
    var intendedMove = function () {  return {  x: 0,  y: 0  };  };
	intendedMove.x = 0;
	intendedMove.y = 0;
	
	if(keyDownArray.includes(37))
		intendedMove = this.moveLeft(intendedMove);
	
	if(keyDownArray.includes(39))
		intendedMove = this.moveRight(intendedMove);
	
	if(keyDownArray.includes(38))
		this.jump();
    
    return intendedMove;
}

HeroCharacter.prototype.jump = function() {
	if(this.fallingState <= 5) {
		this.jumpingState = this.jumpPower;
	}
}












HeroCharacter.prototype.collisionWithExitGate = function(exitGates) {
	for(var i=0; i<exitGates.length; i++) {
		if(exitGates[i].getCenter().x-blockSize/2 < this.getCenter().x+this.collisionBoxX/2) {
			// hinter linker Kante
			if(exitGates[i].getCenter().x+blockSize/2 > this.getCenter().x-this.collisionBoxX/2) {
				// vor rechter Kante
				if(exitGates[i].getCenter().y-blockSize/2 < this.getCenter().y+this.collisionBoxY/2) {
					// unter oberer Kante
					if(exitGates[i].getCenter().y+blockSize/2 > this.getCenter().y-this.collisionBoxY/2) {
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



