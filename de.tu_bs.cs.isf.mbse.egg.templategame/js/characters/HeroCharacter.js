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
    
    // This handsome bugger will only calculate the blinking of your hero when he gets damaged
    var magicBlinkNumber = Math.round((Math.sin(((1000 - Math.max(0, 1000 - ((new Date()).getTime() - this.lastDamageDealt))) / 1000) * Math.PI*10)+1)/2);
    if(this.life <= 0)
        magicBlinkNumber = 0;

    if(magicBlinkNumber === 0) {
        if(this.lookingright) {
            ctx.drawImage(this.currentImage, imageX, imageY);
        }
        else {
            for(var x = 0; x < this.currentImage.width; x++) {
                ctx.drawImage(this.currentImage, x, 0, 1, this.currentImage.height, this.currentImage.width - x + imageX, imageY, 1, this.currentImage.height);
            }
        }
    }
    
    // draw Collision Box
    if(this.showCollisionBox) {
        ctx.fillStyle = "rgba(255, 100, 100, 0.5)";
        ctx.fillRect(imageX + this.currentImage.width / 2 - this.collisionBoxX / 2, imageY + this.currentImage.height / 2 - this.collisionBoxY / 2, this.collisionBoxX, this.collisionBoxY);
    }
}





HeroCharacter.prototype.getIntendedMove = function(blocks, gravity) {
    var intendedMove = function () {  return {  x: 0,  y: 0  };  };
	intendedMove.x = 0;
	intendedMove.y = 0;
	
	if(keyDownArray.includes(37))
		intendedMove = this.moveLeft(intendedMove);
	
	if(keyDownArray.includes(39))
		intendedMove = this.moveRight(intendedMove);
	
	if(keyDownArray.includes(38))
		this.jump(1);
    
    return intendedMove;
}












HeroCharacter.prototype.collisionWithSpecialBlocks = function(specialBlocks) {
	for(var i=0; i<specialBlocks.length; i++) {
		if(specialBlocks[i].getCenter().x-blockSize/2 < this.getCenter().x+this.collisionBoxX/2) {
			// hinter linker Kante
			if(specialBlocks[i].getCenter().x+blockSize/2 > this.getCenter().x-this.collisionBoxX/2) {
				// vor rechter Kante
				if(specialBlocks[i].getCenter().y-blockSize/2 < this.getCenter().y+this.collisionBoxY/2) {
					// unter oberer Kante
					if(specialBlocks[i].getCenter().y+blockSize/2 > this.getCenter().y-this.collisionBoxY/2) {
						// über unterer Kante
						
						// COLLISION
						return specialBlocks[i].newPageKey;
					}
				}
			}
		}
	}
	
	return null;
}

