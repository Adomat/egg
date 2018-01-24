EnemyCharacter.prototype = new Character();

function EnemyCharacter() {
    this.movable = false;
    
    this.currentMove = 0;
}








EnemyCharacter.prototype.draw = function(scroll, mapSize) {
	if(this.currentImage == null)
		this.currentImage = this.idleImages[0];
	
	this.switchAnimationIndex();
    
	var imageX = this.positionX * blockSize;
	var imageY = height - this.positionY * blockSize;

	imageX -= scroll.x * mapSize.x * blockSize - width/2 + blockSize/2;
	imageY -= height/2 - (1-scroll.y) * mapSize.y * blockSize;
	
	var offSet = getPixelOffsetFromScroll(scroll, mapSize);
	
	imageX += offSet.x;
	imageY -= offSet.y;
    
    imageY -= this.currentImage.height/2;

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







EnemyCharacter.prototype.postProcessMovement = function(blocks, gravity, exitGates) {
    // TODO detect collision with hero?
}

EnemyCharacter.prototype.getIntendedMove = function(blocks, gravity, exitGates) {
    console.log(this.positionY);
    
    var intendedMove = function () {  return {  x: 0,  y: 0  };  };
	intendedMove.x = 0;
	intendedMove.y = 0;
	
    if(this.currentMove == 0) {
        if(Math.random() * 100 < 1) {
            this.currentMove = Math.round((Math.random() - 0.5) * 150);
        }
    }
    else if(this.currentMove > 0) {
        this.moveRight(intendedMove);
        this.currentMove --;
    }
    else if(this.currentMove < 0) {
        this.moveLeft(intendedMove);
        this.currentMove ++;
    }
    
    if(Math.random() * 100 < 1)
        this.jump();
    
    return intendedMove;
}

EnemyCharacter.prototype.jump = function() {
	if(this.fallingState <= 5) {
		this.jumpingState = this.jumpPower * Math.random();
	}
}