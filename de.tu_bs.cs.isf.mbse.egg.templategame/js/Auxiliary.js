function getPixelOffsetFromScroll(scroll, mapSize) {
	var returnVector = function () {  return {  x: null,  y: null  };  };
	
	var leftGap = Math.min(0, scroll.x * mapSize.x * blockSize - width/2 + blockSize/2);
	var rightGap = (-1) * Math.min(0, mapSize.x * blockSize - scroll.x * mapSize.x * blockSize - width/2 + blockSize/2);

	var lowerGap = Math.min(0, (1-scroll.y) * mapSize.y * blockSize - height/2 + blockSize);
	var upperGap = (-1) * Math.min(0, mapSize.y * blockSize - (1-scroll.y) * mapSize.y * blockSize - height/2);

	returnVector.x = leftGap;
	returnVector.y = lowerGap;

	if(rightGap != 0)
		returnVector.x = rightGap;
	if(upperGap != 0)
		returnVector.y = upperGap;

	if(leftGap != 0 && rightGap != 0)
		returnVector.x = 0;
	if(lowerGap != 0 && upperGap != 0)
		returnVector.y = 0;

	if(mapSize.y * blockSize < height) {
		returnVector.y = lowerGap;
	}
	if(mapSize.x * blockSize < width) {
		returnVector.x = width/2 - mapSize.x * blockSize / 2 + leftGap;
	}
	
//	if(Math.round(Math.random() * 1000) == 1) {
//		console.log("leftGap", leftGap);
//		console.log("rightGap", rightGap);
//		console.log("sum", leftGap+rightGap);
//		console.log("--------------------");
//	}
	
	return returnVector;
}